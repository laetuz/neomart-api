package id.neotica.data.dao.cart

import id.neotica.data.dao.product.ProductTable
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import kotlin.uuid.ExperimentalUuidApi

object CartTable: UUIDTable(name = "carts") {
    @OptIn(ExperimentalUuidApi::class)
    val userId = uuid("user_id")
    val productId = reference("product_id", ProductTable, onDelete = ReferenceOption.CASCADE)
    val quantity = integer("quantity").default(1)
}