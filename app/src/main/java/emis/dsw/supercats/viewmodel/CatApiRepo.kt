package emis.dsw.supercats.viewmodel

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class CatApiRepo() {

    suspend fun getRndCatsCall(): HttpResponse {
        val client = HttpClient(CIO)
        val response: HttpResponse =
            client.get("https://api.thecatapi.com/v1/images/search?limit=20")
        client.close()
        return response
    }
}