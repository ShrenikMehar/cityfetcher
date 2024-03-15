package sahaj.ai

import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class CityfetcherTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun testGetCityDataEndpoint() {
        val pincode = 123123
        val response: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/getCityData/$pincode", Map::class.java)

        if (response != null) {
            Assertions.assertEquals(200, response.code())
        }
        val body = response?.body()
        Assertions.assertNotNull(body)
        Assertions.assertEquals("Quahog", body!!["name"])
        Assertions.assertEquals(123123, body["pincode"])
    }

    @Test
    fun testGetUserDataEndpoint() {
        val id = 69
        val response: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/getUserData/$id", Map::class.java)

        if (response != null) {
            Assertions.assertEquals(200, response.code())
        }
        val body = response?.body()
        Assertions.assertNotNull(body)
        Assertions.assertEquals(69, body!!["id"])
        Assertions.assertEquals("Peter", body["name"])
        Assertions.assertEquals("Quahog", body["city"])
        Assertions.assertEquals(123123, body["pincode"])
    }
}
