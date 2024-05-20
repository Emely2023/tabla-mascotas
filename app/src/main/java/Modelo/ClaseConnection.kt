package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConnection {

    fun cadenaConexion(): Connection? {

        try {
            val ip = "jdbc:oracle:thin:@10.10.0.65:1521:xe"
            val user = "system" //Esta onda puede cambiar segun la PC
            val contrasena = "desarrollo" //Esta onda tambien puede cambiar segun la PC

            val connection = DriverManager.getConnection(ip, user, contrasena)
            return connection
        } catch (e: Exception) {
            println("Este es el error: $e")
            return null
        }
    }
}