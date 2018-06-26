package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    
    public Connection iniciarConexion(String server, String pass) throws SQLException {
        Connection con;
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        con = DriverManager.getConnection(server, "root", pass);
        return con;
    }
    
    public void cerrarConexion(Connection con) {
        try {
            if(!con.isClosed()) 
                con.close();
        } catch(SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
