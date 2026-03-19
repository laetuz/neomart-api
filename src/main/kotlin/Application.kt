package id.neotica

import id.neotica.application.configureFrameworks
import id.neotica.application.configureMonitoring
import id.neotica.application.configureRouting
import id.neotica.application.configureSecurity
import id.neotica.application.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureFrameworks()
    configureRouting()
}
