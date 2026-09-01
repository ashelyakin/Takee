package ru.takee.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.Flow
import ru.takee.android.MainIntent
import ru.takee.android.MainState
import ru.takee.android.MainViewModel
import ru.takee.android.models.PetModel
import ru.takee.android.ui.home.HomeScreen
import ru.takee.android.ui.pet.PetCardScreen
import ru.takee.android.utils.fromJson

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object PetCard : Screen("petCard/{model}")
    data object Profile : Screen("profile")
}

@Composable
fun NavHostScreen(state: MainState, onIntent: (MainIntent) -> Unit, imageEffect: Flow<String?>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.background(Color.White)
            .padding(top = 44.dp)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, state.pets){
                onIntent(MainIntent.PickFromGallery(multiple = true))
            }
        }
        composable(
            route = Screen.PetCard.route,
            arguments = listOf(
                navArgument("model"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("model") ?: ""
            val model = json.fromJson<PetModel>()
            PetCardScreen(navController, model, imageEffect,
                pickPhotoCallback = {
                    onIntent(MainIntent.PickFromGallery(multiple = false))
                },
                onSavePet = {
                    onIntent(MainIntent.SavePet(it))
                },
                onRemovePet = {
                    onIntent(MainIntent.RemovePet(it))
                }
            )
        }
    }
}