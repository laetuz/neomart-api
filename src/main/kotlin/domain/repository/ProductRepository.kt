package id.neotica.domain.repository

import id.neotica.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(id: String): Product?
    suspend fun createProduct(product: Product): Product
    suspend fun updateProduct(id: String, product: Product): Product?
    suspend fun deleteProduct(id: String): Product?
}