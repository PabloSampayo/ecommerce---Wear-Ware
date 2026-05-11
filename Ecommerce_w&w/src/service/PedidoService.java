/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.PedidoDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import model.*;
import java.io.IOException;
import java.util.List;

public class PedidoService {
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;
    private UsuarioDAO usuarioDAO;
    
    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Pedido crearPedido(Cliente cliente, String metodoPago) throws IOException, Exception {
        if (cliente.getCarrito().isEmpty()) {
            throw new Exception("El carrito está vacío");
        }
        
        // Validar stock antes de crear pedido
        for (ItemCarrito item : cliente.getCarrito().getItems()) {
            Producto p = productoDAO.buscarPorId(item.getProducto().getId());
            if (p == null) {
                throw new Exception("Producto no encontrado: " + item.getProducto().getNombre());
            }
            if (p.getStock() < item.getCantidad()) {
                throw new Exception("Stock insuficiente para: " + p.getNombre() + ". Disponible: " + p.getStock());
            }
        }
        
        // Crear pedido
        Pedido pedido = new Pedido(null, cliente.getId(), cliente.getCarrito().getItems(), metodoPago);
        pedidoDAO.guardarPedido(pedido);
        
        // Actualizar stock
        for (ItemCarrito item : cliente.getCarrito().getItems()) {
            Producto p = productoDAO.buscarPorId(item.getProducto().getId());
            p.setStock(p.getStock() - item.getCantidad());
            productoDAO.guardarProducto(p);
        }
        
        // Agregar al historial del cliente
        cliente.agregarPedido(pedido);
        usuarioDAO.guardarUsuario(cliente);
        
        // Vaciar carrito
        cliente.getCarrito().vaciar();
        
        return pedido;
    }
    
    public List<Pedido> listarPedidosCliente(String clienteId) throws IOException {
        return pedidoDAO.listarPorCliente(clienteId);
    }
    
    public void actualizarEstadoPedido(String pedidoId, String nuevoEstado) throws IOException {
        pedidoDAO.actualizarEstado(pedidoId, nuevoEstado);
    }
}