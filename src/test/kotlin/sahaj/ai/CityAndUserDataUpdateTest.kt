package sahaj.ai

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest
class CityAndUserDataUpdateTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `City name should be updated to LosSantos for All user records having city name as LA and pincode as 123699`() {

        val requiredPinCode = 123699
        val updatedCityName = "LosSantos"

        val requestToUpdateData: HttpRequest<Any> =
            HttpRequest.PUT("/cityData/update/$requiredPinCode", mapOf("name" to updatedCityName))

        client.toBlocking().exchange<Any, Any>(requestToUpdateData)

        val responseToVerifyCityDataUpdate: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/cityData/$requiredPinCode", Map::class.java)

        if (responseToVerifyCityDataUpdate != null) {
            assertEquals(200, responseToVerifyCityDataUpdate.code())
        }

        val updatedCityDataBody = responseToVerifyCityDataUpdate?.body()
        Assertions.assertNotNull(updatedCityDataBody)
        assertEquals("LosSantos", updatedCityDataBody!!["name"])
        assertEquals(123699, updatedCityDataBody["pincode"])

        val id = 420
        val responseToVerifyUserDataUpdate: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (responseToVerifyUserDataUpdate != null) {
            assertEquals(200, responseToVerifyUserDataUpdate.code())
        }

        val updatedUserDataBody = responseToVerifyUserDataUpdate?.body()
        Assertions.assertNotNull(updatedUserDataBody)
        assertEquals(420, updatedUserDataBody!!["id"])
        assertEquals("Quagmire", updatedUserDataBody["name"])
        assertEquals("LosSantos", updatedUserDataBody["city"])
        assertEquals(123699, updatedUserDataBody["pincode"])

        // Revert back to original city name
        val originalCityName = "LA"

        val requestToRevertData: HttpRequest<Any> =
            HttpRequest.PUT("/cityData/update/$requiredPinCode", mapOf("name" to originalCityName))

        client.toBlocking().exchange<Any, Any>(requestToRevertData)

        val responseToVerifyCityDataRevert: HttpResponse<Map<*, *>>? = client.toBlocking()
            .exchange("/userData/$id", Map::class.java)

        if (responseToVerifyCityDataRevert != null) {
            assertEquals(200, responseToVerifyCityDataRevert.code())
        }

        val revertedCityDataBody = responseToVerifyCityDataRevert?.body()
        Assertions.assertNotNull(revertedCityDataBody)
        assertEquals(420, revertedCityDataBody!!["id"])
        assertEquals("Quagmire", revertedCityDataBody["name"])
        assertEquals("LA", revertedCityDataBody["city"])
        assertEquals(123699, revertedCityDataBody["pincode"])


    }
}
