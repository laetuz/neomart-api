package id.neotica.di

import id.neotica.data.DatabaseImpl
import id.neotica.data.NeoDatabase
import id.neotica.data.repository.CartRepositoryImpl
import id.neotica.data.repository.ProductRepositoryImpl
import id.neotica.domain.repository.CartRepository
import id.neotica.domain.repository.ProductRepository
import id.neotica.route.CartRoute
import id.neotica.route.ProductRoute
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val neomartModule = module {
    single<NeoDatabase> { DatabaseImpl() }

    singleOf(::ProductRepositoryImpl).bind(ProductRepository::class)
    singleOf(::CartRepositoryImpl).bind(CartRepository::class)

    singleOf(::ProductRoute)
    singleOf(::CartRoute)
}