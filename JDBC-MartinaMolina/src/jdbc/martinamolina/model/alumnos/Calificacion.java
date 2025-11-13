/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.model.alumnos;
import java.time.LocalDate;
/**
 *
 * @author Mar
 */
public class Calificacion {
       private Integer id;
    private Integer estudianteId; // FK a estudiantes.id
    private String materia;
    private Double nota;
    private LocalDate fecha;

    public Calificacion() { }

    public Calificacion(Integer id, Integer estudianteId, String materia, Double nota, LocalDate fecha) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.materia = materia;
        this.nota = nota;
        this.fecha = fecha;
    }

    public Calificacion(Integer estudianteId, String materia, Double nota, LocalDate fecha) {
        this(null, estudianteId, materia, nota, fecha);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
