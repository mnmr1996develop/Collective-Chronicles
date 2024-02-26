package com.michaelrichards.collectivechronicles.screens.startStoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelrichards.collectivechronicles.dtos.requests.StoryRequest
import com.michaelrichards.collectivechronicles.repositories.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun startStory(storyRequest: StoryRequest){
        viewModelScope.launch {
            storyRepository.startStory(storyRequest = storyRequest)
        }
    }

}