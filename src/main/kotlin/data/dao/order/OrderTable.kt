package id.neotica.data.dao.order

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import kotlin.uuid.ExperimentalUuidApi

object OrderTable: UUIDTable("orders") {
    @OptIn(ExperimentalUuidApi::class)
    val userId = uuid("user_id")
    val totalAmount = double("total_amount")
    val status = varchar("status", 50)
    val createdAt = long("created_at").clientDefault { System.currentTimeMillis() }
}