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
            ResultSet rs = s.executeQuery("SELECT Nombre FROM Sucursal");
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
    
    public DefaultTableModel getPrestamos() {
        Connection con;
        DefaultTableModel modelo = new DefaultTableModel();
        con = firstConnection(servers[0], passwords[0]);
        try {
            Statement st = con.createStatement();
            modelo.addColumn("Nombre cliente");
            modelo.addColumn("Título libro");
            modelo.addColumn("Fecha de préstamo");
            modelo.addColumn("Fecha de entrega");
            modelo.addColumn("Costo");
            ResultSet rS = st.executeQuery("SELECT ");
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelo;
    }
    
    public DefaultTableModel getInventario() {
        Connection con;
        DefaultTableModel modelo = new DefaultTableModel();
        con = firstConnection(servers[0], passwords[0]);
        try {
            Statement st = con.createStatement();
            modelo.addColumn("Título");
            modelo.addColumn("Autor");
            modelo.addColumn("Año de publicación");
            modelo.addColumn("Género");
            modelo.addColumn("Copias totales");
            modelo.addColumn("Editorial");
            modelo.addColumn("Número de páginas");
            ResultSet rS = st.executeQuery("SELECT Titulo, Autor, Anio, Genero, TotalCopias, Editorial, NumPaginas FROM Libro");
            ResultSetMetaData rSMd = rS.getMetaData();
            while(rS.next()) {
                Object[] fila = new Object[rSMd.getColumnCount()];
                for(int i=0; i<rSMd.getColumnCount(); i++) {
                    fila[i] = rS.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
            return modelo;
        } catch (SQLException ex) {
            Logger.getLogger(mInicio.class.getName()).log(Level.SEVERE, null, ex);
            modelo = null;
        }
        return modelo;
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
     */
    public void insertarLibro(String... datos) {
        
    }
}
