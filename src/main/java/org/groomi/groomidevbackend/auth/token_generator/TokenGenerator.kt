package org.groomi.groomidevbackend.auth.token_generator

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException
import org.groomi.groomidevbackend.user.UserProfile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Supplier
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value($$"${jwt.secret}")
    private val secret: String? = null

    fun generateToken(user: UserProfile): String? {
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("email", user.getEmail())
            .issuedAt(Date())
            .expiration(
                Date(System.currentTimeMillis() + 86400000)
            )
            .signWith(this.signingKey)
            .compact()
    }
    private val signingKey: SecretKey
        get() = Keys.hmacShaKeyFor(secret?.toByteArray())
}