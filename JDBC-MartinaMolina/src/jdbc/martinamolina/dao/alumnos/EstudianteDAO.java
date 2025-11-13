/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.dao.alumnos;
import jdbc.martinamolina.model.alumnos.Estudiante;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Mar
 */
public interface EstudianteDAO {
     List<Estudiante> findAll();
    Optional<Estudiante> findById(int id);
    List<Estudiante> searchByNombreOApellido(String texto);
    int insert(Estudiante e);       // devuelve ID generado
    boolean update(Estudiante e);   // true si afectó 1 fila
    boolean delete(int id);         // true si afectó 1 fila
}
