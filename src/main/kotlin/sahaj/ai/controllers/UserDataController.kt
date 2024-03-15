package sahaj.ai.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import sahaj.ai.repositories.City
import sahaj.ai.repositories.User
import sahaj.ai.repositories.UserRepository

@Controller("/getUserData")
class UserController(private val userRepository: UserRepository) {

    @Get("/{id}")
    fun getUserData(@PathVariable id: Int): User? {
        return userRepository.getUserData(id)
    }
}
