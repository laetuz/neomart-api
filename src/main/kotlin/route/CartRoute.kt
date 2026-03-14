package id.neotica.route

import id.neotica.domain.repository.CartRepository
import id.neotica.utils.Constants.AUTH_JWT
import id.neotica.utils.getUserIdFromToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

class CartRoute(private val repository: CartRepository) {
    operator fun invoke(route: Route) {
        route.authenticate(AUTH_JWT) {
            route.route("/cart") {
                get {
                    val userId = call.getUserIdFromToken()
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, "Missing or invalid User ID")
                    val cart = repository.getCartForUser(userId)

                    call.respond(HttpStatusCode.OK, cart)
                }
                post {
                    val userId = call.getUserIdFromToken()
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing or invalid User ID")
                    val request = call.receive<AddToCartRequest>()

                    val cartItem = repository.addToCart(userId, productId = request.productId, quantity = request.quantity)
                    if (cartItem != null) { call.respond(HttpStatusCode.OK, cartItem) }
                        else call.respond(HttpStatusCode.BadRequest)
                }
                delete("/{productId}") {
                    val userId = call.getUserIdFromToken()
                        ?: return@delete call.respond(HttpStatusCode.Unauthorized, "Missing or invalid User ID")
                    val cartItemId = call.parameters["productId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    val success = repository.removeFromCart(cartItemId, userId)

                    if (success != null) {
                        call.respond(HttpStatusCode.OK, success)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}

@Serializable
private data class AddToCartRequest(val productId: String, val quantity: Int)