package id.neotica.route

import id.neotica.domain.repository.OrderRepository
import id.neotica.utils.Constants.AUTH_JWT
import id.neotica.utils.getUserIdFromToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class OrderRoute(private val repository: OrderRepository) {
    operator fun invoke(route: Route) {
        route.authenticate(AUTH_JWT) {
            route("/checkout") {
                post {
                    try {
                        val userId = call.getUserIdFromToken() ?: return@post call.respond(HttpStatusCode.BadRequest)

                        val order = repository.checkout(userId)?: return@post call.respond(HttpStatusCode.BadRequest)

                        call.respond(HttpStatusCode.Created, order)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, e.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}