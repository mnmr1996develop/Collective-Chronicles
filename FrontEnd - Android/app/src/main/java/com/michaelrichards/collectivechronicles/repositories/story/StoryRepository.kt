package com.michaelrichards.collectivechronicles.repositories.story

import com.michaelrichards.collectivechronicles.dtos.requests.StoryPieceRequest
import com.michaelrichards.collectivechronicles.dtos.requests.StoryRequest
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import com.michaelrichards.collectivechronicles.repositories.results.ApiSuccessFailState

interface StoryRepository {

    suspend fun startStory(storyRequest: StoryRequest): ApiSuccessFailState<StoryResponse>

    suspend fun getStoryData(storyId: Long) : ApiSuccessFailState<StoryResponse>

    suspend fun sendStoryRequest(storyId: Long, storyPieceRequest: StoryPieceRequest): ApiSuccessFailState<Unit>
}