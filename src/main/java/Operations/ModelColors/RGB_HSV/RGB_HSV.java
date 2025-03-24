package Operations.ModelColors.RGB_HSV;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_HSV extends ModelColor {

    /**
     * Convierte una imagen de RGB a HSV.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return Un Result con la imagen convertida a HSV.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] matrix = imageMatrix.getMatrix();
        double[][][] hsv = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double R = matrix[i][j][0];
                double G = matrix[i][j][1];
                double B = matrix[i][j][2];

                double max = Math.max(R, Math.max(G, B));
                double min = Math.min(R, Math.min(G, B));
                double delta = max - min;

                // Value
                double V = max;

                // Saturation
                double S = (max == 0) ? 0 : delta / max;

                // Hue
                double H = 0;
                if (delta != 0) {
                    if (max == R) H = (G - B) / delta + (G < B ? 6 : 0);
                    else if (max == G) H = (B - R) / delta + 2;
                    else H = (R - G) / delta + 4;
                    H /= 6; // Normalizar a [0,1]
                }

                hsv[i][j][0] = H;
                hsv[i][j][1] = S;
                hsv[i][j][2] = V;
            }
        }

        ImageMatrix hsvImage = new ImageMatrix(hsv, TypeOfImage.HSV);
        JPanel resultPanel = makeJPanel(hsvImage);

        return new Result(resultPanel, hsvImage);
    }
}