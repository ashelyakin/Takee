package ru.takee.android.ui.pet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.takee.android.R
import ru.takee.android.models.PetCategory
import ru.takee.android.models.PetModel
import ru.takee.android.ui.components.PhotoFrame
import ru.takee.android.ui.components.TextFieldWithStroke
import ru.takee.android.ui.components.TextFieldWithFilling
import ru.takee.android.ui.theme.Accent
import ru.takee.android.ui.theme.Accent_20
import ru.takee.android.utils.toPetCategory

@Preview(widthDp = 400, heightDp = 850, showBackground = true, backgroundColor = 0xFFFFFFFF )
@Composable
fun PetCardScreenPreview(){
    val navController = rememberNavController()
    PetCardScreen(navController, PetModel(), MutableSharedFlow(), {}, {}, {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetCardScreen(
    navController: NavHostController,
    model: PetModel?,
    imagesFlow: SharedFlow<String?>,
    pickPhotoCallback: () -> Unit,
    onSavePet: (PetModel) -> Unit,
    onRemovePet: (PetModel) -> Unit
) {
    val image = imagesFlow.collectAsState(initial = null)
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        var nameValue by remember { mutableStateOf(model?.name ?: "") }
        var descriptionValue by remember { mutableStateOf(model?.description ?: "") }
        val realCategory = model?.category?.toPetCategory()
        var speciesCategory by remember { mutableStateOf(
            if (realCategory?.isDogCategory() == true)
                PetCategory.DOG
            else
                realCategory
        ) }
        var breed by remember { mutableStateOf(
            if (realCategory?.isDogCategory() == true) {
                if (realCategory == PetCategory.DOG) PetCategory.NONE else realCategory
            } else
                null
        ) }
        var colorValue by remember { mutableStateOf(model?.color ?: "") }

        ToolBar(navController, model, onRemovePet)
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp),
            ) {
                PhotoFrame(image.value ?: model?.imgPath, pickPhotoCallback)
                Spacer(modifier = Modifier.height(24.dp))
                TextFieldWithStroke("Имя", nameValue, Accent, {
                    nameValue = it
                })
                Spacer(modifier = Modifier.height(24.dp))
                val speciesCategories = listOf(PetCategory.DOG, PetCategory.CAT, PetCategory.RABBIT, PetCategory.TURTLE, PetCategory.NONE)
                DropdownCategoriesField(
                    title = "Категория",
                    items = speciesCategories,
                    selectedItem = speciesCategory,
                    onItemSelected = { speciesCategory = it },
                    strokeColor = Accent,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (speciesCategory?.isDogCategory() == true) {
                    val breeds = listOf(PetCategory.GERMAN_SHEPHERD, PetCategory.GOLDEN_RETRIEVER, PetCategory.DACHSHUND, PetCategory.NONE)
                    DropdownCategoriesField(
                        title = "Порода",
                        items = breeds,
                        selectedItem = breed,
                        onItemSelected = { breed = it },
                        strokeColor = Accent,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                TextFieldWithStroke("Цвет", colorValue, Accent, {
                    colorValue = it
                })
                Spacer(modifier = Modifier.height(24.dp))

                TextFieldWithFilling("Описание", descriptionValue, Accent_20) {
                    descriptionValue = it
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Accent,
                    contentColor = Color.White
                ),
                onClick = {
                    val realCategoryForSaving = if (speciesCategory?.isDogCategory() == true && breed != null && breed != PetCategory.NONE) breed else speciesCategory
                    onSavePet(
                        PetModel(
                            id = model?.id ?: 0,
                            name = nameValue,
                            description = descriptionValue,
                            category = realCategoryForSaving?.id ?: PetCategory.NONE.id,
                            imgPath = (image.value ?: model?.imgPath),
                            color = colorValue
                        )
                    )
                    navController.navigateUp()
                }
            ) {
                Text(
                    modifier =  Modifier.padding(vertical = 10  .dp),
                    text = "Сохранить"
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }

}

@Composable
private fun ToolBar(
    navController: NavHostController,
    model: PetModel?,
    onRemovePet: (PetModel) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .size(48.dp),
            onClick = {
                navController.navigateUp()
            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = null
            )
        }

        if (model != null) {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .size(48.dp),
                onClick = {
                    onRemovePet(model)
                    navController.navigateUp()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = null
                )
            }
        }
    }
}
