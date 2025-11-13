/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.controller.productos;
import jdbc.martinamolina.dao.productos.*;
import jdbc.martinamolina.daoimpl.productos.*;
import jdbc.martinamolina.model.productos.*;
import jdbc.martinamolina.view.productos.ProductosView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author Mar
 */
public class ProductoController {
    private final ProductosView view;
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    public ProductoController(ProductosView view) {
        this.view = view;

        cargarCategorias();
        wireEvents();
        refrescarTabla();

        view.setVisible(true);
    }

    // ===============================
    // Cargar categorías al combo box
    // ===============================
    private void cargarCategorias() {
        DefaultComboBoxModel<ProductosView.CategoriaItem> model = new DefaultComboBoxModel<>();
        for (Categoria c : categoriaDAO.listarTodas()) {
            model.addElement(new ProductosView.CategoriaItem(c.getId(), c.getNombre()));
        }
        view.getCmbCategoria().setModel(model);
    }

    // ===============================
    // Refrescar tabla
    // ===============================
    private void refrescarTabla() {
        cargarTabla(productoDAO.listarConCategoria());
    }

    private void cargarTabla(List<Producto> data) {
        DefaultTableModel m = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoría"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Producto p : data) {
            m.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoriaNombre()
            });
        }
        view.getTblProductos().setModel(m);
    }

    // ===============================
    // Tomar datos del formulario
    // ===============================
    private Producto tomarDelForm(boolean conId) {
        Producto p = new Producto();

        if (conId && !view.getTxtId().getText().isBlank()) {
            p.setId(Integer.parseInt(view.getTxtId().getText().trim()));
        }

        p.setNombre(view.getTxtNombre().getText().trim());

        Number pr = (Number) view.getTxtPrecio().getValue();
        Number st = (Number) view.getTxtStock().getValue();

        p.setPrecio(pr == null ? BigDecimal.ZERO : new BigDecimal(pr.toString()));
        p.setStock(st == null ? 0 : st.intValue());

        var item = (ProductosView.CategoriaItem) view.getCmbCategoria().getSelectedItem();
        p.setCategoriaId(item == null ? 0 : item.id);

        return p;
    }

    // ===============================
    // Asignar eventos de botones
    // ===============================
    private void wireEvents() {
        // Refrescar
    view.getBtnRefrescar().addActionListener(e -> refrescarTabla());

    // Nuevo
    view.getBtnNuevo().addActionListener(e -> limpiarForm());

    // Guardar (único listener)
    view.getBtnGuardar().addActionListener(e -> {
        var item = (ProductosView.CategoriaItem) view.getCmbCategoria().getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(view, "Elegí una categoría antes de guardar.",
                    "Falta categoría", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (view.getTxtNombre().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El nombre no puede estar vacío.",
                    "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (productoDAO.crear(tomarDelForm(false)) > 0) {
            refrescarTabla();
            limpiarForm();
        }
    });

    // Actualizar
    view.getBtnActualizar().addActionListener(e -> {
        if (productoDAO.actualizar(tomarDelForm(true)) > 0) {
            refrescarTabla();
            limpiarForm();
        }
    });

    // Eliminar
    view.getBtnEliminar().addActionListener(e -> {
        String idTxt = view.getTxtId().getText().trim();
        if (!idTxt.isEmpty() && productoDAO.eliminar(Integer.parseInt(idTxt)) > 0) {
            refrescarTabla();
            limpiarForm();
        }
    });

    // Selección de tabla -> pasar a formulario (evita dobles eventos)
    view.getTblProductos().getSelectionModel().addListSelectionListener(e -> {
        if (e.getValueIsAdjusting()) return;  // <- clave
        int r = view.getTblProductos().getSelectedRow();
        if (r < 0) return;

        JTable t = view.getTblProductos();
        view.getTxtId().setText(t.getValueAt(r, 0).toString());
        view.getTxtNombre().setText(t.getValueAt(r, 1).toString());
        view.getTxtPrecio().setValue(new java.math.BigDecimal(t.getValueAt(r, 2).toString()));
        view.getTxtStock().setValue(Integer.parseInt(t.getValueAt(r, 3).toString()));

        String cat = t.getValueAt(r, 4).toString();
        for (int i = 0; i < view.getCmbCategoria().getItemCount(); i++) {
            var it = view.getCmbCategoria().getItemAt(i);
            if (it.nombre.equals(cat)) { view.getCmbCategoria().setSelectedIndex(i); break; }
        }
    });
    }

    // ===============================
    // Limpiar formulario
    // ===============================
    private void limpiarForm() {
        view.getTxtId().setText("");
        view.getTxtNombre().setText("");
        view.getTxtPrecio().setValue(null);
        view.getTxtStock().setValue(null);

        if (view.getCmbCategoria().getItemCount() > 0) {
            view.getCmbCategoria().setSelectedIndex(0);
        }

        view.getTblProductos().clearSelection();
    }
}
