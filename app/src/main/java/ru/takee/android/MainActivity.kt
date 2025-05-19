package ru.takee.android

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.takee.android.ui.Loader
import ru.takee.android.ui.NavHostScreen
import ru.takee.android.ui.theme.TakeeTheme

class MainActivity : ComponentActivity() {

    private val requiredPermissions = mutableListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        if (it.values.any { !it }){
            Toast.makeText(this, "Необходимые разрешения не выданы", Toast.LENGTH_LONG).show()
        }
    }

    private val imageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        val singleUri = result.data?.data
                        if (singleUri != null){
                            viewModel.onImageReady(singleUri)
                        } else{
                            val clipData = result.data?.clipData
                            if (clipData != null){
                                val images = mutableListOf<Pair<Bitmap, Uri>>()
                                for (i in 0 until clipData.itemCount){
                                    val uri = clipData.getItemAt(i).uri
                                    val bitmap = MediaStore.Images.Media.getBitmap(this@MainActivity.contentResolver, uri)
                                    images.add(bitmap to uri)
                                }
                                viewModel.onImagesReady(images)
                            }
                        }
                    }catch (e: Exception){

                    }
                }
            }
        }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application, imageResultLauncher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Takee)
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(requiredPermissions.toTypedArray())

        enableEdgeToEdge()
        setContent {
            TakeeTheme {
                NavHostScreen(viewModel)
                val isAnalysing = viewModel.isAnalysing.collectAsState()
                if (isAnalysing.value){
                    Loader()
                }
            }
        }
    }
}