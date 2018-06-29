/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vinicio
 */

public class mInicio extends DatabaseErrorHandler implements Runnable {
    private Conexion conexion = new Conexion();
    private int net;
    private String ipserver1;
    private String passserver1;
    private String ipserver2;
    private String passserver2;
    private String ipserver3;
    private String passserver3;
    private boolean statusNetwork1 = true;
    private boolean statusNetwork2 = true;
    private boolean statusNetwork3 = true;
 
    Runnable net1;
    Runnable net2;
    Runnable net3;
    
    public String[][] obtenerSucursales() {
        try {
            int i=0;
            Connection con = null;
            //Para conectarse a su base de datos primero debe obtener la información de una base de datos por defecto
            while(con == null && i < 3)
            {
                con = firstConnection(servers[i], passwords[i]);
                i++;
            }
            
            if(con == null) {
                System.out.println("Fallo en la conexión");
                return null;
            }
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT IdSucursal,Nombre,IP,Password FROM sucursal");
            if(!rs.last()) {
                System.out.println("No hay ninguna sucursal");
                return null;
            }
            
            int numSuc = rs.getRow();
            rs.beforeFirst();
            String[][] arraySuc = new String[numSuc][4];
            int x = 0;
            while(rs.next()) {
                arraySuc[x][0] = rs.getString("IdSucursal");
                arraySuc[x][1] = rs.getString("Nombre");
                arraySuc[x][2] = rs.getString("IP");
                arraySuc[x][3] = rs.getString("Password");
                x++;
            }
            
            conexion.cerrarConexion(con);
            return arraySuc;
            
        } catch (SQLException ex) {
            System.out.println("Fallo en la conexión");
            return null;
        }
    }
    /**
     * Parametros
     * 0) Cuando es para prestamos activos
     * 1) Cuando es para historial
     * @param opc
     * @return 
     */
    public DefaultTableModel getPrestamos(int opc) {
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            String query;
            if(opc == 0){
                query = "SELECT cliente.Nombre, libro.Titulo, FechaPrestamo, FechaEntrega, FechaLimite, Multa "
                    + "FROM reserva, cliente, libro WHERE cliente.IdCliente = Cliente_IdCliente AND libro.IdLibro = Libro_IdLibro "
                    + "AND (FechaEntrega IS NULL OR FechaEntrega = '')";
            }
            else{
                query = "SELECT cliente.Nombre, libro.Titulo, FechaPrestamo, FechaEntrega, FechaLimite, Multa "
                    + "FROM reserva, cliente, libro WHERE cliente.IdCliente = Cliente_IdCliente AND libro.IdLibro = Libro_IdLibro "
                    + "AND (FechaEntrega IS NOT NULL AND FechaEntrega != '')";
            }
            String[] txtModelo =  new String[]{"Nombre cliente", "Titulo libro", "Fecha de préstamo", "Fecha de entrega", "Fecha límite", "Multa"};
            ResultSet rS = st.executeQuery(query);
            return tableModel(rS, txtModelo);
            
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public DefaultTableModel getInventario() {
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            String[] txtModelo = new String[]{"Titulo", "Autor" , "Año de publicación", "Género", "Copias totales", "Editorial", "Numero de páginas"};
            ResultSet rS = st.executeQuery("SELECT Titulo, Autor, Anio, Genero, TotalCopias, Editorial, NumPaginas FROM libro");
            return tableModel(rS, txtModelo);
        }
        catch(Exception e){
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public DefaultTableModel tableModel(ResultSet rs, String ... arrayModelo){
        DefaultTableModel modelo = new DefaultTableModel();
        for (String arrayModelo1 : arrayModelo) {
            modelo.addColumn(arrayModelo1);
        }
        try{
            ResultSetMetaData rSMd = rs.getMetaData();
            while(rs.next()) {
                    Object[] fila = new Object[rSMd.getColumnCount()];
                    for(int i=0; i<rSMd.getColumnCount(); i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    modelo.addRow(fila);
                }
            return modelo;
            }
        catch(SQLException e){
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    /**
     * Inserta un libro en la base de datos
     * Parametros.<br>
     * <ul>
     * <li>0: Título</li>
     * <li>1: Autor</li>
     * <li>2: Año de publicación </li>
     * <li>3: Género</li>
     * <li>4: Copias prestadas (Por defecto 0)</li>
     * <li>5: Total de copias recibidas</li>
     * <li>6: Sucursal</li>
     * <li>7: Editorial</li>
     * <li>8: Número de páginas </li>
     * <li>9: Multa por pérdida</li>
     * </ul>
     * @param datos datos del libro
     * @return 
     */
    public boolean insertarLibro(String... datos) {
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO libro(Titulo, Autor, Anio, Genero, CopiasPrestadas, TotalCopias, Sucursal_IdSucursal, Editorial, NumPaginas, CostoPerdida) "
                    + "VALUES('"+datos[0]+"', '"+datos[1]+"', '"+datos[2]+"', '"+datos[3]+"', 0, '"+datos[4]+"', (SELECT IdSucursal FROM sucursal WHERE Nombre='"+datos[5]+"'), '"+datos[6]+"', '"+datos[7]+"', '"+datos[8]+"')");
            cn.cerrarConexion(con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String[] getServer(){
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream("src/File/config");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        String suc[] = new String[2];
        try {
            prop.load(is);
            this.ipserver1 = prop.getProperty("IpSucursal");
            this.passserver1 = prop.getProperty("PassSucursal");
            this.ipserver2 = prop.getProperty("IpSucursal2");
            this.passserver2 = prop.getProperty("PassSucursal2");
            this.ipserver3 = prop.getProperty("IpSucursal3");
            this.passserver3 = prop.getProperty("PassSucursal3");
            if(this.statusNetwork1 == true && testConnection(this.ipserver1,this.passserver1)){
                System.out.println("Conexión 1 falló");
               System.out.println(prop.getProperty("Sucursal"));
               suc[0] = prop.getProperty("IpSucursal");
               suc[1] = prop.getProperty("PassSucursal");
            }
            else{
                this.statusNetwork1 = false;
                new Thread().start();
                if(this.statusNetwork2 == true && testConnection(this.ipserver2,this.passserver2)){
                    System.out.println(prop.getProperty("Sucursal2"));
                   new Thread(net2).start();
                    suc[0] = prop.getProperty("IpSucursal2");
                    suc[1] = prop.getProperty("PassSucursal2");
                }
                else{
                    this.statusNetwork2 = false;
                    System.out.println("Conexión 2 falló");
                    new Thread().start();
                    if(statusNetwork3 == true &&testConnection(this.ipserver1,this.passserver1)){
                        System.out.println(prop.getProperty("Sucursa3"));
                        suc[0] = prop.getProperty("IpSucursal3");
                        suc[1] = prop.getProperty("PassSucursal3");
                    }
                    else{
                        System.out.println("Todas las redes estan caidas");
                        this.statusNetwork3 = false;
                    }
                }
            }
            is.close();
            return suc;
        } catch (IOException ex) {
            System.out.println("No se pudo cargar el archivo");
            return null;
        }
    }
//    public mInicio(String ipServer, String pass){
//        this.ipserver1 = ipServer;
//        this.passserver1 = pass; 
//    }
//     public mInicio(){
//         
//    }
    
    public boolean testConnection(String server, String pass){
        Connection con;
        con = makeConnection(server , pass);
        System.out.println(con);
        return con != null;
    }
    
    @Override
    public void run() {
        Connection con;
        con = null;
        int x = 0;
        while(con == null){
            if(!this.statusNetwork1){
                con = makeConnection(this.ipserver1 , this.passserver1);
                if(con != null){
                    System.out.println("Se supone que ya prendió ptm >:c");
                    statusNetwork1 = true;
                }
            }
            else if(!this.statusNetwork2){
                con = makeConnection(this.ipserver2 , this.passserver2);
                if(con != null){
                    statusNetwork2 = true;
                }
            }
            else if(!this.statusNetwork3){
                con = makeConnection(this.ipserver3 , this.passserver3);
                if(con != null){
                    statusNetwork3 = true;
                }
            }
            //System.out.println("Network: "+this.statusNetwork1);
        } 
    }
    
    
}
