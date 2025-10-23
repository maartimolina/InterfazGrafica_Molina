/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Mar
 */
public class Conversor {
    public double celsiusAFahrenheit(double c) {
        return c * 9.0 / 5.0 + 32.0;
    }

    public double fahrenheitACelsius(double f) {
        return (f - 32.0) * 5.0 / 9.0;
    }
}
