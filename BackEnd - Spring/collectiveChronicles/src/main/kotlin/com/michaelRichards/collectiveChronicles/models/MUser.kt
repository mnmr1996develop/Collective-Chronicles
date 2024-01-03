package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
class MUser(

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

    private var isAccountNonExpired: Boolean = true,

    private var isAccountNonLocked: Boolean = true,

    private var isCredentialsNonExpired: Boolean = true,

    private var isEnabled: Boolean = true,

    ) : UserDetails {


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(
        SimpleGrantedAuthority(role.name)
    )

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonExpired(): Boolean = this.isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = this.isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = this.isCredentialsNonExpired

    override fun isEnabled(): Boolean = this.isEnabled


}