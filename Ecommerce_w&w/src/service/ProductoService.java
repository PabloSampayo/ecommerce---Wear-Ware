/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ProductoDAO;
import model.*;
import java.io.IOException;
import java.util.List;

public class ProductoService {
    private ProductoDAO productoDAO;
    
    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }
    
    public List<Producto> listarTodos() throws IOException {
        return productoDAO.listarTodos();
    }
    
    public Producto buscarPorId(String id) throws IOException {
        return productoDAO.buscarPorId(id);
    }
    
    public void crearProductoElectronico(String nombre, double precio, int stock, int garantiaMeses, String marca) throws IOException {
        ProductoElectronico producto = new ProductoElectronico(null, nombre, precio, stock, garantiaMeses, marca);
        productoDAO.guardarProducto(producto);
    }
    
    public void crearProductoRopa(String nombre, double precio, int stock, String talla, String color) throws IOException {
        ProductoRopa producto = new ProductoRopa(null, nombre, precio, stock, talla, color);
        productoDAO.guardarProducto(producto);
    }
    
    public void actualizarProducto(Producto producto) throws IOException {
        productoDAO.guardarProducto(producto);
    }
    
    public void eliminarProducto(String id) throws IOException {
        productoDAO.eliminarProducto(id);
    }
    
    public boolean validarStock(String id, int cantidadRequerida) throws IOException {
        Producto p = productoDAO.buscarPorId(id);
        return p != null && p.getStock() >= cantidadRequerida;
    }
}