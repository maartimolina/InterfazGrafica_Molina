/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.dao.biblioteca;
import java.util.List;
import jdbc.martinamolina.model.biblioteca.Libro;
/**
 *
 * @author Mar
 */
public interface LibroDAO {
    
   int crear(Libro l);
    int actualizar(Libro l);
    int eliminar(int id);

    List<Libro> listarTodos();
    List<Libro> buscarPorAutor(String autor);
    List<Libro> listarDisponibles();
}
