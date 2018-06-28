package Controlador;

import Modelo.mInicio;
import Vista.vInicio;
import Vista.vSucursal;
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
        this.vI.btnInventario.addActionListener(this);
        this.vI.btnHistorial.addActionListener(this);
        this.vI.btnGuardar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.vI.btnGuardar) {
            this.mI.insertarLibro(this.vI.txtTitLibro.getText(), this.vI.txtAutLibro.getText(),
                    this.vI.txtPublic.getText(), this.vI.cmbGeneros.getSelectedItem().toString(),
                    this.vI.txtNumCopias.getText(), this.vI.txtNumPaginas.getText());
        }
        
        if(e.getSource() == this.vI.btnAgregar) {
            this.vI.pnlHistorial.setVisible(false);
            this.vI.pnlAgregar.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
            System.out.println("Clic 1");
        }
        
        if(e.getSource() == this.vI.btnPrestamo) {
            this.vI.pnlHistorial.setVisible(false);
            this.vI.pnlPrestamos.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlAgregar.setVisible(false);
        }
        
        if(e.getSource() == this.vI.btnHistorial) {
            this.vI.pnlHistorial.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
            this.vI.pnlInventario.setVisible(false);
        }
        
        if(e.getSource() == this.vS.btnAceptar){
            try {
                escribirFichero(new File("src/File/config"));
                iniciarVista();
            } catch (UnsupportedEncodingException ex) {}
        }
        
        if(e.getSource() == this.vI.btnInventario) {
            this.vI.pnlHistorial.setVisible(false);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
            this.vI.pnlInventario.setVisible(true);
            this.vI.tblInventario.setModel(this.mI.getInventario());
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
        vS.setTitle("Sucursal");
        vS.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
        vS.setResizable(false);
        vS.setLocationRelativeTo(null);
        vS.setVisible(true);
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
                    this.vI.tblHistorial.setRowHeight(50);
                    this.vI.tblInventario.setRowHeight(50);
                    this.vI.pnlPrestamos.setVisible(false);
                    this.vI.pnlAgregar.setVisible(false);
                    this.vI.pnlInventario.setVisible(false);
                    this.vI.pnlHistorial.setVisible(false);
                    this.vI.setLocationRelativeTo(null);
                    this.vI.setVisible(true);
                    this.vI.setResizable(false);
                    this.vI.setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
                }
                System.out.println(sucursal);
            }
        } catch (IOException ex) {}
    }
}
