package id.neotica.data.dao.cart

import id.neotica.data.dao.product.ProductEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CartEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<CartEntity>(CartTable)

    @OptIn(ExperimentalUuidApi::class)
    var userId: Uuid by CartTable.userId
    var product: ProductEntity by ProductEntity referencedOn CartTable.productId
    var quantity: Int by CartTable.quantity
    var createdAt: Long by CartTable.createdAt
}