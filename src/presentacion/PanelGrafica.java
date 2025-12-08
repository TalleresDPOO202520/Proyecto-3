package presentacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("serial")
public class PanelGrafica extends JPanel {

   
    private Map<LocalDate, Integer> actividadPorDia;

    public PanelGrafica() {
        this.setPreferredSize(new Dimension(700, 150)); 
        this.setBackground(Color.WHITE);
        this.actividadPorDia = generarDatosSimulados();
    }

    
    private Map<LocalDate, Integer> generarDatosSimulados() {
        Map<LocalDate, Integer> datos = new HashMap<>();
        LocalDate hoy = LocalDate.now();
        Random random = new Random();

      
        for (int i = 0; i < 365; i++) {
            LocalDate fecha = hoy.minusDays(i);
        
            int cantidad = random.nextDouble() > 0.4 ? random.nextInt(5) : 0;
            datos.put(fecha, cantidad);
        }
        return datos;
    }

   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
       
        Graphics2D g2d = (Graphics2D) g;
        
       
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tamanoCuadro = 12;
        int espacio = 3;
        int inicioX = 40;
        int inicioY = 20;

    
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("Actividad del Organizador (Último Año)", inicioX, inicioY - 5);

      
        String[] dias = {"", "Lun", "", "Mie", "", "Vie", ""};
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < 7; i++) {
            if (!dias[i].isEmpty()) {
                g2d.drawString(dias[i], 5, inicioY + (i * (tamanoCuadro + espacio)) + 10);
            }
        }

       
        LocalDate fechaIteracion = LocalDate.now().minusWeeks(52).with(java.time.DayOfWeek.SUNDAY);
        
        for (int semana = 0; semana < 52; semana++) {
            for (int dia = 0; dia < 7; dia++) {
                
                
                int actividad = actividadPorDia.getOrDefault(fechaIteracion, 0);
                
                Color colorCuadro;
                if (actividad == 0) colorCuadro = new Color(235, 237, 240); 
                else if (actividad == 1) colorCuadro = new Color(155, 233, 168); 
                else if (actividad == 2) colorCuadro = new Color(64, 196, 99);   
                else if (actividad == 3) colorCuadro = new Color(48, 161, 78);   
                else colorCuadro = new Color(33, 110, 57);                       

                
                g2d.setColor(colorCuadro);
                int x = inicioX + (semana * (tamanoCuadro + espacio));
                int y = inicioY + (dia * (tamanoCuadro + espacio));
                
                g2d.fillRoundRect(x, y, tamanoCuadro, tamanoCuadro, 2, 2); // Bordes redondeados

                
                fechaIteracion = fechaIteracion.plusDays(1);
                
                
                if (fechaIteracion.isAfter(LocalDate.now())) break;
            }
        }
    }
}
