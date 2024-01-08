package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "_user")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "first_name")
    var firstName: String = "",

    @Column(name = "last_name")
    var lastName: String = "",

    @Column(unique = true)
    private var username: String = "",

    @Column(unique = true)
    private var email: String = "",

    @Column(name = "password")
    private var password: String = "",

    @Enumerated(value = EnumType.STRING)
    var role: Role = Role.ROLE_USER,

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    val tokens: MutableSet<Token> = mutableSetOf(),

    var accountCreatedAt: LocalDateTime? = null,

    var birthday: LocalDate? = null,

    private var isAccountNonExpired: Boolean = true,

    private var isAccountNonLocked: Boolean = true,

    private var isCredentialsNonExpired: Boolean = true,

    private var isEnabled: Boolean = true,

    ) : UserDetails {



    @OneToMany(mappedBy = "storyOwner", orphanRemoval = true)
    val ownedStories: MutableSet<FullStory> = mutableSetOf()

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    val storyPieces: MutableSet<StoryPiece> = mutableSetOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(
        SimpleGrantedAuthority(role.name)
    )

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username
    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean = this.isAccountNonExpired
    fun setIsAccountNonExpired(isAccountNonExpired: Boolean) {
        this.isAccountNonExpired = isAccountNonExpired
    }

    override fun isAccountNonLocked(): Boolean = this.isAccountNonLocked
    fun setIsAccountNonLocked(isAccountNonLocked: Boolean) {
        this.isAccountNonLocked = isAccountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean = this.isCredentialsNonExpired

    override fun isEnabled(): Boolean = this.isEnabled


}