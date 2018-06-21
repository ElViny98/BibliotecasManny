package Controlador;

import Modelo.mInicio;
import Vista.vInicio;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class cInicio implements ActionListener{
    vInicio vI = new vInicio();
    mInicio mI = new mInicio();
    
    public cInicio(mInicio mI, vInicio vI) {
        this.vI = vI;
        this.mI = mI;
        
        /*this.vI.btnAgregar.addActionListener(this);
        this.vI.btnInventario.addActionListener(this);
        this.vI.btnHistorial.addActionListener(this);
        this.vI.btnSucursales.addActionListener(this);
        this.vI.btnPrestamo.addActionListener(this);*/
    }
    
    
    
    private JComponent[] getComponentes() {
        JComponent[] paneles = new JComponent[3];
        /*paneles[0] = this.vI.pnlInicio;
        paneles[1] = this.vI.pnlSaludo;*/
        //paneles[2] = this.vI.pnlAgregar;
        return paneles;
    }
    
    public void setComponentVisible(JComponent v) {
        for(int i=0; i<getComponentes().length; i++) {
            if(v == getComponentes()[i]) {
                v.setVisible(true);
            }
            else {
                v.setVisible(false);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      /*  if(e.getSource() == this.vI.btnAgregar) {
          //  setComponentVisible(this.vI.pnlAgregar);
            System.out.println("Clic 1");
        }
        if(e.getSource() == this.vI.btnInventario) {
            setComponentVisible(this.vI.pnlSaludo);
            System.out.println("Clic 2 ");
        }*/
    }
    
    public void iniciarVista() {
        this.vI.setTitle("Bibliotecas Manny");
        //setComponentVisible(this.vI.pnlSaludo);
        this.vI.setVisible(true);
        this.vI.setResizable(false);
        ImageIcon l = new ImageIcon(getClass().getResource("/img/logo.png"));
        ImageIcon l2 = new ImageIcon(l.getImage().getScaledInstance(77, 77, Image.SCALE_DEFAULT));
        this.vI.lblLogo.setIcon(l2);
    }
}
