/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Tarea;
import vista.Tareas;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;



public class controlador {
    
    private Tareas vista;
    private DefaultListModel<Tarea> modeloLista;
    

    public controlador(Tareas vista) {
    this.vista = vista;
    this.modeloLista = new DefaultListModel<>();
    this.vista.lstTarea.setModel(modeloLista); // <--- Acceso directo a la propiedad public
    
    iniciarEventos();
}

private void iniciarEventos() {
    vista.btnAgregar.addActionListener(e -> agregarTarea()); // <--- Acceso directo a la propiedad public
    vista.btnCompletada.addActionListener(e -> marcarCompletada()); // <--- Acceso directo a la propiedad public
}

    private void agregarTarea() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la tarea:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            modeloLista.addElement(new Tarea(nombre));
        }
    }

    private void marcarCompletada() {
        int index = vista.lstTarea.getSelectedIndex();
        if (index != -1) {
            Tarea t = modeloLista.get(index);
            t.marcarCompletada();
            vista.lstTarea.repaint(); // Actualiza la lista
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una tarea primero");
        }
    }
}
