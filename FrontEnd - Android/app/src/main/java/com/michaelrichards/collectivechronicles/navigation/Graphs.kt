package com.michaelrichards.collectivechronicles.navigation

sealed class Graphs(val graphName: String) {
    data object SplashGraph : Graphs("splash")
    data object AuthGraph : Graphs("auth")
    data object MainGraph : Graphs("main")
}