package sahaj.ai

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class CityFetcherAPIsTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testItWorks() {
        assertTrue(application.isRunning)
    }

    @Test
    fun `We should be able to get data of city having PinCode as 123123`() {
        val pinCodeToVerifyPresence = 123123
        val responseToVerifyPresence: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/cityData/$pinCodeToVerifyPresence", Map::class.java)

        assertNotNull(responseToVerifyPresence)
        assertEquals(200, responseToVerifyPresence!!.code())

        val responseBody = responseToVerifyPresence.body()
        assertNotNull(responseBody)
        assertEquals("Oklahoma", responseBody["name"])
        assertEquals(123123, responseBody["pincode"])
    }

    @Test
    fun `We should be able to get data of user having ID 69`() {
        val idToVerifyPresence = 69
        val responseToVerifyPresence: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$idToVerifyPresence", Map::class.java)

        assertNotNull(responseToVerifyPresence)
        assertEquals(200, responseToVerifyPresence!!.code())

        val responseBody = responseToVerifyPresence.body()
        assertNotNull(responseBody)
        assertEquals(69, responseBody["id"])
        assertEquals("Peter", responseBody["name"])
        assertEquals("Oklahoma", responseBody["city"])
        assertEquals(123123, responseBody["pincode"])
    }

    @Test
    fun `We should be able to insert city data in the Database`() {
        val dummyCityDataForInsertionJson = "{\"name\":\"Florida\",\"pincode\":123666}"

        val requestToInsertRecord = HttpRequest.POST("/cityData/insert", dummyCityDataForInsertionJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking().exchange(requestToInsertRecord, String::class.java)

        val pinCodeToVerifyInsertion = 123666
        val responseToVerifyInsertion: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/cityData/$pinCodeToVerifyInsertion", Map::class.java)

        assertNotNull(responseToVerifyInsertion)
        assertEquals(200, responseToVerifyInsertion!!.code())

        val responseBody = responseToVerifyInsertion.body()
        assertNotNull(responseBody)
        assertEquals("Florida", responseBody!!["name"])
        assertEquals(123666, responseBody["pincode"])

        // Delete dummy Record after successful record insertion
        val pinCodeToDelete = 123666
        val requestToDeleteRecord = HttpRequest.DELETE<Any>("/cityData/delete/$pinCodeToDelete")
        val responseToVerifyDeletion: HttpResponse<Any> = client.toBlocking().exchange(requestToDeleteRecord)
        assertEquals(204, responseToVerifyDeletion.status.code)
    }

    @Test
    fun `We should be able to insert user data in the Database`() {
        val dummyUserDataForInsertionJson = "{\"name\":\"Chris\",\"id\":169,\"city\":\"Quahog\",\"pincode\":123456}"

        val requestToInsertRecord = HttpRequest.POST("/userData/insert", dummyUserDataForInsertionJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking().exchange(requestToInsertRecord, String::class.java)

        val idToVerifyInsertion = 169
        val responseToVerifyInsertion: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$idToVerifyInsertion", Map::class.java)

        assertNotNull(responseToVerifyInsertion)
        assertEquals(200, responseToVerifyInsertion!!.code())

        val responseBody = responseToVerifyInsertion.body()
        assertNotNull(responseBody)
        assertEquals(169, responseBody["id"])
        assertEquals("Chris", responseBody["name"])
        assertEquals("Quahog", responseBody["city"])
        assertEquals(123456, responseBody["pincode"])

        // Delete dummy Record after successful record insertion
        val idToDelete = 169
        val requestToDeleteRecord = HttpRequest.DELETE<Any>("/userData/delete/$idToDelete")
        val responseToVerifyDeletion: HttpResponse<Any> = client.toBlocking().exchange(requestToDeleteRecord)
        assertEquals(204, responseToVerifyDeletion.status.code)
    }

    @Test
    fun `We should be able to delete city data for city having pincode as 369369`() {
        // Create dummy Record for Deletion
        val dummyUserDataForDeletionJson = "{\"name\":\"obama\",\"pincode\":369369}"

        val requestToInsertData = HttpRequest.POST("/cityData/insert", dummyUserDataForDeletionJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking().exchange(requestToInsertData, String::class.java)

        val pinCodeToDelete = 369369
        val requestToDeleteData = HttpRequest.DELETE<Any>("/cityData/delete/$pinCodeToDelete")
        
        val responseToVerifyDeletion: HttpResponse<Any> = client.toBlocking().exchange(requestToDeleteData)
        assertEquals(204, responseToVerifyDeletion.status.code)

    }

    @Test
    fun `We should be able to delete user data for user having id as 122122`() {

        // Create dummy Record for Deletion
        val dummyUserDataForDeletionJson = "{\"name\":\"california\",\"id\":122122,\"city\":\"lebronJames\",\"pincode\":666}"

        val requestToInsertData = HttpRequest.POST("/userData/insert", dummyUserDataForDeletionJson)
            .contentType(MediaType.APPLICATION_JSON)

        client.toBlocking().exchange(requestToInsertData, String::class.java)

        val idToDelete = 122122

        val requestToDeleteData = HttpRequest.DELETE<Any>("/userData/delete/$idToDelete")

        val responseToVerifyDeletion: HttpResponse<Any> = client.toBlocking().exchange(requestToDeleteData)

        assertEquals(204, responseToVerifyDeletion.status.code)

    }

}


