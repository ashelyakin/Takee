package ru.takee.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldWithFilling(title: String, value: String, fillingColor: Color, onNewValue: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        TextField(
            colors = getBaseTextFieldColors().copy(
                focusedContainerColor = fillingColor,
                unfocusedContainerColor = fillingColor
            ),
            value = value,
            onValueChange = { onNewValue(it) },
            label = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}