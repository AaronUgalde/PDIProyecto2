package Operations.HistogramOperations;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class UniformEqualization extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // ---------------------------------------------------------
        // Paso 1: Obtener dimensiones y matriz de la imagen original
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        double[][][] matrix = originalImage.getMatrix();

        // ---------------------------------------------------------
        // Paso 2: Convertir la imagen a escala de grises (rango 0-255) usando promedio de canales
        int[][] grayImage = new int[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int gray = (int) Math.round(((matrix[i][j][0] + matrix[i][j][1] + matrix[i][j][2]) / 3.0) * 255);
                grayImage[i][j] = gray;
            }
        }

        // ---------------------------------------------------------
        // Paso 3: Calcular el histograma de la imagen en escala de grises
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                histogram[grayImage[i][j]]++;
            }
        }

        // ---------------------------------------------------------
        // Paso 4: Calcular la Función de Distribución Acumulada (CDF)
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++){
            cdf[i] = cdf[i - 1] + histogram[i];
        }
        int totalPixels = width * height;
        // Buscar el primer valor no nulo en la CDF (cdfMin)
        int cdfMin = 0;
        for (int i = 0; i < 256; i++){
            if (cdf[i] != 0) {
                cdfMin = cdf[i];
                break;
            }
        }

        // ---------------------------------------------------------
        // Paso 5: Calcular la LUT usando el modelo Uniforme
        // Se calcula el valor transformado para cada nivel con la función de transferencia:
        // f(i) = p * 255
        // donde p (densidad acumulada normalizada) se calcula como:
        // p = (cdf[i] - cdfMin) / (totalPixels - cdfMin)
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++){
            double p = (double)(cdf[i] - cdfMin) / (totalPixels - cdfMin); // Densidad de probabilidad acumulada
            double f = p * 255;  // Función de transferencia: modelo uniforme
            lut[i] = (int) Math.round(f);
            if(lut[i] < 0) lut[i] = 0;
            if(lut[i] > 255) lut[i] = 255;
        }

        // ---------------------------------------------------------
        // Paso 6: Aplicar la LUT a la imagen en escala de grises y reconstruir la imagen RGB
        double[][][] resultMatrix = new double[height][width][3];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int newGray = lut[grayImage[i][j]];
                double normalizedGray = newGray / 255.0;
                resultMatrix[i][j][0] = normalizedGray;
                resultMatrix[i][j][1] = normalizedGray;
                resultMatrix[i][j][2] = normalizedGray;
            }
        }

        ImageMatrix resultImage = new ImageMatrix(resultMatrix, TypeOfImage.RGB);
        BufferedImage resultBuffered = resultImage.toBufferedImage();
        JPanel panel = createPanelFromImage(resultBuffered);
        return new Result(panel, resultImage);
    }

    private JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
