package ui;

import javax.swing.*;
import java.awt.*;
import service.AuthService;
import model.*;
import dao.UsuarioDAO;
import utils.FileManager;

public class LoginUI extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private AuthService authService;
    
    public LoginUI() {
        // Crear datos iniciales ANTES de cualquier cosa
        inicializarDatosPrueba();
        
        authService = new AuthService();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void inicializarDatosPrueba() {
        try {
            // Crear carpeta data si no existe
            FileManager.crearDirectorio("data");
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Verificar si ya existe el admin
            Usuario adminExistente = usuarioDAO.buscarPorEmail("admin@ecommerce.com");
            
            if (adminExistente == null) {
                // Crear administrador
                Administrador admin = new Administrador(
                    null, 
                    "Administrador Principal", 
                    "admin@ecommerce.com", 
                    "admin123"
                );
                usuarioDAO.guardarUsuario(admin);
                System.out.println("✅ Administrador creado con éxito");
            }
            
            // Opcional: Crear un cliente de prueba si no existe
            Usuario clienteExistente = usuarioDAO.buscarPorEmail("cliente@test.com");
            if (clienteExistente == null) {
                Cliente cliente = new Cliente(
                    null,
                    "Cliente Test",
                    "cliente@test.com",
                    "1234"
                );
                usuarioDAO.guardarUsuario(cliente);
                System.out.println("✅ Cliente de prueba creado");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error al crear datos iniciales: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        setTitle("Wear/Ware - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        JLabel lblTitulo = new JLabel("Bienvenido a Wear/Ware");
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
        btnRegistro.addActionListener(e -> {
            new RegistroUI().setVisible(true);
            dispose();
        });
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
                JOptionPane.showMessageDialog(this, "Bienvenido Administrador: " + usuario.getNombre());
                new AdminUI((Administrador) usuario).setVisible(true);
            } else if (usuario instanceof Cliente) {
                JOptionPane.showMessageDialog(this, "Bienvenido: " + usuario.getNombre());
                new ClienteUI((Cliente) usuario).setVisible(true);
            }
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}