package sahaj.ai.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import sahaj.ai.repositories.City
import sahaj.ai.repositories.CityRepository

@Controller("/getCityData")
class CityController(private val cityRepository: CityRepository) {

    @Get("/{pincode}")
    fun getCityData(@PathVariable pincode: Int): City? {
        return cityRepository.getCityData(pincode)
    }
}
