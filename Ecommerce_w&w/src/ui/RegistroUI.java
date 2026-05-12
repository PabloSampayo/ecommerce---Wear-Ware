/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import service.AuthService;

public class RegistroUI extends JFrame {
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private AuthService authService;
    
    public RegistroUI() {
        authService = new AuthService();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Registro de Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int y = 0;
        
        // Título
        JLabel lblTitulo = new JLabel("Crear cuenta nueva");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        y++;
        
        // Nombre
        gbc.gridwidth = 1;
        gbc.gridy = y;
        gbc.gridx = 0;
        add(new JLabel("Nombre completo:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        add(txtNombre, gbc);
        y++;
        
        // Email
        gbc.gridy = y;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        add(txtEmail, gbc);
        y++;
        
        // Password
        gbc.gridy = y;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);
        y++;
        
        // Confirmar password
        gbc.gridy = y;
        gbc.gridx = 0;
        add(new JLabel("Confirmar contraseña:"), gbc);
        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtConfirmPassword, gbc);
        y++;
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnRegistrar = new JButton("Registrarse");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
        
        // Eventos
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });
    }
    
    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres");
            return;
        }
        
        try {
            authService.registrarCliente(nombre, email, password);
            JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puede iniciar sesión.");
            new LoginUI().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}