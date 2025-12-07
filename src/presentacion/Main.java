package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Importaciones de tus clases de usuarios
import usuarios.Cliente;
import usuarios.Organizador;
import usuarios.Administrador;

public class Main {
    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            
            // Intentamos obtener el usuario y el rol del diálogo de login
            Object usuarioActivo = mostrarDialogoLogin();
            
            // Si el usuario cancela o el login falla, cerramos
            if (usuarioActivo == null) {
                System.exit(0);
                return;
            }

            // LANZAR LA APLICACIÓN PRINCIPAL
            
            FPrincipal principal = new FPrincipal();
            // setUsuarioActivo existe en FPrincipal.java
            principal.setUsuarioActivo(usuarioActivo); 
            principal.setVisible(true);
        });
    }

    /**
     * Muestra el cuadro de diálogo unificado para seleccionar Rol, Login y Password.
     * @return Objeto Cliente, Organizador, o Administrador si el login es exitoso; null si falla o cancela.
     */
    private static Object mostrarDialogoLogin() {
        // Componentes de entrada
        String[] roles = {"Cliente", "Organizador", "Administrador"};
        JComboBox<String> cmbRol = new JComboBox<>(roles);
        JTextField txtLogin = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        
        // 1. Panel que contiene todos los campos (Layout tipo columna)
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Seleccione su Rol:"));
        panel.add(cmbRol);
        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Contraseña (Usar '123'):"));
        panel.add(txtPassword);

        // 2. Mostrar el diálogo y esperar la acción del usuario
        int result = JOptionPane.showConfirmDialog(null, panel, 
                "BoletaMaster: Inicio de Sesión", JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String rol = (String) cmbRol.getSelectedItem();
            String login = txtLogin.getText();
            String password = new String(txtPassword.getPassword());
            
            // 3. VALIDACIÓN (Simulación)
            if (login.trim().isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Login y contraseña son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            if (!password.equals("123")) {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // 4. CREACIÓN DEL OBJETO DE USUARIO (Reutilizando tus constructores)
            return switch (rol) {
                case "Administrador" -> new Administrador(login, password, "ADM001", "Admin Central");
                case "Organizador" -> new Organizador(login, password);
                case "Cliente" -> new Cliente(login, password);
                default -> null;
            };
        }
        return null; // El usuario presionó Cancelar o cerró la ventana
    }
}