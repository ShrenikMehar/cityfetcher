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

        val request1: HttpRequest<Any> =
            HttpRequest.PUT("/cityData/update/$pincode", mapOf("name" to updatedCityName))

        client.toBlocking().exchange<Any, Any>(request1)

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


        // Revert back to original city name
        val originalCityName = "LA"

        val request2: HttpRequest<Any> =
            HttpRequest.PUT("/cityData/update/$pincode", mapOf("name" to originalCityName))

        client.toBlocking().exchange<Any, Any>(request2)

        val response3: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (response3 != null) {
            assertEquals(200, response3.code())
        }

        val body3 = response3?.body()
        Assertions.assertNotNull(body3)
        assertEquals(420, body3!!["id"])
        assertEquals("Quagmire", body3["name"])
        assertEquals("LA", body3["city"])
        assertEquals(123699, body3["pincode"])


    }
}
