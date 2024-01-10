package com.michaelrichards.collectivechronicles.screens.splashScreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.navigation.Graphs

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {

    navController.navigate(Graphs.AuthGraph.graphName){
        popUpTo(Graphs.SplashGraph.graphName){
            inclusive = true
        }
    }

}