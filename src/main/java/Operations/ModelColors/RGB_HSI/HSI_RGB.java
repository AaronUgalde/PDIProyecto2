package Operations.ModelColors.RGB_HSI;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

import static java.lang.Math.*;

public class HSI_RGB extends ModelColor {

    /**
     * Convierte una imagen de HSI a RGB.
     * @param imageMatrix La imagen en formato HSI.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return Un Result con la imagen convertida a RGB.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] hsi = imageMatrix.getMatrix();
        double[][][] rgb = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double H = hsi[i][j][0] * 2 * PI;
                double S = hsi[i][j][1];
                double I = hsi[i][j][2];

                double R, G, B;

                if (S == 0) {
                    R = G = B = I;
                } else {
                    if (H < 2 * PI / 3) {
                        B = I * (1 - S);
                        R = I * (1 + (S * cos(H) / cos(PI / 3 - H)));
                        G = 3 * I - (R + B);
                    } else if (H < 4 * PI / 3) {
                        H -= 2 * PI / 3;
                        R = I * (1 - S);
                        G = I * (1 + (S * cos(H) / cos(PI / 3 - H)));
                        B = 3 * I - (R + G);
                    } else {
                        H -= 4 * PI / 3;
                        G = I * (1 - S);
                        B = I * (1 + (S * cos(H) / cos(PI / 3 - H)));
                        R = 3 * I - (G + B);
                    }
                }

                rgb[i][j][0] = Math.max(0, Math.min(1, R));
                rgb[i][j][1] = Math.max(0, Math.min(1, G));
                rgb[i][j][2] = Math.max(0, Math.min(1, B));
            }
        }

        ImageMatrix rgbImage = new ImageMatrix(rgb, TypeOfImage.RGB);
        JPanel resultPanel = makeJPanel(rgbImage);

        return new Result(resultPanel, rgbImage);
    }
}
