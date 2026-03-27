package id.neotica.domain.repository

import id.neotica.domain.model.Product
import id.neotica.utils.PaginatedResponse
import id.neotica.utils.PaginationParams

interface ProductRepository {
    suspend fun getAllProducts(params: PaginationParams): PaginatedResponse<Product>
    suspend fun getProductById(id: String): Product?
    suspend fun createProduct(product: Product): Product
    suspend fun updateProduct(id: String, product: Product): Product?
    suspend fun deleteProduct(id: String): Product?
}