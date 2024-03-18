package sahaj.ai.controllers

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import sahaj.ai.repositories.City
import sahaj.ai.repositories.CityRepository
import sahaj.ai.repositories.UserRepository

@Controller("/cityData")
class CityController(
    private val cityRepository: CityRepository,
    private val userRepository: UserRepository
) {

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

    @Put("/update/{pincode}")
    fun updateCityNameForPincode(@PathVariable pincode: Int, @Body city: City): HttpStatus {
        val updated = cityRepository.updateCityName(pincode, city.name)
        if (updated) {
            userRepository.updateCityNamesForPincode(pincode, city.name)
            return HttpStatus.OK
        }
        return HttpStatus.BAD_REQUEST
    }

}