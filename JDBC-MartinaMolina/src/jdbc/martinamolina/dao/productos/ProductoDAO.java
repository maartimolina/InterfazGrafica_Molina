/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.dao.productos;
import java.util.List;
import jdbc.martinamolina.model.productos.Producto;
/**
 *
 * @author Mar
 */
public interface ProductoDAO {
    int crear(Producto p);
    int actualizar(Producto p);
    int eliminar(int id);

    List<Producto> listarTodos();             // sin JOIN
    List<Producto> listarConCategoria();      // con JOIN (incluye categoriaNombre)
}
