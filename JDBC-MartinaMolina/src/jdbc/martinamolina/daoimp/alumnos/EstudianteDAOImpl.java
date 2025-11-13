/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.daoimp.alumnos;

import jdbc.martinamolina.dao.alumnos.EstudianteDAO;
import jdbc.martinamolina.model.alumnos.Estudiante;
import jdbc.martinamolina.dbConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author Mar
 */
public class EstudianteDAOImpl implements EstudianteDAO {
    private Estudiante map(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setEmail(rs.getString("email"));
        e.setEdad((Integer) rs.getObject("edad")); // permite NULL
        return e;
    }

    @Override
    public List<Estudiante> findAll() {
        String sql = "SELECT id, nombre, apellido, email, edad FROM estudiantes ORDER BY id DESC";
        List<Estudiante> out = new ArrayList<>();
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public Optional<Estudiante> findById(int id) {
        String sql = "SELECT id, nombre, apellido, email, edad FROM estudiantes WHERE id=?";
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Estudiante> searchByNombreOApellido(String texto) {
        String like = "%" + texto.trim() + "%";
        String sql = """
                     SELECT id, nombre, apellido, email, edad
                     FROM estudiantes
                     WHERE nombre LIKE ? OR apellido LIKE ?
                     ORDER BY apellido, nombre
                     """;
        List<Estudiante> out = new ArrayList<>();
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public int insert(Estudiante e) {
        String sql = "INSERT INTO estudiantes(nombre, apellido, email, edad) VALUES(?,?,?,?)";
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            if (e.getEdad() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, e.getEdad());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) return gk.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // error
    }

    @Override
    public boolean update(Estudiante e) {
        String sql = "UPDATE estudiantes SET nombre=?, apellido=?, email=?, edad=? WHERE id=?";
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            if (e.getEdad() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, e.getEdad());
            ps.setInt(5, e.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM estudiantes WHERE id=?";
        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
