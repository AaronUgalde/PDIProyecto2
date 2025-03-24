package Operations.ModelColors.RGB_CMYK;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.ModelColors.RGB_CMY.RGB_CMY;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_CMYK extends ModelColor {

    /**
     * Convierte una imagen de RGB a CMYK.
     *
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters  Mapa de parámetros (no se utilizan en esta operación).
     * @return Un objeto Result que contiene el JPanel con la imagen y el ImageMatrix convertido.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        // Paso 1: Convertir de RGB a CMY primero
        RGB_CMY fromCMY = new RGB_CMY();
        Result cmyResult = fromCMY.apply(imageMatrix, parameters);
        ImageMatrix CMYImageMatrix = cmyResult.getImageMatrix();

        int height = CMYImageMatrix.getHeight();
        int width = CMYImageMatrix.getWidth();
        double[][][] CMYMatrix = CMYImageMatrix.getMatrix();
        double[][][] CMYKMatrix = new double[height][width][4];

        // Paso 2: Convertir de CMY a CMYK
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double c = CMYMatrix[x][y][0];
                double m = CMYMatrix[x][y][1];
                double yC = CMYMatrix[x][y][2];

                double k = Math.min(c, Math.min(m, yC));

                double denominator = (1 - k);
                CMYKMatrix[x][y][0] = (denominator == 0) ? 0 : (c - k) / denominator;
                CMYKMatrix[x][y][1] = (denominator == 0) ? 0 : (m - k) / denominator;
                CMYKMatrix[x][y][2] = (denominator == 0) ? 0 : (yC - k) / denominator;
                CMYKMatrix[x][y][3] = k;
            }
        }

        ImageMatrix resultImageMatrix = new ImageMatrix(CMYKMatrix, TypeOfImage.CMYK);
        JPanel resultPanel = makeJPanel(resultImageMatrix);

        return new Result(resultPanel, resultImageMatrix);
    }
}
