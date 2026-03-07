package id.neotica.domain.repository.mapper

import id.neotica.data.dao.ProductEntity
import id.neotica.domain.model.Product

fun ProductEntity.toProduct(): Product = Product(
    id = this.id.value.toString(),
    name = this.name,
    price = this.price,
    stock = this.stock,
    description = this.description,
)