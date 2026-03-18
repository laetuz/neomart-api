package id.neotica.data.dao.order

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

class OrderEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OrderEntity>(OrderTable)

    @OptIn(ExperimentalUuidApi::class)
    var userId by OrderTable.userId
    var totalAmount by OrderTable.totalAmount
    var status by OrderTable.status
    var createdAt by OrderTable.createdAt

    val items by OrderItemEntity.referrersOn(OrderItemTable.orderId)
}