package com.michaelrichards.collectivechronicles.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.components.MyStoriesCard
import com.michaelrichards.collectivechronicles.components.BottomBar
import com.michaelrichards.collectivechronicles.components.ContributedStoryCard
import com.michaelrichards.collectivechronicles.dtos.responses.OwnerStoryResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryPieceResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import com.michaelrichards.collectivechronicles.navigation.Screens
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {


    val myStoryList = mutableListOf(
        OwnerStoryResponse(
            StoryResponse(
                title = "Yo this is a long title That goes on forever who would even make a title this long",
                canon = mutableListOf(
                    StoryPieceResponse(
                        1,
                        "",
                        LocalDateTime.now(),
                        lastUpdatedAt = LocalDateTime.now(),
                        isCallerOwner = true,
                        owner = "Vincent75fan"
                    )
                ),
                isCallerOwner = true,
                isStoryOpen = true,
                storyEdited = LocalDateTime.now(),
                storyId = 2,
                storyOwner = "Vincent75fan",
                storyStarted = LocalDateTime.now(),
                topic = "Space Lasers from out of space come",
                maximumCanonSize = 20
            ),
            potentialStoryPieces = mutableListOf(
                StoryPieceResponse(
                    1,
                    "",
                    LocalDateTime.now(),
                    lastUpdatedAt = LocalDateTime.now(),
                    isCallerOwner = true,
                    owner = "Vincent75fan"
                )
            )
        )
    )

    val storiesContributedTo = mutableListOf<StoryResponse>(
        StoryResponse(
            title = "title",
            canon = mutableListOf(
                StoryPieceResponse(
                    1,
                    "",
                    LocalDateTime.now(),
                    lastUpdatedAt = LocalDateTime.now(),
                    isCallerOwner = true,
                    owner = "Vincent75fan"
                )
            ),
            isCallerOwner = true,
            isStoryOpen = true,
            storyEdited = LocalDateTime.now(),
            storyId = 2,
            storyOwner = "Vincent75fan",
            storyStarted = LocalDateTime.now(),
            topic = "Space Lasers from out of space come",
            maximumCanonSize = 5

        )
    )

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            BottomBar(navController = navController, selectedValue = 1)
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(8.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Stories Created", style = MaterialTheme.typography.headlineMedium)
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (myStoryList.isEmpty()) {
                        items(5) {
                            MyStoriesCard {
                                navController.navigate(Screens.StartStoryScreen.routeName)
                            }
                        }
                    } else {
                        item {
                            MyStoriesCard {
                                navController.navigate(Screens.StartStoryScreen.routeName)
                            }
                        }
                        items(myStoryList) {
                            MyStoriesCard(ownerStoryResponse = it) {
                                navController.navigate("${Screens.StoryScreen.routeName}/${it.storyResponse.storyId}")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ContributedStoryCard {
                    navController.navigate(Screens.FindStoriesScreen.routeName)
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (storiesContributedTo.isEmpty()) {
                    Text(
                        text = "Find Stories To Contribute To",
                        style = MaterialTheme.typography.headlineMedium
                    )

                } else {
                    LazyColumn {
                        items(storiesContributedTo) {
                            ContributedStoryCard(storyResponse = it) {

                            }
                        }
                    }
                }

            }

        }

    }

}



@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = NavController(LocalContext.current))
}