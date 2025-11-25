package persistencia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Log {

    public static final String RUTA_LOG = "data/marketplace.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void registrar(String tipo, String detalle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LOG, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String linea = "[" + timestamp + "] [" + tipo.toUpperCase() + "] " + detalle;
            writer.write(linea);
            writer.newLine();
        } catch (IOException exeption) {
            System.err.println("Error al escribir en el log: " + exeption.getMessage());
        }
    }
    
    public static List<String> leer() {
        try {
            if (!Files.exists(Paths.get(RUTA_LOG))) {
                return new ArrayList<>();
            }
            return Files.readAllLines(Paths.get(RUTA_LOG));
        } catch (IOException exeption) {
            System.err.println("Error al leer el log: " + exeption.getMessage());
            return new ArrayList<>();
        }
    }
}