package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.FullStory
import com.michaelRichards.collectiveChronicles.models.StoryRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoryRequestRepository: JpaRepository<StoryRequest, Long> {


    fun findByFullStory(fullStory: FullStory): List<StoryRequest>


    fun findByStoryPieceIdAndFullStory(storyPieceId: Long, fullStory: FullStory): StoryRequest?
}