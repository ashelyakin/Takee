package ru.takee.android.ui.home

data class AddPetDialogParams(
    val onCreateManuallyClicked: () -> Unit,
    val onCreateViaAIClicked: () -> Unit?
)