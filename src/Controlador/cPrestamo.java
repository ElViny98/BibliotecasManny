/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.mInicio;
import Vista.vPrestamos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Vinicio
 */
public class cPrestamo implements ActionListener {
    vPrestamos vP;
    mInicio mI;
    
    public cPrestamo(vPrestamos vP, mInicio mI) {
        this.vP = vP;
        this.mI = mI;
        this.vP.btnAgregar.addActionListener(this);
        this.vP.btnNuevo.addActionListener(this);
        this.vP.btnGuardar.addActionListener(this);
        this.vP.btnVolver.addActionListener(this);
    }
    
    public void iniciarVista() {
        this.vP.setVisible(true);
        this.vP.tblCliente.setRowHeight(50);
        this.vP.tblLibro.setRowHeight(50);
        this.vP.pnlCliente.setVisible(!true);
        this.vP.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.vP.btnNuevo) {
            this.vP.pnlCliente.setVisible(true);
            this.vP.pnlDatos.setVisible(!true);
        }
        
        if(e.getSource() == this.vP.btnVolver) {
            this.vP.pnlCliente.setVisible(!true);
            this.vP.pnlDatos.setVisible(true);
        }
        
        if(e.getSource() == this.vP.btnAgregar) {
            this.mI.insertarCliente(this.vP.txtNombre.getText(), this.vP.txtDireccion.getText(), this.vP.txtTelefono.getText());
        }
    }
    
}
