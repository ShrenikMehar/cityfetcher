package sahaj.ai.repositories

import io.micronaut.serde.annotation.SerdeImport
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

    fun insertUserData(user: User): Boolean {
        val query = "INSERT INTO userdata (id, name, city, pincode) VALUES (?, ?, ?, ?)"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, user.id)
        preparedStatement.setString(2, user.name)
        preparedStatement.setString(3, user.city)
        preparedStatement.setInt(4, user.pincode)

        val rowsInserted = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsInserted > 0
    }

    fun updateCityNamesForPincode(pincode: Int, newName: String): Boolean {
        val query = "UPDATE UserData SET city = ? WHERE pincode = ?"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, newName)
        preparedStatement.setInt(2, pincode)

        val rowsUpdated = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsUpdated > 0
    }

    fun deleteUserData(id: Int): Boolean {
        val query = "DELETE FROM userdata WHERE id = ?"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)

        val rowsDeleted = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsDeleted > 0
    }

}

@Serdeable.Serializable
@SerdeImport
data class User(val id: Int, val name: String, val city: String, val pincode: Int)