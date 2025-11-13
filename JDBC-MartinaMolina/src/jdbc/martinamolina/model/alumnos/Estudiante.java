/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.model.alumnos;

/**
 *
 * @author Mar
 */
public class Estudiante {
    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private Integer edad;
    
     public Estudiante() { }
     public Estudiante(Integer id, String nombre, String apellido, String email, Integer edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
    }

    public Estudiante(String nombre, String apellido, String email, Integer edad) {
        this(null, nombre, apellido, email, edad);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
}
