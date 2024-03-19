package sahaj.ai

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest
class CityAndUserUpdaterTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testUpdateCityNameForPincode() {
        // Update city name for pincode 123123
        val pincode = 123699
        val updatedCityName = "LosSantos"

        val request: HttpRequest<Any> =
            HttpRequest.PUT("/cityData/update/$pincode", mapOf("name" to updatedCityName))

        client.toBlocking().exchange<Any, Any>(request)

        val response1: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/cityData/$pincode", Map::class.java)

        if (response1 != null) {
            assertEquals(200, response1.code())
        }

        val body1 = response1?.body()
        Assertions.assertNotNull(body1)
        assertEquals("LosSantos", body1!!["name"])
        assertEquals(123699, body1["pincode"])

        val id = 420
        val response2: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (response2 != null) {
            assertEquals(200, response2.code())
        }

        val body2 = response2?.body()
        Assertions.assertNotNull(body2)
        assertEquals(420, body2!!["id"])
        assertEquals("Quagmire", body2["name"])
        assertEquals("LosSantos", body2["city"])
        assertEquals(123699, body2["pincode"])

    }
}
