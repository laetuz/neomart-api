package id.neotica.application

import id.neotica.di.neomartModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureFrameworks() {
    install(Koin) {
        modules(neomartModule)
    }
}
