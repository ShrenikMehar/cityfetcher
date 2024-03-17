package sahaj.ai.controllers

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import sahaj.ai.repositories.City
import sahaj.ai.repositories.User
import sahaj.ai.repositories.UserRepository

@Controller("/userData")
class UserController(private val userRepository: UserRepository) {

    @Get("/{id}")
    fun getUserData(@PathVariable id: Int): User? {
        return userRepository.getUserData(id)
    }

    @Post("/insert")
    fun insertCityData(@Body user: User): HttpStatus {
        return if (userRepository.insertUserData(user)) {
            HttpStatus.CREATED
        } else {
            HttpStatus.BAD_REQUEST
        }
    }
}
