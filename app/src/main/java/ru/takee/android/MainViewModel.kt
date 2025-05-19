package ru.takee.android

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.takee.android.color.ColorUtils
import ru.takee.android.cv.CVManager
import ru.takee.android.cv.ImageProcessor
import ru.takee.android.models.PetCategory
import ru.takee.android.db.PetDao
import ru.takee.android.models.PetModel
import ru.takee.android.models.toModel
import ru.takee.android.models.toPetEntity
import ru.takee.android.utils.FileHelper.getRealPathFromURI

class MainViewModelFactory(
    private val application: Application,
    private val imageResultLauncher: ActivityResultLauncher<Intent>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application, imageResultLauncher) as T
    }
}

class MainViewModel(
    application: Application,
    private val imageResultLauncher: ActivityResultLauncher<Intent>

): AndroidViewModel(application) {

    private val _imageFlow = MutableSharedFlow<String?>()
    val imageFlow = _imageFlow.asSharedFlow()

    private val petDao: PetDao = application.get()
    val petsFlow = petDao.getAll().map { it.map { it.toModel() }.sortedByDescending { it.timestamp } }

    private val cvManager: CVManager = application.get()

    val isAnalysing = MutableStateFlow(false)

    fun onImageReady(uri: Uri){
        viewModelScope.launch {
            _imageFlow.emit(getRealPathFromURI(getApplication<Application>().applicationContext, uri))
        }
    }

    fun onImagesReady(images: List<Pair<Bitmap, Uri>>){
        viewModelScope.launch(Dispatchers.IO) {
            isAnalysing.value = true
            images.forEach {
                try {
                    val byteBuffer = ImageProcessor.preprocessImage(it.first, 640, 640, Color(56, 56, 56))
                    val modelResult = cvManager.imageDetectionAndClassification(byteBuffer) ?: return@forEach
                    val originalBoxes = modelResult.boxes
                    //при оберзке используем уменьшенную высоту и ширину, чтобы попало меньше фона
                    val reducedBoxes = listOf(originalBoxes[0], originalBoxes[1], originalBoxes[2] / 1.5f, originalBoxes[3] / 1.5f)
                    val croppedBitmap = ImageProcessor.cropBitmap(it.first, reducedBoxes) ?: return@forEach
                    it.first.recycle()
                    Palette.from(croppedBitmap).generate { palette ->
                        val dominantColor = palette?.getDominantColor(0x000000)
                        savePetToDatabase(PetModel(
                            name = "Питомец",
                            description = "",
                            color = dominantColor?.let {
                                ColorUtils.findClosestColor(
                                    dominantColor
                                )?.text ?: ""
                            } ?: "",
                            category = modelResult.category.id,
                            imgPath = getRealPathFromURI(
                                getApplication<Application>().applicationContext,
                                it.second
                            )
                        ))
                        croppedBitmap.recycle()
                    }
                } catch (e: Exception){
                    Log.e("MainViewModel", e.stackTraceToString())
                }
            }
            isAnalysing.value = false
        }
    }

    fun pickFromGallery(){
        imageResultLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    fun pickMultipleFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imageResultLauncher.launch(intent)
    }

    fun savePetToDatabase(petModel: PetModel){
        viewModelScope.launch {
            petDao.add(petModel.toPetEntity())
        }
    }

    fun removePetFromDatabase(petModel: PetModel){
        viewModelScope.launch {
            petDao.delete(petModel.toPetEntity())
        }
    }

}