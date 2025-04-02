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
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        double[][][] matrix = originalImage.getMatrix();

        // Convertir a escala de grises (promedio de R, G y B) y convertir a niveles 0-255
        int[][] grayImage = new int[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int gray = (int)Math.round(((matrix[i][j][0] + matrix[i][j][1] + matrix[i][j][2]) / 3.0) * 255);
                grayImage[i][j] = gray;
            }
        }

        // Calcular el histograma
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                histogram[grayImage[i][j]]++;
            }
        }

        // Calcular la CDF
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++){
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        // Encontrar el valor mínimo no cero de la CDF
        int cdfMin = 0;
        for (int i = 0; i < 256; i++){
            if (cdf[i] != 0){
                cdfMin = cdf[i];
                break;
            }
        }

        int totalPixels = width * height;
        int[] transform = new int[256];
        // Crear tabla de transformación
        for (int i = 0; i < 256; i++){
            transform[i] = (int)Math.round(((double)(cdf[i] - cdfMin) / (totalPixels - cdfMin)) * 255);
            transform[i] = Math.min(255, Math.max(0, transform[i]));
        }

        // Crear la imagen resultante en escala de grises (normalizada)
        double[][][] resultMatrix = new double[height][width][3];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int newGray = transform[grayImage[i][j]];
                double normGray = newGray / 255.0;
                resultMatrix[i][j][0] = normGray;
                resultMatrix[i][j][1] = normGray;
                resultMatrix[i][j][2] = normGray;
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
