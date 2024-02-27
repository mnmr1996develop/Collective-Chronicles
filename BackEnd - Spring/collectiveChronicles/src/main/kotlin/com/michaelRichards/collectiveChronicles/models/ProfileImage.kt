package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*

@Entity
class ProfileImage(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Lob
    @Column(name = "image_data", length = 1000)
    var image: ByteArray = byteArrayOf(),

    var type: String = "",

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
) {



}