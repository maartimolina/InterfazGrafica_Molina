/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.dao.alumnos;
import jdbc.martinamolina.model.alumnos.Calificacion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Mar
 */
public interface CalificacionDAO {
      List<Calificacion> listByEstudiante(int estudianteId);
    int insert(Calificacion c);
    boolean delete(int id);
    java.util.Optional<Double> promedioDeEstudiante(int estudianteId);
    
}
