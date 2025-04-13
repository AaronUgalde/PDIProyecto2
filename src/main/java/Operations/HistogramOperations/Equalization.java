package Operations.HistogramOperations;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class Equalization extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // 1. Obtener las dimensiones y la matriz de la imagen original
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        double[][][] matrix = originalImage.getMatrix();

        // 2. Convertir la imagen a escala de grises mediante el promedio de los canales.
        // Se multiplica por 255 para trabajar en el rango [0, 255]
        int[][] grayImage = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int gray = (int) Math.round(((matrix[i][j][0] + matrix[i][j][1] + matrix[i][j][2]) / 3.0) * 255);
                grayImage[i][j] = gray;
            }
        }

        // 3. Calcular el histograma de la imagen en escala de grises.
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                histogram[grayImage[i][j]]++;
            }
        }

        // 4. Calcular la función de distribución acumulada (CDF)
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }
        int totalPixels = width * height;

        // Para evitar problemas con los valores muy bajos, se obtiene cdfMin (primer valor no nulo)
        int cdfMin = 0;
        for (int i = 0; i < 256; i++) {
            if (cdf[i] != 0) {
                cdfMin = cdf[i];
                break;
            }
        }

        // 5. Crear la tabla de mapeo (LUT) utilizando la fórmula de ecualización:
        //    f(i) = round( ((cdf(i) - cdfMin) / (totalPixels - cdfMin)) * (L - 1) )
        //    donde L = 256 en este caso.
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = (int) Math.round(((double)(cdf[i] - cdfMin) / (totalPixels - cdfMin)) * 255);
            // Asegurarse de que el mapeo esté dentro del rango [0, 255]
            if (lut[i] < 0) {
                lut[i] = 0;
            }
            if (lut[i] > 255) {
                lut[i] = 255;
            }
        }

        // 6. Aplicar la tabla de mapeo a la imagen en escala de grises y reconstruir una imagen RGB.
        // Se crea una nueva matriz con la imagen ecualizada (todos los canales toman el mismo valor para mantener la escala de grises).
        double[][][] resultMatrix = new double[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int newGray = lut[grayImage[i][j]];
                double normalizedGray = newGray / 255.0;
                resultMatrix[i][j][0] = normalizedGray;
                resultMatrix[i][j][1] = normalizedGray;
                resultMatrix[i][j][2] = normalizedGray;
            }
        }

        // 7. Crear la imagen resultante y el panel para la interfaz gráfica.
        ImageMatrix resultImage = new ImageMatrix(resultMatrix, TypeOfImage.RGB);
        BufferedImage resultBuffered = resultImage.toBufferedImage();
        JPanel panel = createPanelFromImage(resultBuffered);
        return new Result(panel, resultImage);
    }

    // Método auxiliar para crear un panel con la imagen a mostrar
    private JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
