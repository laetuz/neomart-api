package id.neotica.route

import id.neotica.domain.model.Product
import id.neotica.domain.repository.ProductRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class ProductRoute(private val repository: ProductRepository) {
    operator fun invoke(route: Route) {
        route.route("/products") {
            get {
                val products = repository.getAllProducts()
                call.respond(HttpStatusCode.OK, products)
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

                val product = repository.getProductById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(product)
            }

            route.authenticate("auth-jwt") {
                route.route("/admin/product") {
                    post {
                        val product = call.receive<Product>()
                        val newProduct = repository.createProduct(product)
                        call.respond(HttpStatusCode.Created, newProduct)
                    }
                }
            }
        }
    }
}