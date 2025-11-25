package persistencia;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import marketplace.Oferta;

public class Persistencia {

    public static final String RUTA_OFERTAS = "data/ofertas.csv";

    public static void guardarOfertas(Collection<Oferta> ofertas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_OFERTAS))) {
            for (Oferta oferta : ofertas) {
                String linea = String.join(";",
                		oferta.getIdOferta(),
                		oferta.getVendedor().getLogin(),
                        String.valueOf(oferta.getPrecio()),
                        String.valueOf(oferta.getEstado()),
                        String.valueOf(oferta.getTiquetes().size())
                );
                writer.write(linea);
                writer.newLine();
            }
            Log.registrar("PERSISTENCIA", "Ofertas guardadas correctamente en " + RUTA_OFERTAS);
        } catch (IOException exeption) {
            System.err.println("Error al guardar ofertas: " + exeption.getMessage());
        }
    }

    public static List<String> cargarOfertasCrudo() {
        try {
            if (!Files.exists(Paths.get(RUTA_OFERTAS))) {
                return new ArrayList<>();
            }
            return Files.readAllLines(Paths.get(RUTA_OFERTAS));
        } catch (IOException exeption) {
            System.err.println("⚠Error al leer ofertas: " + exeption.getMessage());
            return new ArrayList<>();
        }
    }
}