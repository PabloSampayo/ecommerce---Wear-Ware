/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.UsuarioDAO;
import model.*;
import java.io.IOException;

public class AuthService {
    private UsuarioDAO usuarioDAO;
    
    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Usuario login(String email, String password) throws IOException, Exception {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }
        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta");
        }
        return usuario;
    }
    
    public void registrarCliente(String nombre, String email, String password) throws IOException, Exception {
        // Validar que no exista
        if (usuarioDAO.buscarPorEmail(email) != null) {
            throw new Exception("El email ya está registrado");
        }
        
        Cliente cliente = new Cliente(null, nombre, email, password);
        usuarioDAO.guardarUsuario(cliente);
    }
}