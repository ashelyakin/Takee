package ru.takee.android.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.takee.android.R
import ru.takee.android.ui.theme.Accent
import ru.takee.android.ui.theme.Grey

@Composable
fun AddPetDialog(addPetDialogParams: MutableState<AddPetDialogParams?>) {
    Dialog(onDismissRequest = { addPetDialogParams.value = null }) {
        AddPetDialogLayout(addPetDialogParams = addPetDialogParams)
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun AddPetDialogPreview() {
    AddPetDialog(
        addPetDialogParams = mutableStateOf(
            AddPetDialogParams(
                onCreateViaAIClicked = { },
                onCreateManuallyClicked = { }
            )
        )
    )
}

@Composable
fun AddPetDialogLayout(addPetDialogParams: MutableState<AddPetDialogParams?>) {
    val params = addPetDialogParams.value ?: return
    Box(
        modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        Column {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = stringResource(id = R.string.new_pet),
                    color = Grey,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(id = R.string.create_card),
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(modifier = Modifier.width(16.dp))
                TextButton(
                    onClick = {
                        params.onCreateManuallyClicked()
                        addPetDialogParams.value = null
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        stringResource(id = R.string.manually),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                TextButton(
                    modifier = Modifier
                        .background(Accent, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp),
                    onClick = {
                        params.onCreateViaAIClicked()
                        addPetDialogParams.value = null
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        stringResource(id = R.string.via_AI),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}