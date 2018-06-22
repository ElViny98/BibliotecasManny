/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import Modelo.Conexion;
import java.sql.SQLException;

/**
 *
 * @author Vinicio
 */
public class mInicio {
    private Conexion conexion = new Conexion();
    
    public String[] obtenerSucursales(){
        try {
            Connection con = conexion.iniciarConexion("jdbc:mysql://localhost:3306/biblioteca_manny");
            if(con == null){
                System.out.println("Fallo en la conexión");
                return null;
            }
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT Nombre FROM Sucursal");
            if(!rs.last()){
                System.out.println("No hay ninguna sucursal");
                return null;
            }
            int numSuc = rs.getRow();
            rs.beforeFirst();
            String[] arraySuc = new String[numSuc];
            int x = 0;
            while(rs.next()){
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
}
