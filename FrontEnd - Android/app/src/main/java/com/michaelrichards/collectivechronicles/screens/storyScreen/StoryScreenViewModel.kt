package com.michaelrichards.collectivechronicles.screens.storyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import com.michaelrichards.collectivechronicles.repositories.results.ApiSuccessFailState
import com.michaelrichards.collectivechronicles.repositories.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryScreenViewModel @Inject constructor(
    private val storyRepository: StoryRepository
): ViewModel() {

    private val storyDetailsChannel: Channel<ApiSuccessFailState<StoryResponse>> = Channel()
    val storyDetails = storyDetailsChannel.receiveAsFlow()

    fun getStoryData(storyId: Long) = viewModelScope.launch {
        storyDetailsChannel.send(ApiSuccessFailState.Loading())
        val result = storyRepository.getStoryData(storyId)
        storyDetailsChannel.send(result)
    }

}