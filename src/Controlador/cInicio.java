package Controlador;

import Modelo.mInicio;
import Vista.vInicio;
import Vista.vSucursal;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class cInicio implements ActionListener{
    vInicio vI = new vInicio();
    mInicio mI = new mInicio();
    vSucursal vS = new vSucursal();
    String sucursal; 
    public cInicio(mInicio mI, vInicio vI) {
        this.vI = vI;
        this.mI = mI;
        
        /*this.vI.btnAgregar.addActionListener(this);
        this.vI.btnInventario.addActionListener(this);
        this.vI.btnHistorial.addActionListener(this);
        this.vI.btnSucursales.addActionListener(this);
        this.vI.btnPrestamo.addActionListener(this);*/
        vS.btnAceptar.addActionListener(this);
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
        if(e.getSource() == this.vS.btnAceptar){
            try {
                escribirFichero(new File("src/File/config"));
                iniciarVista();
            } catch (UnsupportedEncodingException ex) {}
        }
    }
    
    public boolean existeFichero() throws IOException{
        File config = new File("src/File/config"); 
        boolean fileExist = config.exists();
        if(!fileExist){
            config.getParentFile().mkdirs();
            config.createNewFile();
        }
        return fileExist;
    }
   
    public void escribirFichero(File conf) throws UnsupportedEncodingException{
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(conf), "utf-8"))) {
            writer.write(String.valueOf("Sucursal:"+vS.comboSucursales.getSelectedItem()));
         }       catch (IOException ex) {
            Logger.getLogger(cInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String leerFichero() throws FileNotFoundException{
        Properties prop = new Properties();
        InputStream is = new FileInputStream("src/File/config");
        String suc = null;
        try {
            prop.load(is);
            suc = prop.getProperty("Sucursal");
            is.close();
        } catch (IOException ex) {
            System.out.println("No se pudo cargar el archivo");
        }
        if(suc == null)
        {
            File file = new File("src/File/config");
            file.delete();
        }
        return prop.getProperty("Sucursal");
    }
    
    public void ventanaSucursal(){
        String[] sucursales = mI.obtenerSucursales();
        if(sucursales != null){
            vS.comboSucursales.setModel(new DefaultComboBoxModel(sucursales));
        }
        else{
            vS.comboSucursales.disable();
        }
        vS.setLocationRelativeTo(null);
        vS.setVisible(true);
    }
    
    public void iniciarVista() {
        this.vI.setTitle("Bibliotecas Manny");
        try {
            if(!existeFichero()){
                ventanaSucursal();
                iniciarVista();
            }
            else{
                sucursal = leerFichero();
                if(sucursal!=null){
                    this.vI.setVisible(true);
                    this.vI.setResizable(false);
                    ImageIcon l = new ImageIcon(getClass().getResource("/img/logo.png"));
                    ImageIcon l2 = new ImageIcon(l.getImage().getScaledInstance(77, 77, Image.SCALE_DEFAULT));
                    this.vI.lblLogo.setIcon(l2);
                }
                System.out.println(sucursal);
                
                //setComponentVisible(this.vI.pnlSaludo);
            }
        } catch (IOException ex) {}
    }
}
