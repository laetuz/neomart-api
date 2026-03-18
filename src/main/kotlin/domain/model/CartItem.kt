package id.neotica.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?,
    val subtotal: Double,
    @SerialName("created_at")
    val createdAt: Long? = null,
)
