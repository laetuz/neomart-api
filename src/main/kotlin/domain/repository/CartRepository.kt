package id.neotica.domain.repository

import id.neotica.domain.model.CartItem

interface CartRepository {
    suspend fun getCartForUser(userId: String): List<CartItem>
    suspend fun addToCart(userId: String, productId: String, quantity: Int): CartItem?
    suspend fun removeFromCart(cartItemId: String, userId: String): CartItem?
}