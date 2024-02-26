package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime

@Entity
class StoryRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    val createdAt: LocalDateTime? = null,

    val editedAt: LocalDateTime? = null,

    val storyPieceId: Long? = null,

    val narrative: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH])
    @JoinColumn(name = "full_story_id")
    val fullStory: FullStory? = null

) {



}