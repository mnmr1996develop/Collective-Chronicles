package com.michaelrichards.collectivechronicles.navigation

sealed class Screens(val routeName: String){
    data object SplashScreen : Screens("splash-screen")
    data object RegistrationScreen : Screens("registration")
    data object LoginScreen : Screens("login")
    data object HomeScreen : Screens("home")

    data object StartStoryScreen: Screens("start-story")
    data object StoryScreen: Screens("story")
    data object AccountScreen: Screens("account")

    data object FindStoriesScreen: Screens("find-stories")
}