/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.*;
import service.ProductoService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminUI extends JFrame {
    private Administrador admin;
    private ProductoService productoService;
    private JTable tablaProductos;
    private DefaultTableModel modelProductos;
    
    public AdminUI(Administrador admin) {
        this.admin = admin;
        this.productoService = new ProductoService();
        initComponents();
        cargarProductos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Panel de Administrador - " + admin.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Gestión de productos
        tabbedPane.addTab("Gestión de Productos", crearPanelProductos());
        
        // Perfil
        tabbedPane.addTab("Mi Perfil", crearPanelPerfil());
        
        add(tabbedPane);
    }
    
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Botones superiores
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregarElectronico = new JButton("Agregar Electrónico");
        JButton btnAgregarRopa = new JButton("Agregar Ropa");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        
        btnAgregarElectronico.addActionListener(e -> agregarProductoElectronico());
        btnAgregarRopa.addActionListener(e -> agregarProductoRopa());
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnEliminar.addActionListener(e -> eliminarProducto());
        
        panelBotones.add(btnAgregarElectronico);
        panelBotones.add(btnAgregarRopa);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Tipo", "Precio", "Stock", "Detalles"};
        modelProductos = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modelProductos);
        JScrollPane scroll = new JScrollPane(tablaProductos);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Panel para modificar stock
        JPanel panelStock = new JPanel(new FlowLayout());
        panelStock.add(new JLabel("Modificar stock - ID producto:"));
        JTextField txtIdStock = new JTextField(15);
        panelStock.add(txtIdStock);
        panelStock.add(new JLabel("Nuevo stock:"));
        JTextField txtNuevoStock = new JTextField(10);
        panelStock.add(txtNuevoStock);
        JButton btnActualizarStock = new JButton("Actualizar Stock");
        
        btnActualizarStock.addActionListener(e -> {
            try {
                String id = txtIdStock.getText().trim();
                int nuevoStock = Integer.parseInt(txtNuevoStock.getText().trim());
                Producto p = productoService.buscarPorId(id);
                if (p != null) {
                    p.setStock(nuevoStock);
                    productoService.actualizarProducto(p);
                    cargarProductos();
                    JOptionPane.showMessageDialog(this, "Stock actualizado");
                } else {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        
        panelStock.add(btnActualizarStock);
        panel.add(panelStock, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void cargarProductos() {
        modelProductos.setRowCount(0);
        try {
            List<Producto> productos = productoService.listarTodos();
            for (Producto p : productos) {
                String detalles = "";
                if (p instanceof ProductoElectronico) {
                    ProductoElectronico pe = (ProductoElectronico) p;
                    detalles = "Marca: " + pe.getMarca() + ", Garantía: " + pe.getGarantiaMeses() + " meses";
                } else if (p instanceof ProductoRopa) {
                    ProductoRopa pr = (ProductoRopa) p;
                    detalles = "Talla: " + pr.getTalla() + ", Color: " + pr.getColor();
                }
                modelProductos.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    "$" + p.getPrecio(),
                    p.getStock(),
                    detalles
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
        }
    }
    
    private void agregarProductoElectronico() {
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtGarantia = new JTextField();
        
        Object[] fields = {
            "Nombre:", txtNombre,
            "Precio:", txtPrecio,
            "Stock:", txtStock,
            "Marca:", txtMarca,
            "Garantía (meses):", txtGarantia
        };
        
        int option = JOptionPane.showConfirmDialog(this, fields, "Agregar Producto Electrónico", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                String marca = txtMarca.getText().trim();
                int garantia = Integer.parseInt(txtGarantia.getText().trim());
                
                productoService.crearProductoElectronico(nombre, precio, stock, garantia, marca);
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void agregarProductoRopa() {
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtTalla = new JTextField();
        JTextField txtColor = new JTextField();
        
        Object[] fields = {
            "Nombre:", txtNombre,
            "Precio:", txtPrecio,
            "Stock:", txtStock,
            "Talla:", txtTalla,
            "Color:", txtColor
        };
        
        int option = JOptionPane.showConfirmDialog(this, fields, "Agregar Producto Ropa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                String talla = txtTalla.getText().trim();
                String color = txtColor.getText().trim();
                
                productoService.crearProductoRopa(nombre, precio, stock, talla, color);
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void eliminarProducto() {
        int row = tablaProductos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
            return;
        }
        
        String id = (String) modelProductos.getValueAt(row, 0);
        String nombre = (String) modelProductos.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el producto: " + nombre + "?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productoService.eliminarProducto(id);
                cargarProductos();
                JOptionPane.showMessageDialog(this, "Producto eliminado");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        int y = 0;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getNombre()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getEmail()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getRol()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });
        panel.add(btnCerrarSesion, gbc);
        
        return panel;
    }
}