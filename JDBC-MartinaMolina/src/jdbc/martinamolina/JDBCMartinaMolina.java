/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jdbc.martinamolina;
import jdbc.martinamolina.view.biblioteca.BibliotecaView;
import jdbc.martinamolina.controller.biblioteca.LibroController;
import jdbc.martinamolina.view.productos.ProductosView;
import jdbc.martinamolina.controller.productos.ProductoController;
import javax.swing.SwingUtilities;
/**
 *
 * @author Mar
 */
public class JDBCMartinaMolina {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       dbConexion.probarConexion();

        //javax.swing.SwingUtilities.invokeLater(() -> {
        //    BibliotecaView v = new BibliotecaView();
        //    new LibroController(v);     // el controller cablea los eventos
        //    v.setVisible(true);         // si ya lo hacés en el controller, esta línea es opcional
        //});
        //SwingUtilities.invokeLater(()->{
          //  ProductosView v = new ProductosView();
            //new ProductoController(v);
        //});
        javax.swing.SwingUtilities.invokeLater(()->new menu().setVisible(true));
    }
    }
