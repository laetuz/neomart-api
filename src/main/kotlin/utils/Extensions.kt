package id.neotica.utils

import com.auth0.jwt.JWT
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header

fun ApplicationCall.getUserIdFromToken(): String? {
    val authHeader = request.header("Authorization")?: return null
    val tokenString = authHeader.removePrefix("Bearer ").trim()

    return try {
        val decodedJwt = JWT.decode(tokenString)
        decodedJwt.getClaim("id").asString()
    } catch (e: Exception) {
        null
    }
}