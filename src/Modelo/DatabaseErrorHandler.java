package Modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encargará de manejar los errores en los servidores de bases 
 * de datos */
public class DatabaseErrorHandler {
    Conexion cn = new Conexion();
    protected final String[] passwords = { 
        "1234567", 
        "1234567", 
        "1234567" 
    };
    
    protected final String[] servers = { 
        "jdbc:mysql://172.20.10.5:3306/Biblioteca_Manny?zeroDateTimeBehavior=convertToNull", 
        "jdbc:mysql://192.168.1.173:3306/Biblioteca_Manny?zeroDateTimeBehavior=convertToNull", 
        "jdbc:mysql://192.168.43.23:3306/Biblioteca_Manny?zeroDateTimeBehavior=convertToNull" 
    };
    
    public Connection firstConnection(String server, String pass) {
        Connection con;
        try {
            con = cn.iniciarConexion(server, pass);            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return con;
    }
    
    public Connection makeConnection(String server, String pass) {
        Connection con;
            try {
                con = cn.iniciarConexion("jdbc:mysql://"+server+":3306/Biblioteca_Manny?zeroDateTimeBehavior=convertToNull", pass);
                return con;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseErrorHandler.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
}
