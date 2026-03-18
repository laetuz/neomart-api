package id.neotica.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("total_amount")
    val totalAmount: Double,
    val status: String,
    @SerialName("created_at")
    val createdAt: Long? = null,
    val items: List<OrderItem> = emptyList()
)
