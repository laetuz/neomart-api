package id.neotica.domain.repository.mapper

import id.neotica.data.dao.order.OrderEntity
import id.neotica.data.dao.order.OrderItemEntity
import id.neotica.domain.model.Order
import id.neotica.domain.model.OrderItem
import kotlin.uuid.ExperimentalUuidApi

fun OrderItemEntity.toOrderItem(): OrderItem  = OrderItem(
    id = this.id.value.toString(),
    productId = this.productId.toString(),
    quantity = this.quantity,
    priceAtPurchase = this.priceAtPurchase
)

@OptIn(ExperimentalUuidApi::class)
fun OrderEntity.toOrder(): Order = Order(
    id = this.id.value.toString(),
    userId = this.userId.toString(),
    totalAmount = this.totalAmount,
    status = this.status,
    createdAt = this.createdAt,
    items = this.items.map { it.toOrderItem() }
)