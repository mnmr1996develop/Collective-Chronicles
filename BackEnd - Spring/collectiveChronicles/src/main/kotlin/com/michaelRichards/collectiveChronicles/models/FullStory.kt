package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*

@Entity

class FullStory(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    var topic: String = "",

    var isStoryOpen: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "story_owner_id")
    val storyOwner: User? = null




) {


    @OneToMany(mappedBy = "fullStory", cascade = [CascadeType.REMOVE, CascadeType.REFRESH], orphanRemoval = true)
    val potentialPieces: MutableSet<StoryPiece> = mutableSetOf()


    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "full_story_id")
    val canon: MutableList<StoryPiece> = mutableListOf()

}