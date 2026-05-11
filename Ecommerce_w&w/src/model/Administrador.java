/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    public Administrador(String id, String nombre, String email, String password) {
        super(id, nombre, email, password);
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }
}