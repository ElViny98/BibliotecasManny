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
        
        vS.btnAceptar.addActionListener(this);
        this.vI.btnAgregar.addActionListener(this);
        this.vI.btnPrestamo.addActionListener(this);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.vI.btnAgregar) {
            this.vI.pnlAgregar.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
            System.out.println("Clic 1");
        }
        
        if(e.getSource() == this.vI.btnPrestamo) {
            this.vI.pnlPrestamos.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlAgregar.setVisible(false);
        }
        
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
            vS.comboSucursales.setEnabled(false);
        }
        vS.setResizable(true);
        vS.setLocationRelativeTo(null);
        vS.setVisible(true);
        vS.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public void iniciarVista() {
        this.vI.setTitle("Bibliotecas Manny");
        try {
            if(!existeFichero()){
                ventanaSucursal();
                iniciarVista();
                System.out.println("");
            }
            else{
                sucursal = leerFichero();
                if(sucursal!=null){
                    this.vI.pnlPrestamos.setVisible(false);
                    this.vI.pnlAgregar.setVisible(false);
                    this.vI.setLocationRelativeTo(null);
                    this.vI.setVisible(true);
                    this.vI.setResizable(false);
                    ImageIcon l = new ImageIcon(getClass().getResource("/img/logo.png"));
                    ImageIcon l2 = new ImageIcon(l.getImage().getScaledInstance(77, 77, Image.SCALE_DEFAULT));
                    //this.vI.lblLogo.setIcon(l2);
                }
                System.out.println(sucursal);
            }
        } catch (IOException ex) {}
    }
}
