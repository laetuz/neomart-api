package id.neotica.data.repository

import id.neotica.data.NeoDatabase
import id.neotica.data.dao.product.ProductEntity
import id.neotica.domain.model.Product
import id.neotica.domain.repository.ProductRepository
import id.neotica.domain.repository.mapper.toProduct
import id.neotica.utils.PaginatedResponse
import id.neotica.utils.PaginationParams
import id.neotica.utils.paginate
import java.util.UUID

class ProductRepositoryImpl(
    private val database: NeoDatabase,
): ProductRepository {
    override suspend fun getAllProducts(params: PaginationParams): PaginatedResponse<Product> = database.dbQuery {
        ProductEntity.all().paginate(params) {
            it.toProduct()
        }
    }

    override suspend fun getProductById(id: String): Product? = database.dbQuery {
        ProductEntity.findById(UUID.fromString(id))?.toProduct()
    }

    override suspend fun createProduct(product: Product): Product = database.dbQuery {
        ProductEntity.new {
            name = product.name
            price = product.price
            stock = product.stock
            description = product.description
            imageUrl = product.imageUrl
        }.toProduct()
    }

    override suspend fun updateProduct(id: String, product: Product): Product? = database.dbQuery {
        val entity = ProductEntity.findById(UUID.fromString(id))?: return@dbQuery null

        entity.name = product.name
        entity.price = product.price
        entity.description = product.description
        entity.imageUrl = product.imageUrl
        entity.stock = product.stock

        entity.toProduct()
    }

    override suspend fun deleteProduct(id: String): Product? = database.dbQuery {
        val entity = ProductEntity.findById(UUID.fromString(id))?: return@dbQuery null
        val tempt = entity.toProduct()
        entity.delete()

        tempt
    }
}