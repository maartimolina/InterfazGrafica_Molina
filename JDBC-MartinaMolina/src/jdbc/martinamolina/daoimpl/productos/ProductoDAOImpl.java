/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.daoimpl.productos;
import jdbc.martinamolina.dao.productos.ProductoDAO;
import jdbc.martinamolina.model.productos.Producto;
import jdbc.martinamolina.dbConexion;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mar
 */
public class ProductoDAOImpl implements ProductoDAO  {
    private Producto map(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        return p;
    }

    @Override public int crear(Producto p) {
        String sql = "INSERT INTO productos(nombre, precio, stock, categoria_id) VALUES (?,?,?,?)";
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setBigDecimal(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getCategoriaId());
            int rows = ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) p.setId(k.getInt(1)); }
            return rows;
        } catch (SQLException e) {
            System.err.println("Error crear producto: " + e.getMessage());
            return 0;
        }
    }

    @Override public int actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, precio=?, stock=?, categoria_id=? WHERE id=?";
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setBigDecimal(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getCategoriaId());
            ps.setInt(5, p.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualizar producto: " + e.getMessage());
            return 0;
        }
    }

    @Override public int eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminar producto: " + e.getMessage());
            return 0;
        }
    }

    @Override public List<Producto> listarTodos() {
        String sql = "SELECT id, nombre, precio, stock, categoria_id FROM productos ORDER BY id";
        List<Producto> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error listar productos: " + e.getMessage());
        }
        return out;
    }

    @Override public List<Producto> listarConCategoria() {
        String sql = """
            SELECT p.id, p.nombre, p.precio, p.stock, p.categoria_id, c.nombre AS categoria
            FROM productos p
            JOIN categorias c ON c.id = p.categoria_id
            ORDER BY p.id
        """;
        List<Producto> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = map(rs);
                p.setCategoriaNombre(rs.getString("categoria"));
                out.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error listar con categoria: " + e.getMessage());
        }
        return out;
    }
}
