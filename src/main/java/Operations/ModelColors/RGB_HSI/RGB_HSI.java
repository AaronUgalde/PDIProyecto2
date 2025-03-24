package Operations.ModelColors.RGB_HSI;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

import static java.lang.Math.*;

public class RGB_HSI extends ModelColor {

    /**
     * Convierte una imagen de RGB a HSI.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return Un Result con la imagen convertida a HSI.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] matrix = imageMatrix.getMatrix();
        double[][][] hsi = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double R = matrix[i][j][0];
                double G = matrix[i][j][1];
                double B = matrix[i][j][2];

                // Intensidad
                double I = (R + G + B) / 3.0;

                // Saturación
                double min = Math.min(R, Math.min(G, B));
                double S = (I == 0) ? 0 : 1 - (min / I);

                // Tono
                double H = 0;
                double numerator = 0.5 * ((R - G) + (R - B));
                double denominator = sqrt((R - G) * (R - G) + (R - B) * (G - B));

                if (denominator != 0) {
                    double theta = acos(numerator / denominator);
                    H = (B > G) ? (2 * PI - theta) : theta;
                    H /= 2 * PI; // Normalizar a [0,1]
                }

                hsi[i][j][0] = H;
                hsi[i][j][1] = S;
                hsi[i][j][2] = I;
            }
        }

        ImageMatrix hsiImage = new ImageMatrix(hsi, TypeOfImage.HSI);
        JPanel resultPanel = makeJPanel(hsiImage);

        return new Result(resultPanel, hsiImage);
    }
}
