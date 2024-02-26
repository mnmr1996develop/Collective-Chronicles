package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.dao.DuplicateKeyException
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


    @field:NotBlank
    @Column(name = "first_name")
    var firstName: String = "",

    @field:NotBlank
    @Column(name = "last_name")
    var lastName: String = "",

    @field:NotBlank
    @Column(unique = true)
    private var username: String = "",

    @field:Email
    @field:NotBlank
    @Column(unique = true)
    var email: String = "",

    @field:NotBlank
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



    @OneToMany(mappedBy = "storyOwner", orphanRemoval = true, cascade = [CascadeType.ALL])
    val ownedStories: MutableList<FullStory> = mutableListOf()


    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val storyRequests: MutableSet<StoryRequest> = mutableSetOf()


    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
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

    fun addOwnedStory(fullStory: FullStory){
        if(ownedStories.contains(fullStory))
            throw DuplicateKeyException("")
        ownedStories.add(fullStory)
    }

    fun removeOwnedStory(fullStory: FullStory) : Boolean = ownedStories.remove(fullStory)




    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (role != other.role) return false
        if (birthday != other.birthday) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + (birthday?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', username='$username', email='$email', password='$password', role=$role, birthday=$birthday)"
    }


}