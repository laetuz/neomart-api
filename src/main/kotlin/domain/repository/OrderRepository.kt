package id.neotica.domain.repository

import id.neotica.domain.model.Order

interface OrderRepository {
    suspend fun checkout(userId: String): Order?
}