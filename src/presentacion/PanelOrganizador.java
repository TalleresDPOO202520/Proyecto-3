// src/presentacion/PanelOrganizador.java
package presentacion;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelOrganizador extends JPanel {
    public PanelOrganizador() {
        add(new JLabel("Opciones Organizador: Aquí se crean Eventos y Venues.", SwingConstants.CENTER));
    }
}