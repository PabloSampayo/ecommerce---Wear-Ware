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

public class UsuarioDAO {
    private static final String ARCHIVO = "data/usuarios.dat";
    
    public void guardarUsuario(Usuario usuario) throws IOException {
        List<Usuario> usuarios = listarTodos();
        if (usuario.getId() == null || usuario.getId().isEmpty()) {
            usuario.setId(UUID.randomUUID().toString());
        }
        // Reemplazar si ya existe
        boolean existe = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                existe = true;
                break;
            }
        }
        if (!existe) {
            usuarios.add(usuario);
        }
        FileManager.guardarLista(usuarios, ARCHIVO);
    }
    
    @SuppressWarnings("unchecked")
    public List<Usuario> listarTodos() throws IOException {
        try {
            return FileManager.cargarLista(ARCHIVO);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    public Usuario buscarPorEmail(String email) throws IOException {
        for (Usuario u : listarTodos()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
    
    public Usuario buscarPorId(String id) throws IOException {
        for (Usuario u : listarTodos()) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
    
    public void eliminarUsuario(String id) throws IOException {
        List<Usuario> usuarios = listarTodos();
        usuarios.removeIf(u -> u.getId().equals(id));
        FileManager.guardarLista(usuarios, ARCHIVO);
    }
}