package Operations.HistogramOperations;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class HyperbolicLogEqualization extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // ---------------------------------------------------------
        // Paso 1: Obtener dimensiones y matriz de la imagen original
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        double[][][] matrix = originalImage.getMatrix();

        // ---------------------------------------------------------
        // Paso 2: Convertir la imagen a escala de grises (0-255)
        int[][] grayImage = new int[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int gray = (int) Math.round(((matrix[i][j][0] + matrix[i][j][1] + matrix[i][j][2]) / 3.0) * 255);
                grayImage[i][j] = gray;
            }
        }

        // ---------------------------------------------------------
        // Paso 3: Calcular el histograma
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                histogram[grayImage[i][j]]++;
            }
        }

        // ---------------------------------------------------------
        // Paso 4: Calcular la CDF y cdfMin
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++){
            cdf[i] = cdf[i-1] + histogram[i];
        }
        int totalPixels = width * height;
        int cdfMin = 0;
        for (int i = 0; i < 256; i++){
            if(cdf[i] != 0){
                cdfMin = cdf[i];
                break;
            }
        }

        // ---------------------------------------------------------
        // Paso 5: Construir la LUT usando el modelo Hiperbólico Logarítmico.
        // Se calcula la densidad de probabilidad acumulada:
        // p = (cdf[i] - cdfMin) / (totalPixels - cdfMin)
        // La función de transferencia es:
        // f(i) = 255 * ln(1 + p*(e-1))
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++){
            double p = (double)(cdf[i] - cdfMin) / (totalPixels - cdfMin);
            if(p >= 1.0){
                p = 0.999999;
            }
            double f = 255 * Math.log(1 + p * (Math.E - 1)); // ln(e) = 1, por lo que se omite la división
            int newVal = (int) Math.round(f);
            if(newVal < 0) newVal = 0;
            if(newVal > 255) newVal = 255;
            lut[i] = newVal;
        }

        // ---------------------------------------------------------
        // Paso 6: Aplicar la LUT y reconstruir la imagen RGB
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

    public JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
