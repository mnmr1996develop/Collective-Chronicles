package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.StoryPiece
import com.michaelRichards.collectiveChronicles.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoryPieceRepository: JpaRepository<StoryPiece, Long> {


    fun findByUserOrderByCreatedAtDesc(user: User): List<StoryPiece>
}