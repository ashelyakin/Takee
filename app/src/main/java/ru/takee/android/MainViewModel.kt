package ru.takee.android

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.takee.android.color.ColorUtils
import ru.takee.android.cv.CVManager
import ru.takee.android.cv.ImageProcessor
import ru.takee.android.db.PetDao
import ru.takee.android.models.PetModel
import ru.takee.android.models.toModel
import ru.takee.android.models.toPetEntity
import ru.takee.android.utils.FileHelper.getRealPathFromURI

class MainViewModel(
    application: Application
): AndroidViewModel(application) {

    private val petDao: PetDao = application.get()

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val cvManager: CVManager = application.get()

    private val _mainEffect = Channel<MainEffect>(Channel.BUFFERED)
    val mainEffect = _mainEffect.receiveAsFlow()   // собирает MainActivity

    private val _imageEffect = Channel<String?>(Channel.BUFFERED)
    val imageEffect = _imageEffect.receiveAsFlow()  // собирает PetCardScreen

    init {
        petDao.getAll()
            .map { it.map { p -> p.toModel() }.sortedByDescending { it.timestamp } }
            .onEach { pets -> emitResult(MainResult.PetsLoaded(pets)) }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.PickFromGallery -> viewModelScope.launch { _mainEffect.send(MainEffect.OpenGallery(intent.multiple)) }
            is MainIntent.ImagePicked -> handleImagePicked(intent.uri)
            is MainIntent.ImagesPicked -> handleImagesPicked(intent.images)
            is MainIntent.SavePet -> savePetToDatabase(intent.pet)
            is MainIntent.RemovePet -> removePetFromDatabase(intent.pet)
        }
    }

    private fun emitResult(result: MainResult) {
        _state.update { reduce(it, result) }
    }

    private fun reduce(state: MainState, result: MainResult): MainState = when (result) {
        is MainResult.PetsLoaded -> state.copy(pets = result.pets)
        is MainResult.AnalysingStarted -> state.copy(isAnalysing = true)
        is MainResult.AnalysingFinished -> state.copy(isAnalysing = false)
    }

    private fun handleImagePicked(uri: Uri){
        viewModelScope.launch {
            val path = getRealPathFromURI(getApplication<Application>().applicationContext, uri)
            _imageEffect.send(path)
        }
    }

    private fun handleImagesPicked(images: List<Pair<Bitmap, Uri>>){
        viewModelScope.launch(Dispatchers.IO) {
            emitResult(MainResult.AnalysingStarted)
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
            emitResult(MainResult.AnalysingFinished)
        }
    }

    private fun savePetToDatabase(petModel: PetModel){
        viewModelScope.launch {
            petDao.add(petModel.toPetEntity())
        }
    }

    private fun removePetFromDatabase(petModel: PetModel){
        viewModelScope.launch {
            petDao.delete(petModel.toPetEntity())
        }
    }

}