package ru.takee.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldWithStroke(
    title: String,
    value: String,
    strokeColor: Color,
    onNewValue: (String) -> Unit,
    readOnly: Boolean = false,
    trailingIcon: @Composable () -> Unit = {}
) {
    Card(
        border = BorderStroke(1.dp, strokeColor),
        shape = RoundedCornerShape(8.dp)
    ){
        TextField(
            colors = getBaseTextFieldColors().copy(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            value = value,
            onValueChange = { onNewValue(it) },
            label = { Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            ) },
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}