/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina;
import java.sql.*;

/**
 *
 * @author Mar
 */
public class dbConexion {
    private static String URL = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static String USER = "root";
    private static String PASSWORD = "241291Mm.";
   
    public static Connection obtenerConexion() throws SQLException{
        try{
           //Cargar Driver
           Class.forName("com.mysql.cj.jdbc.Driver");
           //Establecer y retornar conexion 
           return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(ClassNotFoundException e){
            throw new SQLException("No se encontró el driver de MySQL", e);
        }
    }
    public static void probarConexion(){
        try(Connection conn = obtenerConexion()){
           System.out.println("Conexion exitosa a la base");
           System.out.println("Base de datos: " + conn.getCatalog());
        }catch(SQLException e){
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
    
}
