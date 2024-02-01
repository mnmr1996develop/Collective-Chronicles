package com.michaelRichards.collectiveChronicles.models

import com.michaelRichards.collectiveChronicles.exceptions.storyExceptions.StoryExceptions
import jakarta.persistence.*
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

@Entity
class FullStory(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Length(min = 3, max = 100)
    val title: String = "",

    @Length(min = 3, max = 100)
    var topic: String = "",

    var isStoryOpen: Boolean = true,

    var maxStories: Int? = 20,

    @ManyToOne
    @JoinColumn(name = "story_owner_id")
    val storyOwner: User? = null,

    val created: LocalDateTime? = null,

    var lastEdited: LocalDateTime? = null

) {

    @OneToMany(mappedBy = "fullStory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val canon: MutableList<StoryPiece> = mutableListOf()

    @OneToMany(mappedBy = "fullStory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val potentialPieces: MutableList<StoryPiece> = mutableListOf()


    fun addPotentialPiece(storyPiece: StoryPiece){
        potentialPieces.add(storyPiece)
    }




    fun addToCanon(storyPiece: StoryPiece){
        if (potentialPieces.contains(storyPiece)){
            potentialPieces.remove(storyPiece)
        }
        maxStories?.let { max ->
            if (canon.size < max){
                canon.add(storyPiece)
            }
            else throw StoryExceptions.MaxStoriesReached(storiesInCanon = max, this.title)
        } ?: kotlin.run {
            canon.add(storyPiece)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullStory

        if (id != other.id) return false
        if (title != other.title) return false
        if (topic != other.topic) return false
        if (isStoryOpen != other.isStoryOpen) return false
        if (storyOwner != other.storyOwner) return false
        if (created != other.created) return false
        if (lastEdited != other.lastEdited) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + topic.hashCode()
        result = 31 * result + isStoryOpen.hashCode()
        result = 31 * result + (storyOwner?.hashCode() ?: 0)
        result = 31 * result + (created?.hashCode() ?: 0)
        result = 31 * result + (lastEdited?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "FullStory(id=$id, title='$title', topic='$topic', isStoryOpen=$isStoryOpen, storyOwner=$storyOwner, storyStarted=$created, storyLastEdited=$lastEdited)"
    }



}