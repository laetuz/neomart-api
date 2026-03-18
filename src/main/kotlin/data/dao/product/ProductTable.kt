package id.neotica.data.dao.product

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object ProductTable: UUIDTable("products") {
    val name = varchar("name", 255)
    val price = double("price")
    val stock = integer("stock")
    val description = text("description").nullable()
    val imageUrl = varchar("image_url", 255).nullable()
    val createdAt = long("created_at").clientDefault { System.currentTimeMillis() }
}