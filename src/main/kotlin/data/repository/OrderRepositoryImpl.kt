package id.neotica.data.repository

import id.neotica.data.NeoDatabase
import id.neotica.data.dao.cart.CartEntity
import id.neotica.data.dao.cart.CartTable
import id.neotica.data.dao.order.OrderEntity
import id.neotica.data.dao.order.OrderItemEntity
import id.neotica.data.dao.product.ProductEntity
import id.neotica.domain.model.Order
import id.neotica.domain.repository.OrderRepository
import id.neotica.domain.repository.mapper.toOrder
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalUuidApi::class)
class OrderRepositoryImpl(
    private val database: NeoDatabase
): OrderRepository {

    override suspend fun checkout(userId: String): Order? = database.dbQuery {
        val userUuid = UUID.fromString(userId)
        val cartItems = CartEntity.find { CartTable.userId.eq(userUuid.toKotlinUuid()) }.toList()

        if (cartItems.isEmpty()) return@dbQuery null

        var calculatedTotal = 0.0

        cartItems.forEach {
            val product = ProductEntity.findById(it.product.id) ?: throw Exception("Checkout failed: Product ${it.product.id} no longer exist.")

            if (product.stock < it.quantity) {
                throw Exception("Checkout failed: Insufficient stock for ${it.product.name}")
            }

            calculatedTotal += (product.price * it.quantity)
        }

        val newOrder = OrderEntity.new {
            this.userId = userUuid.toKotlinUuid()
            this.totalAmount = calculatedTotal
            this.status = "COMPLETED"
        }

        cartItems.forEach {
            val product = ProductEntity.findById(it.product.id)

            if (product != null) {
                OrderItemEntity.new {
                    this.order = newOrder
                    this.productId = product.id
                    this.quantity = it.quantity
                    this.priceAtPurchase = product.price
                }

                product.stock -= it.quantity
                it.delete()
            } else throw Exception("Checkout failed: Product ${it.product.id} no longer exist.")
        }

        newOrder.toOrder()
    }
}