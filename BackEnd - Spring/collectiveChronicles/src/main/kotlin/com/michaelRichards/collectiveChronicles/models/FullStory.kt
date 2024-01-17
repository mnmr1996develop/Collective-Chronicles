package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity

class FullStory(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    val title: String = "",

    var topic: String = "",

    var isStoryOpen: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "story_owner_id")
    val storyOwner: User? = null,

    val storyStarted: LocalDateTime? = null,

    var storyLastEdited: LocalDateTime? = null

) {


    @OneToMany(mappedBy = "fullStory", cascade = [CascadeType.REMOVE, CascadeType.REFRESH], orphanRemoval = true)
    val potentialPieces: MutableSet<StoryPiece> = mutableSetOf()

    fun addPotentialPiece(storyPiece: StoryPiece){
        potentialPieces.add(storyPiece)
    }


    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "full_story_id")
    val canon: MutableList<StoryPiece> = mutableListOf()

    fun addToCanon(storyPiece: StoryPiece){
        if (potentialPieces.contains(storyPiece)){
            potentialPieces.remove(storyPiece)
            canon.add(storyPiece)
        }

    }

}