package ru.takee.android.ui.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.takee.android.R
import ru.takee.android.models.PetCategory
import ru.takee.android.models.PetModel
import ru.takee.android.ui.Screen
import ru.takee.android.ui.components.BreedsCheckbox
import ru.takee.android.utils.toJson
import ru.takee.android.utils.toPetCategory

@Preview(widthDp = 400, heightDp = 850, showBackground = true, backgroundColor = 0xFFFFFFFF )
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController, flow{ emit(listOf(PetModel(), PetModel(), PetModel())) }, {})
}

private const val FILTER_ALL_ID = -1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, petsFlow: Flow<List<PetModel>>, createMultipleCards: () -> Unit) {
    val pets = petsFlow.collectAsState(initial = null)
    val addPetDialogParams = AddPetDialogParams(
        onCreateManuallyClicked = {
            navController.navigate(Screen.PetCard.route)
        },
        onCreateViaAIClicked = {
            createMultipleCards()
        }
    )
    val addPetDialogParamsState: MutableState<AddPetDialogParams?> = remember { mutableStateOf(null) }
    var selectedFilterId by remember { mutableIntStateOf(FILTER_ALL_ID) }
    var selectedBreeds by remember { mutableStateOf(listOf(
        PetCategory.GOLDEN_RETRIEVER.id,
        PetCategory.GERMAN_SHEPHERD.id,
        PetCategory.DACHSHUND.id
    )) }
    var breedsFilterEnabled by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ){
            val (logoImg, btnAdd) = createRefs()
            Image(
                modifier = Modifier.constrainAs(logoImg){
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start)
                },
                painter = painterResource(id = R.drawable.takee_logo),
                contentDescription = null
            )
            IconButton(
                modifier = Modifier.constrainAs(btnAdd){
                    top.linkTo(logoImg.top)
                    bottom.linkTo(logoImg.bottom)
                    end.linkTo(parent.end)
                },
                onClick = {
                    addPetDialogParamsState.value = addPetDialogParams
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 24.dp)
            ) {
                val filters = listOf(
                    Triple(FILTER_ALL_ID, "Все", R.drawable.ic_filter_all),
                    Triple(PetCategory.DOG.id ,PetCategory.DOG.toString(), R.drawable.ic_filter_dogs),
                    Triple(PetCategory.CAT.id, PetCategory.CAT.toString(), R.drawable.ic_filter_cats),
                    Triple(PetCategory.RABBIT.id, PetCategory.RABBIT.toString(), R.drawable.ic_filter_rabbits),
                    Triple(PetCategory.TURTLE.id, PetCategory.TURTLE.toString(), R.drawable.ic_filter_turteles)
                )
                items(filters){
                    FiltersItem(it, selectedFilterId == it.first){
                        selectedFilterId = it
                        breedsFilterEnabled = false
                        selectedBreeds = listOf(
                            PetCategory.GOLDEN_RETRIEVER.id,
                            PetCategory.GERMAN_SHEPHERD.id,
                            PetCategory.DACHSHUND.id
                        )
                    }
                }
            }
        }

        if (selectedFilterId == PetCategory.DOG.id) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 20.dp),
                onClick = {
                    breedsFilterEnabled = !breedsFilterEnabled
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_filter_settings),
                    contentDescription = null
                )
            }
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (breedsFilterEnabled && selectedFilterId == PetCategory.DOG.id) {
            BreedsCheckbox(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                selectedBreeds = it.map { it.id }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ){
                val filteredItems = pets.value?.filter {
                    selectedFilterId == FILTER_ALL_ID
                            || (it.category.toPetCategory().isDogCategory()
                                && selectedFilterId == PetCategory.DOG.id
                                && (selectedBreeds.contains(it.category) || !breedsFilterEnabled))
                            || (it.category.toPetCategory() == PetCategory.CAT && selectedFilterId == PetCategory.CAT.id)
                            || (it.category.toPetCategory() == PetCategory.RABBIT && selectedFilterId == PetCategory.RABBIT.id)
                            || (it.category.toPetCategory() == PetCategory.TURTLE && selectedFilterId == PetCategory.TURTLE.id)
                }
                items(filteredItems ?: emptyList()) { pet ->
                    PetCardItem(pet){
                        navController.navigate(Screen.PetCard.route
                            .replace("{model}", Uri.encode(it.toJson()))
                        )
                    }
                }
            }
        }

        if (addPetDialogParamsState.value != null){
            AddPetDialog(addPetDialogParamsState)
        }
    }
}