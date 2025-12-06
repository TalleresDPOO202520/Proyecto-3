package utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class Qr {

    private static final int QR_SIZE = 250;
    private static final String CHARSET = "UTF-8";

    public static boolean crearCodigoQR(String datosParaQR, String rutaArchivo) {
        
      
        Map<EncodeHintType, Object> hints = new HashMap<>();
      
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); 

        try {

            QRCodeWriter writer = new QRCodeWriter();
            
            BitMatrix bitMatrix = writer.encode(
                    datosParaQR, 
                    BarcodeFormat.QR_CODE, 
                    QR_SIZE, 
                    QR_SIZE, 
                    hints
            );

    
            MatrixToImageWriter.writeToPath(
                bitMatrix, 
                "PNG", 
                Paths.get(rutaArchivo)
            );
            
            System.out.println("QR generado exitosamente en: " + rutaArchivo);
            return true;

        } catch (WriterException e) {
         
            System.err.println("Error de codificación (ZXing): " + e.getMessage());
            return false;
        } catch (IOException e) {
     
            System.err.println("Error de E/S al guardar el archivo: " + e.getMessage());
            return false;
        }
    }
  
}