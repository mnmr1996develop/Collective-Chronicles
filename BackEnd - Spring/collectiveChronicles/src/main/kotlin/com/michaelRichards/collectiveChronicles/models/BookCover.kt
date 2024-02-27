package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*

@Entity
class BookCover(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Lob
    @Column(name = "image_data", length = 1000)
    var image: ByteArray = byteArrayOf(),

    val type: String = "",

    @OneToOne(
        mappedBy = "bookCover",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH],
        orphanRemoval = true
    )
    var fullStory: FullStory? = null
) {

}