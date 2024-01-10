package com.michaelrichards.collectivechronicles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.michaelrichards.collectivechronicles.screens.loginScreen.LoginScreen
import com.michaelrichards.collectivechronicles.screens.registrationScreen.RegistrationScreen
import com.michaelrichards.collectivechronicles.screens.splashScreen.SplashScreen

@Composable
fun UserNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Graphs.AuthGraph.graphName){

        navigation(
            route = Graphs.SplashGraph.graphName,
            startDestination = Screens.SplashScreen.routeName
        ) {
            composable(route = Screens.SplashScreen.routeName) {
                SplashScreen(navController = navController)
            }
        }

        navigation(
            route = Graphs.AuthGraph.graphName,
            startDestination = Screens.LoginScreen.routeName
        ) {
            composable(route = Screens.LoginScreen.routeName) {
                LoginScreen(navController = navController)
            }
            composable(route = Screens.RegistrationScreen.routeName) {
                RegistrationScreen(navController = navController)
            }
        }
    }


}