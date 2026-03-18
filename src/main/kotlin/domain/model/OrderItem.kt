package id.neotica.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: String? = null,
    @SerialName("product_id")
    val productId: String? = null,
    val quantity: Int,
    @SerialName("price_at_purchase")
    val priceAtPurchase: Double
)
