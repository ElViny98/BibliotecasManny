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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

/**
 *
 * @author Vinicio
 */
public class cPrestamo implements ActionListener, MouseListener {
    vPrestamos vP;
    mInicio mI;
    String nCliente;
    String nLibro;
    
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
        this.vP.tblCliente.setModel(this.mI.getNombreClientes());
        this.vP.tblLibro.setModel(this.mI.getNombreLibro());
        this.vP.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
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
            this.vP.pnlCliente.setVisible(!true);
            this.vP.pnlDatos.setVisible(true);
            this.vP.txtDireccion.setText("");
            this.vP.txtNombre.setText("");
            this.vP.txtTelefono.setText("");
        }
        
        if(e.getSource() == this.vP.btnGuardar) {
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == this.vP.tblCliente) {
            int fila = this.vP.tblCliente.rowAtPoint(e.getPoint());
            if(fila>-1) {
                this.nCliente = String.valueOf(this.vP.tblCliente.getValueAt(fila, 0));
            }
        }
        
        if(e.getSource() == this.vP.tblLibro) {
            int fila = this.vP.tblLibro.rowAtPoint(e.getPoint());
            if(fila>-1) {
                this.nLibro = String.valueOf(this.vP.tblLibro.getValueAt(fila, 0));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
