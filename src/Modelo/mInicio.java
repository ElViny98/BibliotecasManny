/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vinicio
 */

public class mInicio extends DatabaseErrorHandler {
    private Conexion conexion = new Conexion();
    
    public String[] obtenerSucursales() {
        try {
            int i=0;
            Connection con;
            //Para conectarse a su base de datos primero debe obtener la información de una base de datos por defecto
            con = firstConnection(servers[0], passwords[0]);
            
            if(con == null) {
                System.out.println("Fallo en la conexión");
                return null;
            }
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT IdSucursal,Nombre FROM sucursal");
            if(!rs.last()) {
                System.out.println("No hay ninguna sucursal");
                return null;
            }
            
            int numSuc = rs.getRow();
            rs.beforeFirst();
            String[] arraySuc = new String[numSuc];
            int x = 0;
            while(rs.next()) {
                arraySuc[x] = rs.getString("Nombre");
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
        con = firstConnection(servers[0], passwords[0]);
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
        con = firstConnection(servers[0], passwords[0]);
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
        con = firstConnection(servers[0] , passwords[0]);
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
}
