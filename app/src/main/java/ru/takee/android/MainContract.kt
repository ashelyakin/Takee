package ru.takee.android

import android.graphics.Bitmap
import android.net.Uri
import ru.takee.android.models.PetModel

data class MainState(
    val pets: List<PetModel> = emptyList(),
    val isAnalysing: Boolean = false
)

sealed interface MainIntent {
    data class ImagePicked(val uri: Uri) : MainIntent
    data class ImagesPicked(val images: List<Pair<Bitmap, Uri>>) : MainIntent
    data class PickFromGallery(val multiple: Boolean) : MainIntent
    data class SavePet(val pet: PetModel) : MainIntent
    data class RemovePet(val pet: PetModel) : MainIntent
}

sealed interface MainResult {
    data class PetsLoaded(val pets: List<PetModel>) : MainResult
    data object AnalysingStarted : MainResult
    data object AnalysingFinished : MainResult
}

sealed interface MainEffect {
    data class OpenGallery(val multiple: Boolean) : MainEffect
}