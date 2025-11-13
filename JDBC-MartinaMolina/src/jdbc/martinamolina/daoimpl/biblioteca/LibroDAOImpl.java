/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.daoimpl.biblioteca;

import jdbc.martinamolina.dao.biblioteca.LibroDAO;
import jdbc.martinamolina.model.biblioteca.Libro;
import jdbc.martinamolina.dbConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mar
 */
public class LibroDAOImpl implements LibroDAO {
    // Helper para mapear una fila a un Libro
    private Libro map(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getInt("id"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setAnioPublicacion(rs.getInt("anio_publicacion"));
        l.setIsbn(rs.getString("isbn"));
        l.setDisponible(rs.getBoolean("disponible"));
        return l;
    }

    @Override
    public int crear(Libro l) {
        String sql = """
            INSERT INTO libros (titulo, autor, anio_publicacion, isbn, disponible)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setInt(3, l.getAnioPublicacion());
            ps.setString(4, l.getIsbn());
            ps.setBoolean(5, l.isDisponible());
            int rows = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) l.setId(keys.getInt(1));
            }
            return rows;
        } catch (SQLException e) {
            System.err.println("Error crear libro: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int actualizar(Libro l) {
        String sql = """
            UPDATE libros
               SET titulo=?, autor=?, anio_publicacion=?, isbn=?, disponible=?
             WHERE id=?
        """;
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setInt(3, l.getAnioPublicacion());
            ps.setString(4, l.getIsbn());
            ps.setBoolean(5, l.isDisponible());
            ps.setInt(6, l.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualizar libro: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id=?";
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminar libro: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<Libro> listarTodos() {
        String sql = "SELECT * FROM libros ORDER BY id";
        List<Libro> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error listar libros: " + e.getMessage());
        }
        return out;
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        String sql = "SELECT * FROM libros WHERE autor LIKE ? ORDER BY titulo";
        List<Libro> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + autor + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error buscar por autor: " + e.getMessage());
        }
        return out;
    }

    @Override
    public List<Libro> listarDisponibles() {
        String sql = "SELECT * FROM libros WHERE disponible = 1 ORDER BY titulo";
        List<Libro> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error listar disponibles: " + e.getMessage());
        }
        return out;
    }
}
