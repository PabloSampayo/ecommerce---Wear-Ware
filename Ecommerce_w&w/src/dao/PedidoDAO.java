/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.*;
import utils.FileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PedidoDAO {
    private static final String ARCHIVO = "data/pedidos.dat";
    
    public void guardarPedido(Pedido pedido) throws IOException {
        List<Pedido> pedidos = listarTodos();
        if (pedido.getId() == null || pedido.getId().isEmpty()) {
            pedido.setId(UUID.randomUUID().toString());
        }
        boolean existe = false;
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId().equals(pedido.getId())) {
                pedidos.set(i, pedido);
                existe = true;
                break;
            }
        }
        if (!existe) {
            pedidos.add(pedido);
        }
        FileManager.guardarLista(pedidos, ARCHIVO);
    }
    
    @SuppressWarnings("unchecked")
    public List<Pedido> listarTodos() throws IOException {
        try {
            return FileManager.cargarLista(ARCHIVO);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    public List<Pedido> listarPorCliente(String clienteId) throws IOException {
        return listarTodos().stream()
                .filter(p -> p.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }
    
    public Pedido buscarPorId(String id) throws IOException {
        for (Pedido p : listarTodos()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    public void actualizarEstado(String id, String nuevoEstado) throws IOException {
        Pedido p = buscarPorId(id);
        if (p != null) {
            p.setEstado(nuevoEstado);
            guardarPedido(p);
        }
    }
}