/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.*;
import service.ProductoService;
import service.PedidoService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClienteUI extends JFrame {
    private Cliente cliente;
    private ProductoService productoService;
    private PedidoService pedidoService;
    
    private JTabbedPane tabbedPane;
    private JTable tablaProductos;
    private DefaultTableModel modelProductos;
    private JTable tablaCarrito;
    private DefaultTableModel modelCarrito;
    private JTable tablaPedidos;
    private DefaultTableModel modelPedidos;
    private JLabel lblTotalCarrito;
    
    public ClienteUI(Cliente cliente) {
        this.cliente = cliente;
        this.productoService = new ProductoService();
        this.pedidoService = new PedidoService();
        initComponents();
        cargarProductos();
        actualizarCarritoUI();
        cargarHistorialPedidos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("E-Commerce - Bienvenido " + cliente.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        
        tabbedPane = new JTabbedPane();
        
        // Panel de productos
        tabbedPane.addTab("Productos", crearPanelProductos());
        
        // Panel de carrito
        tabbedPane.addTab("Mi Carrito", crearPanelCarrito());
        
        // Panel de historial
        tabbedPane.addTab("Mis Pedidos", crearPanelHistorial());
        
        // Panel de perfil
        tabbedPane.addTab("Mi Perfil", crearPanelPerfil());
        
        add(tabbedPane);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSesion();
            }
        });
    }
    
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Tipo", "Precio", "Stock", "Acción"};
        modelProductos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        tablaProductos = new JTable(modelProductos);
        
        // Botón de compra en cada fila
        tablaProductos.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaProductos.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scroll = new JScrollPane(tablaProductos);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarProductos());
        panelFiltros.add(btnRefrescar);
        panel.add(panelFiltros, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void cargarProductos() {
        modelProductos.setRowCount(0);
        try {
            List<Producto> productos = productoService.listarTodos();
            for (Producto p : productos) {
                modelProductos.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    "$" + p.getPrecio(),
                    p.getStock(),
                    "Comprar"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
        }
    }
    
    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabla del carrito
        String[] columnas = {"Producto", "Precio", "Cantidad", "Subtotal", "Acción"};
        modelCarrito = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        tablaCarrito = new JTable(modelCarrito);
        tablaCarrito.getColumn("Acción").setCellRenderer(new ButtonRendererEliminar());
        tablaCarrito.getColumn("Acción").setCellEditor(new ButtonEditorEliminar(new JCheckBox()));
        
        JScrollPane scroll = new JScrollPane(tablaCarrito);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblTotalCarrito = new JLabel("Total: $0.0");
        lblTotalCarrito.setFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.add(lblTotalCarrito, BorderLayout.WEST);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnVaciar = new JButton("Vaciar Carrito");
        JButton btnComprar = new JButton("Procesar Compra");
        
        btnVaciar.addActionListener(e -> vaciarCarrito());
        btnComprar.addActionListener(e -> procesarCompra());
        
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        panelInferior.add(panelBotones, BorderLayout.EAST);
        
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarCarritoUI() {
        modelCarrito.setRowCount(0);
        for (ItemCarrito item : cliente.getCarrito().getItems()) {
            modelCarrito.addRow(new Object[]{
                item.getProducto().getNombre(),
                "$" + item.getProducto().getPrecio(),
                item.getCantidad(),
                "$" + item.getSubtotal(),
                "Eliminar"
            });
        }
        lblTotalCarrito.setText("Total: $" + cliente.getCarrito().getTotal());
    }
    
    private void vaciarCarrito() {
        cliente.getCarrito().vaciar();
        actualizarCarritoUI();
    }
    
    private void procesarCompra() {
        if (cliente.getCarrito().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío");
            return;
        }
        
        String[] metodosPago = {"Tarjeta de Crédito", "PayPal", "Transferencia Bancaria", "Contraentrega"};
        String metodo = (String) JOptionPane.showInputDialog(this, 
            "Seleccione método de pago:", "Pago", 
            JOptionPane.QUESTION_MESSAGE, null, metodosPago, metodosPago[0]);
        
        if (metodo == null) return;
        
        try {
            Pedido pedido = pedidoService.crearPedido(cliente, metodo);
            JOptionPane.showMessageDialog(this, "¡Pedido creado con éxito!\nNúmero de pedido: " + pedido.getId());
            actualizarCarritoUI();
            cargarHistorialPedidos();
            cargarProductos(); // Refrescar stock
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar compra: " + ex.getMessage());
        }
    }
    
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID", "Fecha", "Total", "Estado", "Método Pago"};
        modelPedidos = new DefaultTableModel(columnas, 0);
        tablaPedidos = new JTable(modelPedidos);
        
        JScrollPane scroll = new JScrollPane(tablaPedidos);
        panel.add(scroll, BorderLayout.CENTER);
        
        JButton btnVerDetalle = new JButton("Ver Detalle del Pedido");
        btnVerDetalle.addActionListener(e -> verDetallePedido());
        panel.add(btnVerDetalle, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void cargarHistorialPedidos() {
        modelPedidos.setRowCount(0);
        try {
            List<Pedido> pedidos = pedidoService.listarPedidosCliente(cliente.getId());
            for (Pedido p : pedidos) {
                modelPedidos.addRow(new Object[]{
                    p.getId(),
                    p.getFecha(),
                    "$" + p.getTotal(),
                    p.getEstado(),
                    p.getMetodoPago()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }
    
    private void verDetallePedido() {
        int row = tablaPedidos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido");
            return;
        }
        
        String pedidoId = (String) modelPedidos.getValueAt(row, 0);
        try {
            List<Pedido> pedidos = pedidoService.listarPedidosCliente(cliente.getId());
            Pedido pedido = pedidos.stream().filter(p -> p.getId().equals(pedidoId)).findFirst().orElse(null);
            
            if (pedido != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Detalle del Pedido #").append(pedido.getId()).append("\n\n");
                sb.append("Fecha: ").append(pedido.getFecha()).append("\n");
                sb.append("Estado: ").append(pedido.getEstado()).append("\n");
                sb.append("Método de pago: ").append(pedido.getMetodoPago()).append("\n\n");
                sb.append("Productos:\n");
                sb.append("----------------------------------------\n");
                for (ItemCarrito item : pedido.getItems()) {
                    sb.append(item.getProducto().getNombre())
                      .append(" x").append(item.getCantidad())
                      .append(" = $").append(item.getSubtotal()).append("\n");
                }
                sb.append("----------------------------------------\n");
                sb.append("TOTAL: $").append(pedido.getTotal()).append("\n");
                
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Detalle del Pedido", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
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
        panel.add(new JLabel(cliente.getNombre()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(cliente.getEmail()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(cliente.getRol()), gbc);
        y++;
        
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panel.add(btnCerrarSesion, gbc);
        
        return panel;
    }
    
    private void cerrarSesion() {
        new LoginUI().setVisible(true);
        dispose();
    }
    
    // Renderer para botón Comprar
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            if ("Comprar".equals(label)) {
                String id = (String) table.getValueAt(row, 0);
                String cantidadStr = JOptionPane.showInputDialog(ClienteUI.this, "¿Cuántas unidades desea comprar?");
                if (cantidadStr != null) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        agregarAlCarrito(id, cantidad);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ClienteUI.this, "Cantidad inválida");
                    }
                }
            }
            return button;
        }
        
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
    }
    
    private void agregarAlCarrito(String productoId, int cantidad) {
        try {
            Producto p = productoService.buscarPorId(productoId);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado");
                return;
            }
            cliente.getCarrito().agregarProducto(p, cantidad);
            actualizarCarritoUI();
            JOptionPane.showMessageDialog(this, "Producto agregado al carrito");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    // Renderer para botón Eliminar
    class ButtonRendererEliminar extends JButton implements TableCellRenderer {
        public ButtonRendererEliminar() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditorEliminar extends DefaultCellEditor {
        protected JButton button;
        private String label;
        
        public ButtonEditorEliminar(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                if ("Eliminar".equals(label)) {
                    int row = tablaCarrito.getSelectedRow();
                    if (row >= 0) {
                        String productoNombre = (String) modelCarrito.getValueAt(row, 0);
                        ItemCarrito item = cliente.getCarrito().getItems().get(row);
                        cliente.getCarrito().eliminarProducto(item.getProducto().getId());
                        actualizarCarritoUI();
                        JOptionPane.showMessageDialog(ClienteUI.this, "Producto eliminado: " + productoNombre);
                    }
                }
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }
        
        public Object getCellEditorValue() {
            return label;
        }
    }
}