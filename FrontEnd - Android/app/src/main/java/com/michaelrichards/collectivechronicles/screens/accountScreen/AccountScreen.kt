package com.michaelrichards.collectivechronicles.screens.accountScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.components.BottomBar

@Composable
fun AccountScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold (
        modifier = modifier,
        bottomBar = { BottomBar(navController = navController, selectedValue = 2)}
    ){ paddingValues: PaddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {

        }

    }
}