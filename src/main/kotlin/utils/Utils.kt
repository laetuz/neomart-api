package id.neotica.utils

import java.io.File

class Utils {
    object EnvLoader {
        private val envData = mutableMapOf<String, String>()

        init {
            val envFile = File(".env")
            if (envFile.exists()) {
                envFile.forEachLine { line ->
                    // Basic parsing: ignores comments (#) and empty lines
                    if (line.isNotBlank() && !line.trim().startsWith("#")) {
                        val parts = line.split("=", limit = 2)
                        if (parts.size == 2) {
                            envData[parts[0].trim()] = parts[1].trim()
                        }
                    }
                }
            } else {
                println("No env file provided \uD83D\uDE1B")
            }
        }

        operator fun get(key: String): String? = envData[key] ?: System.getenv(key)
    }
}