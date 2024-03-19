package sahaj.ai.repositories

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.SerdeImport
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

    fun insertCityData(city: City): Boolean {
        val query = "INSERT INTO citydata (city, pincode) VALUES (?, ?)"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, city.name)
        preparedStatement.setInt(2, city.pincode)

        val rowsInserted = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsInserted > 0
    }

    fun updateCityName(pincode: Int, newName: String): Boolean {
        val query = "UPDATE citydata SET city = ? WHERE pincode = ?"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, newName)
        preparedStatement.setInt(2, pincode)

        val rowsUpdated = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsUpdated > 0
    }

    fun deleteCityData(pincode: Int): Boolean {
        val query = "DELETE FROM citydata WHERE pincode = ?"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, pincode)

        val rowsDeleted = preparedStatement.executeUpdate()

        preparedStatement.close()

        return rowsDeleted > 0
    }

}

@Serdeable.Serializable
@SerdeImport
data class City(val name: String, val pincode: Int)