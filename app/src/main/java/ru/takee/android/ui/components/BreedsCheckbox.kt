package ru.takee.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.takee.android.models.PetCategory
import ru.takee.android.ui.theme.Accent

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF )
@Composable
fun BreedsCheckboxPreview(){
    BreedsCheckbox(){}
}

@Composable
fun BreedsCheckbox(
    modifier: Modifier = Modifier,
    onNewSelectedBreeds: (List<PetCategory>) -> Unit) {
    val breeds = listOf(
        PetCategory.GOLDEN_RETRIEVER,
        PetCategory.GERMAN_SHEPHERD,
        PetCategory.DACHSHUND)
    val breedsCheckedStates = remember { mutableStateListOf(true, true, true) }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            breeds.forEachIndexed { index, it ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(it.toString())
                    Checkbox(
                        checked = breedsCheckedStates[index],
                        colors = CheckboxDefaults.colors().copy(
                            checkedBoxColor = Accent,
                            checkedBorderColor = Accent,
                            uncheckedBorderColor = Accent
                        ),
                        onCheckedChange = { isChecked ->
                            breedsCheckedStates[index] = isChecked
                            val selectedBreeds = mutableListOf<PetCategory>()
                            breeds.forEachIndexed { index, it ->
                                if (breedsCheckedStates[index])
                                    selectedBreeds += it
                            }
                            onNewSelectedBreeds(selectedBreeds)
                        }
                    )
                }
            }
        }
    }
}
