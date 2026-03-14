package id.neotica.route

import id.neotica.domain.model.Product
import id.neotica.domain.repository.ProductRepository
import id.neotica.utils.Constants.AUTH_JWT
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
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
        }
        route.authenticate(AUTH_JWT) {
            route("/admin/products") {
                post {
                    val product = call.receive<Product>()
                    val newProduct = repository.createProduct(product)
                    call.respond(HttpStatusCode.Created, newProduct)
                }
                put("/{id}") {
                    val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val product = call.receive<Product>()

                    val updatedProduct = repository.updateProduct(id, product)

                    if (updatedProduct != null) {
                        call.respond(HttpStatusCode.OK, updatedProduct)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Product not found")
                    }
                }
                delete("/{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    val product = repository.deleteProduct(id)

                    if (product != null) {
                        call.respond(HttpStatusCode.OK, "${product.name} has been successfully deleted.")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Product not found")
                    }
                }
            }
        }
    }
}