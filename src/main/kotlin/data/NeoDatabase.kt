package id.neotica.data

interface NeoDatabase {
    suspend fun <T> dbQuery(block: () -> T): T
}