/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.daoimpl.productos;
import jdbc.martinamolina.dao.productos.CategoriaDAO;
import jdbc.martinamolina.model.productos.Categoria;
import jdbc.martinamolina.dbConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mar
 */
public class CategoriaDAOImpl implements CategoriaDAO {
    @Override
    public List<Categoria> listarTodas() {
        String sql = "SELECT id, nombre FROM categorias ORDER BY nombre";
        List<Categoria> out = new ArrayList<>();
        try (Connection c = dbConexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error listar categorias: " + e.getMessage());
        }
        return out;
    }
}
