package id.neotica.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,
    val name: String,
    val price: Double,
    val stock: Int,
    val description: String? = null
)