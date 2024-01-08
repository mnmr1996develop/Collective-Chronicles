package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class StoryPiece(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(length = 5000)
    val text: String = "",

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne
    @JoinColumn(name = "full_story_id")
    val fullStory: FullStory? = null

) {



}