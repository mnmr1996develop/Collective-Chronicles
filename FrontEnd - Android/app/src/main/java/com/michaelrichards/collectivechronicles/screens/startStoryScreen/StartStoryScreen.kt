package com.michaelrichards.collectivechronicles.screens.startStoryScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.sharp.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.R
import com.michaelrichards.collectivechronicles.dtos.requests.StoryRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartStoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StartStoryViewModel = hiltViewModel()
) {

    var title by rememberSaveable {
        mutableStateOf("")
    }
    var topic by rememberSaveable {
        mutableStateOf("")
    }

    var private by remember {
        mutableStateOf(true)
    }

    val enabled = remember(title, topic) {
        title.isNotBlank() && topic.isNotBlank()
    }

    val privateRowScrollState = rememberScrollState()

    val screenScrollState = rememberScrollState()


    Scaffold(

        topBar = {
            TopAppBar(title = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.ArrowBackIosNew,
                            contentDescription = stringResource(id = R.string.go_back),
                            modifier = Modifier
                                .clickable { navController.popBackStack() })
                    }

                    Text(
                        text = stringResource(id = R.string.create_story),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.weight(4f)
                    )

                }
            })
        },
        modifier = modifier.padding(5.dp)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .scrollable(screenScrollState, orientation = Orientation.Vertical),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.story_title),
                        )
                    })
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = stringResource(id = R.string.plot_summary),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    minLines = 10,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .scrollable(privateRowScrollState, orientation = Orientation.Horizontal)
                        .width(900.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.private_story))
                        Spacer(modifier = Modifier.width(15.dp))
                        Switch(
                            checked = private,
                            onCheckedChange = { private = it },
                            thumbContent = {
                                Icon(
                                    imageVector = if (private) Icons.Filled.Check else Icons.Outlined.Cancel,
                                    contentDescription = null
                                )

                            },
                            colors = SwitchDefaults.colors(
                                checkedIconColor = Color.Green,
                                uncheckedIconColor = Color.Red
                            )
                        )
                    }

                    Text(text = stringResource(id = if (private) R.string.private_ else R.string.public_))
                }

                Spacer(modifier = Modifier.height(15.dp))



                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = {
                    startStory(
                        title = title,
                        topic = topic,
                        maxStoryPieces = 20,
                        private,
                        viewModel = viewModel
                    )
                }, enabled = enabled) {
                    Text(text = stringResource(id = R.string.start_story))
                }
            }

        }

    }
}

fun startStory(
    title: String,
    topic: String,
    maxStoryPieces: Int,
    isPrivate: Boolean,
    viewModel: StartStoryViewModel
) {
    val storyRequest = StoryRequest(
        title = title.trim(),
        topic = topic.trim(),
        isStoryOpen = isPrivate,
        maxCanonPieces = minOf(100, maxStoryPieces),
    )
    viewModel.startStory(
        storyRequest = storyRequest
    )
}