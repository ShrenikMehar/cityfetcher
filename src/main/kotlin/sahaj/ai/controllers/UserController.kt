package sahaj.ai.controllers

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import sahaj.ai.repositories.User
import sahaj.ai.repositories.UserRepository

@Controller("/userData")
class UserController(private val userRepository: UserRepository) {

    @Get("/{id}")
    fun getUserData(@PathVariable id: Int): User? {
        return userRepository.getUserData(id)
    }

    @Post("/insert")
    fun insertUserData(@Body user: User): HttpStatus {
        return if (userRepository.insertUserData(user)) {
            HttpStatus.CREATED
        } else {
            HttpStatus.BAD_REQUEST
        }
    }

    @Delete("/delete/{id}")
    fun deleteUserData(@PathVariable id: Int): HttpStatus {
        return if (userRepository.deleteUserData(id)) {
            HttpStatus.NO_CONTENT
        } else {
            HttpStatus.NOT_FOUND
        }
    }
}
