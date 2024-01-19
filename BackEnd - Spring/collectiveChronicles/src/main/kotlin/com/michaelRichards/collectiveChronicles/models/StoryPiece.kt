package com.michaelRichards.collectiveChronicles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class StoryPiece(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(length = 5000)
    val narrative: String = "",

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "full_story_id")
    val fullStory: FullStory? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoryPiece

        if (id != other.id) return false
        if (narrative != other.narrative) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (user != other.user) return false
        if (fullStory != other.fullStory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + narrative.hashCode()
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (user?.hashCode() ?: 0)
        result = 31 * result + (fullStory?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StoryPiece(id=$id, narrative='$narrative', createdAt=$createdAt, updatedAt=$updatedAt)"
    }


}