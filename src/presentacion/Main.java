package presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import usuarios.Cliente;
import usuarios.Organizador;
import usuarios.Administrador;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {

            Object usuarioActivo = mostrarDialogoLogin();
            if (usuarioActivo == null) {
                System.exit(0);
                return;
            }

            Administrador adminGlobal;

            if (usuarioActivo instanceof Administrador) {
                adminGlobal = (Administrador) usuarioActivo;
            } else {

                adminGlobal = new Administrador(
                        "adminGlobal",
                        "123",
                        "ADM000",
                        "Administrador General"
                );
            }

            FPrincipal principal = new FPrincipal(adminGlobal);

            principal.setUsuarioActivo(usuarioActivo);

            principal.setVisible(true);
        });
    }

    
    private static Object mostrarDialogoLogin() {

        String[] roles = {"Cliente", "Organizador", "Administrador"};
        JComboBox<String> cmbRol = new JComboBox<>(roles);

        JTextField txtLogin = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Seleccione su Rol:"));
        panel.add(cmbRol);

        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);

        panel.add(new JLabel("Contraseña (usar '123'):"));
        panel.add(txtPassword);

        int result = JOptionPane.showConfirmDialog(
                null, panel,
                "BoletaMaster: Inicio de Sesión",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION)
            return null;

        String rol = (String) cmbRol.getSelectedItem();
        String login = txtLogin.getText();
        String password = new String(txtPassword.getPassword());

        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Login y contraseña son obligatorios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (!password.equals("123")) {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return switch (rol) {
            case "Administrador" -> new Administrador(login, password, "ADM001", "Admin Central");
            case "Organizador" -> new Organizador(login, password);
            case "Cliente" -> new Cliente(login, password);
            default -> null;
        };
    }
}
