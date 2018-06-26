package Modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encargará de manejar los errores en los servidores de bases 
 * de datos
 */
public class DatabaseErrorHandler {
    Conexion cn = new Conexion();
    protected final String[] passwords = { "1234567", "1234567", "1234567" };
    protected final String[] servers = { "jdbc:mysql:/172.16.3.92:3306/Biblioteca_Manny", "jdbc:mysql:/192.168.43.123:3306/Biblioteca_Manny", 
    "jdbc:mysql:/192.168.43.23:3306/Biblioteca_Manny" };
    protected String database;
    
    public Connection checkConnection(String server, String pass) {
        Connection con;
        try {
            con = cn.iniciarConexion(server, pass);
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Connection retryConnection(String server, String pass) {
        if(checkConnection(server, pass) == null) {
            try {
                return cn.iniciarConexion(server, pass);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
