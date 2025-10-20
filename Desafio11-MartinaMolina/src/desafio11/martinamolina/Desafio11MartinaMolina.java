/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package desafio11.martinamolina;

import vista.Tareas;
import controlador.controlador;
/**
 *
 * @author Alumno
 */
public class Desafio11MartinaMolina {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Tareas vista = new Tareas();
        new controlador(vista);
        vista.setVisible(true);
    }
    
}
