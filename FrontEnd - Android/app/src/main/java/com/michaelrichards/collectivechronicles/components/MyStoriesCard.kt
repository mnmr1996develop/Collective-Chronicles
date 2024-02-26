package com.michaelrichards.collectivechronicles.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michaelrichards.collectivechronicles.dtos.responses.OwnerStoryResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryPieceResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import java.time.LocalDateTime

@Composable
fun MyStoriesCard(
    modifier: Modifier = Modifier,
    ownerStoryResponse: OwnerStoryResponse? = null,
    onClick: () -> Unit
) {
    val gradient = Brush.linearGradient(
        0.0f to Color.Magenta,
        500.0f to Color.Cyan,
        start = Offset.Zero,
        end = Offset.Infinite
    )


    ownerStoryResponse?.let { response ->

        val storyResponse = response.storyResponse

        Card(
            modifier = modifier
                .height(175.dp)
                .width(245.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = storyResponse.title, style = MaterialTheme.typography.bodyLarge, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Row (horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Topic:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = storyResponse.title , style = MaterialTheme.typography.bodySmall)
                }
                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Story Status:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (storyResponse.isCallerOwner) "Public" else "Private", style = MaterialTheme.typography.labelSmall)
                }

                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Story Status:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (storyResponse.isCallerOwner) "Public" else "Private", style = MaterialTheme.typography.labelSmall)
                }
                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Story Requests")
                }
            }
        }

    } ?: Card(
        modifier = modifier
            .height(175.dp)
            .width(245.dp)
            .clickable { onClick() }
    ) {
         Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.clickable { onClick() },
                imageVector = Icons.Outlined.Add,
                contentDescription = "Create Story"
            )
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

@Preview
@Composable
private fun PrevMyStoriesCardBlank() {
    MyStoriesCard {

    }
}
private val ownerStoryResponse = OwnerStoryResponse(
    StoryResponse(
        title = "Yo this is a long ass title That",
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

@Preview
@Composable
private fun PrevMyStoriesCard() {
    MyStoriesCard(
        ownerStoryResponse = ownerStoryResponse
    ) {

    }
}