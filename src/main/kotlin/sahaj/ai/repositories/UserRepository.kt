package sahaj.ai.repositories

import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

@Singleton
class UserRepository {

    // Establish database connection in the constructor
    private val connection: Connection =
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")

    fun getUserData(userId: Int): User? {
        var userData: User? = null
        val query = "SELECT * FROM UserData WHERE id = ?"

        // Use PreparedStatement to prevent SQL injection
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, userId)

        // Execute the query
        val resultSet: ResultSet = preparedStatement.executeQuery()

        // Process the result
        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val city = resultSet.getString("city")
            val pincode = resultSet.getInt("pincode")
            // Create a UserData object
            userData = User(userId, name, city, pincode)
        }

        // Close the ResultSet and PreparedStatement
        resultSet.close()
        preparedStatement.close()

        return userData
    }

}

@Serdeable.Serializable
data class User(val id: Int, val name: String, val city: String, val pincode: Int)
