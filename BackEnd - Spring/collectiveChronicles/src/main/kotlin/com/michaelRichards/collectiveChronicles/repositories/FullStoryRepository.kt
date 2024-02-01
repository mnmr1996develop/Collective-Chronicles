package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.FullStory
import com.michaelRichards.collectiveChronicles.models.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FullStoryRepository: JpaRepository<FullStory, Long> {


    fun findByStoryOwnerOrderByLastEditedDesc(storyOwner: User): MutableList<FullStory>



    fun findByStoryOwner(storyOwner: User, pageable: Pageable): List<FullStory>
}