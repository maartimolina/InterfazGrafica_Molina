/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package desafio11.martinamolina;

import vista.Tareas;
import controlador.controlador;
import controlador.ControladorConversor;
import modelo.Conversor;
import vista.conversor;
import javax.swing.SwingUtilities;
/**
 *
 * @author Alumno
 */
public class Desafio11MartinaMolina {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
        Tareas vistaTareas= new Tareas();
        new controlador(vistaTareas);
        vistaTareas.setVisible(true);
        
        Conversor modelo = new Conversor();
        conversor vistaConv = new conversor();
        new ControladorConversor(modelo, vistaConv);
        vistaConv.setVisible(true);
    });
        
    }
    
}
