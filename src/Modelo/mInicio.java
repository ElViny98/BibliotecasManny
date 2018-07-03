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
    
    public DefaultTableModel getClientes(){
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            String[] txtModelo = new String[]{"ID", "Nombre" , "Dirección", "Teléfono"};
            ResultSet rS = st.executeQuery("SELECT * FROM cliente");
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
    /**
     * Esta madre no lo combine con insertar libros como en clientes porque hay más pedos en las variables 
     * <br>
     * <ul>
     * <li>0: Titulo</li>
     * <li>1: Autor</li>
     * <li>2: Anio</li>
     * <li>3: Genero</li>
     * <li>4: Copias Prestadas</li>
     * <li>5: Total Copias</li>
     * <li>6: Sucursal(Nombre)</li>
     * <li>7: Editorial</li>
     * <li>8: Num. Paginas</li>
     * <li>9: Costo Perdida</li>
     * <li>10: IdLibro</li>
     * </ul>
     * @param datos
     * @return 
     */
    public boolean modificarLibros(String...datos){
        String txtSQL = "UPDATE libro "
                + "SET Titulo = '"+datos[0]+"', "
                + "Autor = '"+datos[1]+"', "
                + "Anio = '"+datos[2]+"', "
                + "Genero = '"+datos[3]+"', "
                + "CopiasPrestadas = '"+datos[4]+"', "
                + "TotalCopias = '"+datos[5]+"', "
                + "Sucursal_IdSucursal = (SELECT IdSucursal FROM sucursal WHERE Nombre='"+datos[6]+"'), "
                + "Editorial = '"+datos[7]+"', "
                + "NumPaginas = '"+datos[8]+"', "
                + "CostoPerdida = '"+datos[9]+"' "
                + "WHERE IdLibro = "+datos[10]+"";
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            st.executeUpdate(txtSQL);
            cn.cerrarConexion(con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /**
     * Parametros <br>
     * <ul>
     * <li>1) 1.Para agregar. 2.Modificar.</li>
     * <li>2) Nombre</li>
     * <li>3) Direccion</li>
     * <li>4) Cliente</li> 
     * <li>5) ID (Solo cuando va a modificar)</li>
     * </ul>
     * @param opc
     * @param datos
     * @return 
     */
    public boolean insertarCliente(int opc, String... datos) {
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        String txtSQL = "";
        //con = makeConnection("localhost", "");
        if(opc==1){
            txtSQL = "INSERT INTO cliente(Nombre, Direccion, Telefono) VALUES('"+datos[0]+"', '"+datos[1]+"', '"+datos[2]+"')";
        }
        else if(opc==2){
            txtSQL = "UPDATE cliente SET Nombre = '"+datos[0]+"', Direccion = '"+datos[1]+"', Telefono = '"+datos[2]+"' WHERE IdCliente = "+datos[3]+"";
        }
        try {
            Statement st = con.createStatement();
            if("".equals(txtSQL))
                return false;
            else{
                st.executeUpdate(txtSQL);
            }
            cn.cerrarConexion(con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /**
     * Para eliminar lo que sea
     * <ul>
     * <li>1) Nombre de la tabla </li>
     * <li>2) Nombre de la columna ID </li>
     * <li>3) ID a eliminar </li>
     * </ul>
     * @param tabla
     * @param idColumn
     * @param id
     * @return 
     */
    public boolean eliminar(String tabla, String idColumn, int id){
        Connection con;
        String[] suc = getServer();
        con = makeConnection(suc[0], suc[1]);
        try {
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM "+tabla+" WHERE "+idColumn+"="+id+"");
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
