package sahaj.ai.controllers

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import sahaj.ai.repositories.City
import sahaj.ai.repositories.CityRepository

@Controller("/cityData")
class CityController(private val cityRepository: CityRepository) {

    @Get("/{pincode}")
    fun getCityData(@PathVariable pincode: Int): City? {
        return cityRepository.getCityData(pincode)
    }

    @Post("/insert")
    fun insertCityData(@Body city: City): HttpStatus {
        return if (cityRepository.insertCityData(city)) {
            HttpStatus.CREATED
        } else {
            HttpStatus.BAD_REQUEST
        }
    }

}