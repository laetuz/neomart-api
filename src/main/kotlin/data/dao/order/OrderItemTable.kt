package id.neotica.data.dao.order

import id.neotica.data.dao.product.ProductTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object OrderItemTable: UUIDTable("order_items") {
    val orderId = reference("order_id", OrderTable, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", ProductTable)
    val quantity = integer("quantity")
    val priceAtPurchase = double("price_at_purchase")
}