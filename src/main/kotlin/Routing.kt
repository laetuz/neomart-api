package id.neotica

import id.neotica.route.CartRoute
import id.neotica.route.OrderRoute
import id.neotica.route.ProductRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val productRoutes by inject<ProductRoute>()
    val cartRoutes by inject<CartRoute>()
    val orderRoutes by inject<OrderRoute>()

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        get { call.respond(HttpStatusCode.OK, "Neomart") }
        productRoutes.invoke(this)
        cartRoutes.invoke(this)
        orderRoutes.invoke(this)
    }
}
