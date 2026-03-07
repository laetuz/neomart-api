package id.neotica.data.repository

import id.neotica.data.NeoDatabase
import id.neotica.data.dao.cart.CartEntity
import id.neotica.data.dao.cart.CartTable
import id.neotica.data.dao.product.ProductEntity
import id.neotica.domain.model.CartItem
import id.neotica.domain.repository.CartRepository
import id.neotica.domain.repository.mapper.toCartItem
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CartRepositoryImpl(
    private val database: NeoDatabase,
): CartRepository {
    override suspend fun getCartForUser(userId: String): List<CartItem> = database.dbQuery {
        CartEntity.find { CartTable.userId.eq(Uuid.parse(userId)) }.map { it.toCartItem() }
    }

    override suspend fun addToCart(
        userId: String,
        productId: String,
        quantity: Int
    ): CartItem? = database.dbQuery {
        val userUuid = Uuid.parse(userId)
        val productUuid = UUID.fromString(productId)
        val product = ProductEntity.findById(productUuid) ?: return@dbQuery null

        val existingCartItem = CartEntity.find {
            CartTable.userId.eq(userUuid).and(CartTable.productId.eq(productUuid))
        }.firstOrNull()

        val savedEntity = if (existingCartItem != null) {
            existingCartItem.quantity += quantity
            existingCartItem
        } else {
            CartEntity.new {
                this.userId = userUuid
                this.product = product
                this.quantity = quantity
            }
        }

        savedEntity.toCartItem()
    }


    override suspend fun removeFromCart(
        cartItemId: String,
        userId: String
    ): CartItem? = database.dbQuery {
        val cartItem = CartEntity.findById(UUID.fromString(cartItemId)) ?: return@dbQuery null
        if (cartItem.userId.equals(UUID.fromString(userId))) {
            cartItem.delete()
            cartItem.toCartItem()
        } else {
            null
        }
    }
}