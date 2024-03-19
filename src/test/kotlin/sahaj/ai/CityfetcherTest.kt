package sahaj.ai

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
            .exchange("/cityData/$pincode", Map::class.java)

        if (response != null) {
            assertEquals(200, response.code())
        }
        val body = response?.body()
        Assertions.assertNotNull(body)
        assertEquals("Oklahoma", body!!["name"])
        assertEquals(123123, body["pincode"])
    }

    @Test
    fun testGetUserDataEndpoint() {
        val id = 69
        val response: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (response != null) {
            assertEquals(200, response.code())
        }
        val body = response?.body()
        Assertions.assertNotNull(body)
        assertEquals(69, body!!["id"])
        assertEquals("Peter", body["name"])
        assertEquals("Oklahoma", body["city"])
        assertEquals(123123, body["pincode"])
    }

    @Test
    fun testInsertCityData() {
        val userDataJson = "{\"name\":\"Florida\",\"pincode\":123666}"

        val request = HttpRequest.POST("/cityData/insert", userDataJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking()
            .exchange(request, String::class.java)

        val pincode = 123666
        val response1: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/cityData/$pincode", Map::class.java)

        if (response1 != null) {
            assertEquals(200, response1.code())
        }
        val body = response1?.body()
        Assertions.assertNotNull(body)
        assertEquals("Florida", body!!["name"])
        assertEquals(123666, body["pincode"])

        // Delete Record after successful record insertion
        val pincodeToDelete = 123666

        val deleteRequest = HttpRequest.DELETE<Any>("/cityData/delete/$pincodeToDelete")

        val response2: HttpResponse<Any> = client.toBlocking()
            .exchange(deleteRequest)

        assertEquals(204, response2.status.code)
    }

    @Test
    fun testInsertUserData() {
        val userDataJson = "{\"name\":\"Chris\",\"id\":169,\"city\":\"Quahog\",\"pincode\":123456}"

        val request = HttpRequest.POST("/userData/insert", userDataJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking()
            .exchange(request, String::class.java)

        val id = 169
        val response1: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (response1 != null) {
            assertEquals(200, response1.code())
        }
        val body = response1?.body()
        Assertions.assertNotNull(body)
        assertEquals(169, body!!["id"])
        assertEquals("Chris", body["name"])
        assertEquals("Quahog", body["city"])
        assertEquals(123456, body["pincode"])

        // Delete Record after successful record insertion
        val pincodeToDelete = 169

        val deleteRequest = HttpRequest.DELETE<Any>("/userData/delete/$pincodeToDelete")

        val response2: HttpResponse<Any> = client.toBlocking()
            .exchange(deleteRequest)

        assertEquals(204, response2.status.code)
    }

    @Test
    fun testDeleteCityData() {

        // Create Record for Deletion
        val userDataJson = "{\"name\":\"obama\",\"pincode\":369369}"

        val request1 = HttpRequest.POST("/cityData/insert", userDataJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking()
            .exchange(request1, String::class.java)

        val pincodeToDelete = 369369

        val request2 = HttpRequest.DELETE<Any>("/cityData/delete/$pincodeToDelete")

        val response: HttpResponse<Any> = client.toBlocking()
            .exchange(request2)

        assertEquals(204, response.status.code)

    }

    @Test
    fun testDeleteUserData() {

        // Create Record for Deletion
        val userDataJson = "{\"name\":\"california\",\"id\":122122,\"city\":\"lebronJames\",\"pincode\":666}"

        val request1 = HttpRequest.POST("/userData/insert", userDataJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking()
            .exchange(request1, String::class.java)

        val idToDelete = 122122

        val request2 = HttpRequest.DELETE<Any>("/userData/delete/$idToDelete")

        val response: HttpResponse<Any> = client.toBlocking()
            .exchange(request2)

        assertEquals(204, response.status.code)

    }

}


