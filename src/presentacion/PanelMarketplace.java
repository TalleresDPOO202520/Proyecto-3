// src/presentacion/PanelMarketplace.java
package presentacion;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelMarketplace extends JPanel {
    public PanelMarketplace() {
        add(new JLabel("Marketplace: Aquí se compran/venden boletas de reventa.", SwingConstants.CENTER));
    }
}