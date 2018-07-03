package Controlador;

import Modelo.mInicio;
import Vista.vInicio;
import Vista.vPrestamos;
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
import javax.swing.JOptionPane;


public class cInicio implements ActionListener{
    vInicio vI = new vInicio();
    mInicio mI = new mInicio();
    vSucursal vS = new vSucursal();
    String sucursal; 
    String[][] sucursales;
    public cInicio(mInicio mI, vInicio vI) {
        this.vI = vI;
        this.mI = mI;
        
        vS.btnAceptar.addActionListener(this);
        this.vI.btnAgregar.addActionListener(this);
        this.vI.btnCancelarAdd.addActionListener(this);
        this.vI.btnPrestamo.addActionListener(this);
        this.vI.btnInventario.addActionListener(this);
        this.vI.btnHistorial.addActionListener(this);
        this.vI.btnGuardar.addActionListener(this);
        this.vI.btnAgregarPrestamo.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.vI.btnAgregarPrestamo) {
            vPrestamos vP = new vPrestamos();
            cPrestamo cP = new cPrestamo(vP, mI);
            cP.iniciarVista();
        }
        
        if(e.getSource() == this.vI.btnGuardar) {
            boolean insercion = this.mI.insertarLibro(this.vI.txtTitLibro.getText(), this.vI.txtAutLibro.getText(),
                    this.vI.txtPublic.getText(), this.vI.cmbGeneros.getSelectedItem().toString(),
                    this.vI.txtNumCopias.getText(), this.sucursal, this.vI.txtEditorial.getText(),
                    this.vI.txtNumPaginas.getText(), this.vI.txtNumPaginas1.getText());
            if(insercion){
                JOptionPane.showMessageDialog(this.vI,"El libro se ha agregado correctamente");
                limpiarInputsAdd();
            }
            else{
                JOptionPane.showMessageDialog(this.vI,"No se ha podido agregar el libro, intente de nuevo");
            }
        }
        if(e.getSource() == this.vI.btnCancelarAdd){
            limpiarInputsAdd();
        }
        if(e.getSource() == this.vI.btnAgregar) {
            this.vI.pnlHistorial.setVisible(false);
            this.vI.pnlAgregar.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
        }
        
        if(e.getSource() == this.vI.btnPrestamo) {
            this.vI.pnlHistorial.setVisible(false);
            this.vI.pnlPrestamos.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlAgregar.setVisible(false);
            this.vI.tblPrestamos.setModel(mI.getPrestamos(0));
        }
        
        if(e.getSource() == this.vI.btnHistorial) {
            this.vI.pnlHistorial.setVisible(true);
            this.vI.pnlInicio.setVisible(false);
            this.vI.pnlPrestamos.setVisible(false);
            this.vI.pnlInventario.setVisible(false);
            mI.modificarLibros("He-man","Stephen","1988","Terror","2","5","BiblioVini","Genaro","500","300","3");
            this.vI.tblHistorial.setModel(mI.getPrestamos(1));
        }
        
        if(e.getSource() == this.vS.btnAceptar){
            try {
                escribirFichero(new File("src/File/config"));
                iniciarVista();
                vS.dispose();
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
            this.sucursal = String.valueOf(vS.comboSucursales.getSelectedItem());
            int sw = 0;
            String id = "-1";
            String id2 = "-1";
            for (int i = 0; i < sucursales.length; i++) {
                if(sucursales[i][1] == null ? this.sucursal == null : sucursales[i][1].equals(this.sucursal) && sw == 0){
                    writer.write(String.valueOf("Sucursal:"+vS.comboSucursales.getSelectedItem()));
                    writer.write("\nIpSucursal:"+sucursales[i][2]);
                    writer.write("\nPassSucursal:"+sucursales[i][3]);
                    sw = 1;
                    id = sucursales[i][0];
                    i= -1;
                }               
                else if( sw == 1 && !sucursales[i][0].equals(id)){
                    writer.write(String.valueOf("\nSucursal2:"+sucursales[i][1]));
                    writer.write("\nIpSucursal2:"+sucursales[i][2]); 
                    writer.write("\nPassSucursal2:"+sucursales[i][3]);
                    sw = 2;
                    id2 = sucursales[i][0];
                }
                else if( sw == 2 && !sucursales[i][0].equals(id) && !sucursales[i][0].equals(id2)){
                    writer.write(String.valueOf("\nSucursal3:"+sucursales[i][1]));
                    writer.write("\nIpSucursal3:"+sucursales[i][2]); 
                    writer.write("\nPassSucursal3:"+sucursales[i][3]);
                }
            }
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
        sucursales = mI.obtenerSucursales();
        String[] nomSucursales = new String[sucursales.length];
        for (int i = 0; i < sucursales.length; i++) {
            nomSucursales[i] = sucursales[i][1];
        }
        if(nomSucursales != null){
            vS.comboSucursales.setModel(new DefaultComboBoxModel(nomSucursales));
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
                this.sucursal = leerFichero();
                if(this.sucursal!=null){
                    this.vI.tblPrestamos.setRowHeight(50);
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
                System.out.println(this.sucursal);
            }
        } catch (IOException ex) {}
    }
    
    public void limpiarInputsAdd(){
        this.vI.txtAutLibro.setText("");
        this.vI.txtNumPaginas1.setText("");
        this.vI.txtEditorial.setText("");
        this.vI.txtNumCopias.setText("");
        this.vI.txtNumPaginas.setText("");
        this.vI.txtPublic.setText("");
        this.vI.txtTitLibro.setText("");
        this.vI.cmbGeneros.setSelectedItem(0);
    }
}
