package com.michaelrichards.collectivechronicles.screens.storyScreen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.navigation.Graphs
import com.michaelrichards.collectivechronicles.repositories.results.ApiSuccessFailState


@Composable
fun StoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    storyId: Long,
    viewModel: StoryScreenViewModel = hiltViewModel()
) {


    LaunchedEffect(true) {
        viewModel.getStoryData(storyId)
    }

    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.storyDetails.collect{
            when(it){
                is ApiSuccessFailState.BadRequest -> {

                }
                is ApiSuccessFailState.Loading -> {
                    Toast.makeText(context, "", Toast.LENGTH_LONG).show()
                }
                is ApiSuccessFailState.Success -> {

                }

                is ApiSuccessFailState.TimeOut -> {

                }

                is ApiSuccessFailState.UnAuthorized -> {
                    Toast.makeText(context, "Unauthorized Access, please log back in", Toast.LENGTH_LONG ).show()
                    navController.navigate(Graphs.AuthGraph.graphName){
                        popUpTo(Graphs.MainGraph.graphName){
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Surface(
            modifier = modifier.padding(paddingValues = paddingValues)
        ) {

        }
    }


}
