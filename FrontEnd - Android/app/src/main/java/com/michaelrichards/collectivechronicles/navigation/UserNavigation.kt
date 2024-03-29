package com.michaelrichards.collectivechronicles.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.michaelrichards.collectivechronicles.screens.accountScreen.AccountScreen
import com.michaelrichards.collectivechronicles.screens.findStoriesScreen.FindStoriesScreen
import com.michaelrichards.collectivechronicles.screens.homeScreen.HomeScreen
import com.michaelrichards.collectivechronicles.screens.loginScreen.LoginScreen
import com.michaelrichards.collectivechronicles.screens.registrationScreen.RegistrationScreen
import com.michaelrichards.collectivechronicles.screens.splashScreen.SplashScreen
import com.michaelrichards.collectivechronicles.screens.startStoryScreen.StartStoryScreen
import com.michaelrichards.collectivechronicles.screens.storyScreen.StoryScreen

@Composable
fun UserNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Graphs.SplashGraph.graphName){

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
        
        navigation(
            route = Graphs.MainGraph.graphName,
            startDestination = Screens.HomeScreen.routeName
        ){
            composable(route = Screens.HomeScreen.routeName){
               HomeScreen(navController = navController)
            }
            composable(route = Screens.StartStoryScreen.routeName){
                StartStoryScreen(navController = navController)
            }

            composable(route = "${Screens.StoryScreen.routeName}/{storyId}", arguments = listOf(
                navArgument("storyId"){type = NavType.LongType}
            )){backStackEntry ->
                backStackEntry.arguments?.getLong("storyId")
                    ?.let { StoryScreen(navController = navController, storyId = it) }
            }

            composable(route = Screens.AccountScreen.routeName){
                AccountScreen(navController = navController)
            }
            composable(route = Screens.FindStoriesScreen.routeName){
                FindStoriesScreen(navController = navController)
            }
        }
    }


}