package id.neotica.data.dao.order

import id.neotica.data.dao.product.ProductTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

class OrderItemEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<OrderItemEntity>(OrderItemTable)

    val order by OrderEntity referencedOn OrderItemTable.orderId
    var productId by OrderItemTable.productId
    var quantity by OrderItemTable.quantity
    var priceAtPurchase by OrderItemTable.priceAtPurchase
}