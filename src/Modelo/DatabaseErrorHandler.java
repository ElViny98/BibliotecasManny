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
    protected final String server1 = "192.168.43.87";
    protected final String server2 = "192.168.43.123";
    protected final String server3 = "192.168.43.23";
    protected String database;
    
    public boolean checkConnection(String server) {
        try {
            cn.iniciarConexion(server);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Connection retryConnection(String server) {
        if(checkConnection(server)) {
            try {
                return cn.iniciarConexion(server);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
