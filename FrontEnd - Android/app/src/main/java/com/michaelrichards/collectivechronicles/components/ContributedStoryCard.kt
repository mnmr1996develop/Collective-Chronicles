package com.michaelrichards.collectivechronicles.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.copy
import com.michaelrichards.collectivechronicles.dtos.responses.OwnerStoryResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryPieceResponse
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import java.time.LocalDateTime

@Composable
fun ContributedStoryCard(
    modifier: Modifier = Modifier,
    storyResponse: StoryResponse? = null,
    onClick: () -> Unit
) {
    val gradient = Brush.radialGradient(
        0.0f to Color.Magenta,
        500.0f to Color.Cyan,
        radius = 1200f,
        tileMode = TileMode.Repeated
    )

    storyResponse?.let {
        Surface(
            modifier = modifier
                .height(125.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = storyResponse.title, style = MaterialTheme.typography.bodyLarge, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Row {
                    Text(text = "Topic:", style = MaterialTheme.typography.bodyMedium, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = storyResponse.topic)
                }
                Row {
                    Text(text = "Current Pieces:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = storyResponse.canon.size.toString())
                }
                Row {
                    Text(text = "Maximum Pieces:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = storyResponse.maximumCanonSize.toString())
                }
            }
        }
    } ?: Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
            .height(100.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Outlined.Search,
            contentDescription = null
        )
    }
}

private val storyResponse = StoryResponse(
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
)

@Preview
@Composable
private fun PrevBlankContributedStoriesCard() {
    ContributedStoryCard {

    }
}

@Preview
@Composable
private fun PrevContributedStoriesCard() {
    ContributedStoryCard (
        storyResponse = storyResponse
    ){

    }
}