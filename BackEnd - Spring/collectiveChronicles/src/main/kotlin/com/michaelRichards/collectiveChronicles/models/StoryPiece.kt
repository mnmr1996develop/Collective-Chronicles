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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoryPiece

        if (id != other.id) return false
        if (text != other.text) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (user != other.user) return false
        if (fullStory != other.fullStory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + text.hashCode()
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (user?.hashCode() ?: 0)
        result = 31 * result + (fullStory?.hashCode() ?: 0)
        return result
    }
}