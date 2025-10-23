/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import vista.conversor;
import modelo.Conversor;
import javax.swing.*;/**
 *
 * @author Mar
 */
public class ControladorConversor {
    private final Conversor modelo;
    private final conversor vista;
     public ControladorConversor(Conversor modelo, conversor vista) {
        this.modelo = modelo;
        this.vista = vista;
        initEventos();
    }
     private void initEventos() {
        vista.btnConvertir.addActionListener(e -> convertir());
        // opcional: convertir al apretar Enter en el campo de entrada
        vista.txtEntrada.addActionListener(e -> convertir());
    }
     private void convertir() {
        String txt = vista.txtEntrada.getText().trim();
        if (txt.isEmpty()) {
            mensaje("Ingresá un número.");
            return;
        }
        try {
            double valor = Double.parseDouble(txt);
            String tipo = (String) vista.cbConversion.getSelectedItem();
            double resultado;

            if ("Celsius → Fahrenheit".equals(tipo)) {
                resultado = modelo.celsiusAFahrenheit(valor);
            } else {
                resultado = modelo.fahrenheitACelsius(valor);
            }
            // mostramos con 2 decimales
            vista.txtSalida.setText(String.format("%.2f", resultado));
        } catch (NumberFormatException ex) {
            mensaje("Formato inválido. Ej: 23.5");
        }
    }

    private void mensaje(String s) {
        JOptionPane.showMessageDialog(vista, s);
    }
}
