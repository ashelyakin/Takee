package ru.takee.android.models

data class PetDetectionClassificationResult(
    val boxes: List<Float>,
    val category: PetCategory
)