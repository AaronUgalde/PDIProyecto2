package Operations.Binarization;

import Model.ImageMatrix;
import Operations.ModelColors.RGB_YIQ.RGB_YIQ;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class BinarizeTwoThresholds extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Convertir la imagen original a YIQ usando la clase RGB_YIQ
        Result yiqResult = new RGB_YIQ().apply(originalImage, new LinkedHashMap<>());
        ImageMatrix yiqImageMatrix = yiqResult.getImageMatrix();

        // Obtener los umbrales de los parámetros
        double threshold1 = (double) params.get("threshold1");
        double threshold2 = (double) params.get("threshold2");

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        double[][][] yiqMatrix = yiqImageMatrix.getMatrix();
        double[][] binMatrix = new double[height][width];

        // Procesamiento de cada píxel utilizando dos umbrales
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                double y = yiqMatrix[i][j][0];
                double binValue;
                if (y < threshold1) {
                    binValue = 0.0;
                } else if (y >= threshold2) {
                    binValue = 1.0;
                } else {
                    // Para valores intermedios se asigna según la cercanía al umbral inferior o superior
                    binValue = ((y - threshold1) < (threshold2 - y)) ? 0.0 : 1.0;
                }
                binMatrix[i][j] = binValue;
            }
        }

        // Actualizamos el canal Y con la imagen binarizada
        yiqImageMatrix.setChannel(0, binMatrix);
        // Se convierte a escala de grises para visualizar la imagen resultante
        JPanel resultPanel = convertToGrayscale(yiqImageMatrix, 0);
        return new Result(resultPanel, yiqImageMatrix);
    }

}
