/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import service.AuthService;
import model.*;

public class LoginUI extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private AuthService authService;
    
    public LoginUI() {
        authService = new AuthService();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("E-Commerce - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        JLabel lblTitulo = new JLabel("Bienvenido a E-Commerce");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        
        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        add(txtEmail, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrarse");
        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
        
        // Eventos
        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> abrirRegistro());
        
        // Cargar datos de prueba
        cargarDatosPrueba();
    }
    
    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos");
            return;
        }
        
        try {
            Usuario usuario = authService.login(email, password);
            
            if (usuario instanceof Administrador) {
                new AdminUI((Administrador) usuario).setVisible(true);
            } else if (usuario instanceof Cliente) {
                new ClienteUI((Cliente) usuario).setVisible(true);
            }
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void abrirRegistro() {
        new RegistroUI().setVisible(true);
        dispose();
    }
    
    private void cargarDatosPrueba() {
        try {
            // Crear admin por defecto si no existe
            if (authService.login("admin@ecommerce.com", "admin123") == null) {
                // No existe, lo creamos
                dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
                if (usuarioDAO.buscarPorEmail("admin@ecommerce.com") == null) {
                    Administrador admin = new Administrador(null, "Administrador", "admin@ecommerce.com", "admin123");
                    usuarioDAO.guardarUsuario(admin);
                }
            }
        } catch (Exception e) {
            // Ignorar errores de carga de datos
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}