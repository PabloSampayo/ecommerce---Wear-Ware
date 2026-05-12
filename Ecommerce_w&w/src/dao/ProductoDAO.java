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
    
public class ProductoDAO {
    private static final String ARCHIVO = "data/productos.dat";
    
    public void guardarProducto(Producto producto) throws IOException {
        List<Producto> productos = listarTodos();
        if (producto.getId() == null || producto.getId().isEmpty()) {
            producto.setId(UUID.randomUUID().toString());
        }
        boolean existe = false;
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(producto.getId())) {
                productos.set(i, producto);
                existe = true;
                break;
            }
        }
        if (!existe) {
            productos.add(producto);
        }
        FileManager.guardarLista(productos, ARCHIVO);
    }
    
    @SuppressWarnings("unchecked")
    public List<Producto> listarTodos() throws IOException {
        try {
            return FileManager.cargarLista(ARCHIVO);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    public Producto buscarPorId(String id) throws IOException {
        for (Producto p : listarTodos()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    public void eliminarProducto(String id) throws IOException {
        List<Producto> productos = listarTodos();
        productos.removeIf(p -> p.getId().equals(id));
        FileManager.guardarLista(productos, ARCHIVO);
    }
    
    public void actualizarStock(String id, int nuevoStock) throws IOException {
        Producto p = buscarPorId(id);
        if (p != null) {
            p.setStock(nuevoStock);
            guardarProducto(p);
        }
    }
}