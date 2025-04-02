package Operations.HistogramOperations;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

public class Matching extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el path de la imagen de referencia
        String referencePath = (String) params.get("image");
        BufferedImage refBuffered = ImageIO.read(new File(referencePath));
        if(refBuffered == null) {
            throw new Exception("No se pudo cargar la imagen de referencia desde: " + referencePath);
        }
        ImageMatrix referenceImage = new ImageMatrix(refBuffered);

        int heightSrc = originalImage.getHeight();
        int widthSrc = originalImage.getWidth();
        int heightRef = referenceImage.getHeight();
        int widthRef = referenceImage.getWidth();

        double[][][] srcMatrix = originalImage.getMatrix();
        double[][][] refMatrix = referenceImage.getMatrix();

        // Convertir ambas imágenes a escala de grises (promedio) y a niveles 0-255
        int[][] srcGray = new int[heightSrc][widthSrc];
        for (int i = 0; i < heightSrc; i++){
            for (int j = 0; j < widthSrc; j++){
                int gray = (int)Math.round(((srcMatrix[i][j][0] + srcMatrix[i][j][1] + srcMatrix[i][j][2]) / 3.0) * 255);
                srcGray[i][j] = gray;
            }
        }

        int[][] refGray = new int[heightRef][widthRef];
        for (int i = 0; i < heightRef; i++){
            for (int j = 0; j < widthRef; j++){
                int gray = (int)Math.round(((refMatrix[i][j][0] + refMatrix[i][j][1] + refMatrix[i][j][2]) / 3.0) * 255);
                refGray[i][j] = gray;
            }
        }

        // Calcular histogramas
        int[] histSrc = new int[256];
        int[] histRef = new int[256];
        for (int i = 0; i < heightSrc; i++){
            for (int j = 0; j < widthSrc; j++){
                histSrc[srcGray[i][j]]++;
            }
        }
        for (int i = 0; i < heightRef; i++){
            for (int j = 0; j < widthRef; j++){
                histRef[refGray[i][j]]++;
            }
        }

        // Calcular CDFs
        int[] cdfSrc = new int[256];
        int[] cdfRef = new int[256];
        cdfSrc[0] = histSrc[0];
        cdfRef[0] = histRef[0];
        for (int i = 1; i < 256; i++){
            cdfSrc[i] = cdfSrc[i - 1] + histSrc[i];
            cdfRef[i] = cdfRef[i - 1] + histRef[i];
        }

        int totalSrc = widthSrc * heightSrc;
        int totalRef = widthRef * heightRef;

        // Normalizar las CDFs
        double[] normCdfSrc = new double[256];
        double[] normCdfRef = new double[256];
        for (int i = 0; i < 256; i++){
            normCdfSrc[i] = (double)cdfSrc[i] / totalSrc;
            normCdfRef[i] = (double)cdfRef[i] / totalRef;
        }

        // Crear la tabla de mapeo: para cada nivel de gris en la imagen fuente, se busca el nivel
        // en la imagen de referencia cuya CDF sea la más cercana.
        int[] mapping = new int[256];
        for (int i = 0; i < 256; i++){
            double diff = Double.MAX_VALUE;
            int bestMatch = 0;
            for (int j = 0; j < 256; j++){
                double currentDiff = Math.abs(normCdfSrc[i] - normCdfRef[j]);
                if (currentDiff < diff) {
                    diff = currentDiff;
                    bestMatch = j;
                }
            }
            mapping[i] = bestMatch;
        }

        // Aplicar el mapeo a la imagen fuente y crear una imagen en escala de grises (normalizada)
        double[][][] resultMatrix = new double[heightSrc][widthSrc][3];
        for (int i = 0; i < heightSrc; i++){
            for (int j = 0; j < widthSrc; j++){
                int newGray = mapping[srcGray[i][j]];
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
