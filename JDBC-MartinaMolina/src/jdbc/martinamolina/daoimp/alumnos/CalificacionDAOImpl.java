/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.daoimp.alumnos;

import jdbc.martinamolina.dao.alumnos.CalificacionDAO;
import jdbc.martinamolina.model.alumnos.Calificacion;
import jdbc.martinamolina.dbConexion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Mar
 */
public class CalificacionDAOImpl implements CalificacionDAO {

    private Calificacion map(ResultSet rs) throws SQLException {
        Calificacion c = new Calificacion();
        c.setId(rs.getInt("id"));
        c.setEstudianteId(rs.getInt("estudiante_id"));
        c.setMateria(rs.getString("materia"));
        c.setNota(rs.getDouble("nota"));
        Date f = rs.getDate("fecha");
        c.setFecha(f != null ? f.toLocalDate() : null);
        return c;
    }

    // ======= MÉTODOS DE LA INTERFAZ =======

    @Override
    public List<Calificacion> listByEstudiante(int estudianteId) {
        String sql = """
                SELECT id, estudiante_id, materia, nota, fecha
                FROM calificaciones
                WHERE estudiante_id = ?
                ORDER BY fecha DESC, id DESC
                """;

        List<Calificacion> out = new ArrayList<>();

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudianteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    @Override
    public int insert(Calificacion c) {
        String sql = "INSERT INTO calificaciones(estudiante_id, materia, nota, fecha) VALUES(?,?,?,?)";

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getEstudianteId());
            ps.setString(2, c.getMateria());
            ps.setDouble(3, c.getNota());

            if (c.getFecha() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(c.getFecha()));
            }

            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) return gk.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;   // error
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM calificaciones WHERE id = ?";

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Optional<Double> promedioDeEstudiante(int estudianteId) {
        String sql = "SELECT AVG(nota) FROM calificaciones WHERE estudiante_id = ?";

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudianteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.math.BigDecimal bd = rs.getBigDecimal(1);
                    if (bd != null) return Optional.of(bd.doubleValue());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // ======= MÉTODOS EXTRA (opcionales, por si los querés usar) =======

    /** Actualizar una calificación existente */
    public boolean update(Calificacion c) {
        String sql = "UPDATE calificaciones SET materia = ?, nota = ?, fecha = ? WHERE id = ?";

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getMateria());
            ps.setDouble(2, c.getNota());

            if (c.getFecha() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(c.getFecha()));
            }

            ps.setInt(4, c.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Buscar calificaciones por materia y rango de fechas (no obligatorio para el TP) */
    public List<Calificacion> buscar(String materiaLike, LocalDate desde, LocalDate hasta) {
        StringBuilder sb = new StringBuilder("""
                SELECT id, estudiante_id, materia, nota, fecha
                FROM calificaciones
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (materiaLike != null && !materiaLike.isBlank()) {
            sb.append(" AND materia LIKE ?");
            params.add("%" + materiaLike.trim() + "%");
        }

        if (desde != null) {
            sb.append(" AND fecha >= ?");
            params.add(Date.valueOf(desde));
        }

        if (hasta != null) {
            sb.append(" AND fecha <= ?");
            params.add(Date.valueOf(hasta));
        }

        sb.append(" ORDER BY fecha DESC, id DESC");

        List<Calificacion> out = new ArrayList<>();

        try (Connection con = dbConexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }
}
