package com.michaelRichards.collectiveChronicles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.jpa.domain.AbstractPersistable_.id
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

    val acceptedAt: LocalDateTime,

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "full_story_id")
    val fullStory: FullStory? = null
) {



    override fun toString(): String {
        return "StoryPiece(id=$id, narrative='$narrative', createdAt=$createdAt, updatedAt=$updatedAt)"
    }


}