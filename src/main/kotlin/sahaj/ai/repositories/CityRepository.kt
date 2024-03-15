package sahaj.ai.repositories

import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

@Singleton
class CityRepository {

    // Establish database connection in the constructor
    private val connection: Connection =
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")

    fun getCityData(pincode: Int): City? {
        var cityData: City? = null
        val query = "SELECT city FROM citydata WHERE pincode = ?"

        // Use PreparedStatement to prevent SQL injection
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, pincode)

        // Execute the query
        val resultSet: ResultSet = preparedStatement.executeQuery()

        // Process the results
        if (resultSet.next()) {
            val cityName = resultSet.getString("city")
            // Create a City object
            cityData = City(cityName, pincode)
        }

        // Close the ResultSet and PreparedStatement
        resultSet.close()
        preparedStatement.close()

        return cityData
    }

}

@Serdeable.Serializable
data class City(val name: String, val pincode: Int)
