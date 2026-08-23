package org.groomi.groomidevbackend.auth.token_generator

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType
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

    fun generateToken(user: UserProfile, tokenType: TokenType): String? {
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("email", user.getEmail())
            .claim("type", tokenType.name)
            .issuedAt(Date())
            .expiration(
                Date(System.currentTimeMillis() + 86400000)
            )
            .signWith(this.signingKey)
            .compact()
    }
    fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
    fun isTokenType(token: String, expectedType: TokenType): Boolean {
        val claims = extractAllClaims(token)
        return claims["type"] == expectedType.name
    }
    fun extractUserId(token: String): UUID {
        return UUID.fromString(
            extractAllClaims(token).subject
        )
    }
    private val signingKey: SecretKey
        get() = Keys.hmacShaKeyFor(secret?.toByteArray())
}