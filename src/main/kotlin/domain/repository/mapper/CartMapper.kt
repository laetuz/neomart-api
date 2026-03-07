package id.neotica.domain.repository.mapper

import id.neotica.data.dao.cart.CartEntity
import id.neotica.domain.model.CartItem

fun CartEntity.toCartItem(): CartItem = CartItem(
    id = this.id.value.toString(),
    productId = this.product.id.toString(),
    productName = this.product.name,
    price = this.product.price,
    quantity = this.quantity,
    imageUrl = this.product.imageUrl,
    subtotal = this.product.price * this.quantity,
)