package com.michaelrichards.collectivechronicles.screens.findStoriesScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.components.BottomBar

@Composable
fun FindStoriesScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold (
        bottomBar = {
            BottomBar(navController = navController, selectedValue = 0)
        }
    ){paddingValues ->  
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            
        }
    }
}