package id.neotica.data.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import java.util.UUID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass

class ProductEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<ProductEntity>(ProductTable)

    var name: String by ProductTable.name
    var price: Double by ProductTable.price
    var stock: Int by ProductTable.stock
    var description: String? by ProductTable.description
    var imageUrl: String? by ProductTable.imageUrl
}