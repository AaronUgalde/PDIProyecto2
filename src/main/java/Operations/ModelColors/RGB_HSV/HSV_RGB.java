package Operations.ModelColors.RGB_HSV;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class HSV_RGB extends ModelColor {

    /**
     * Convierte una imagen de HSV a RGB.
     * @param imageMatrix La imagen en formato HSV.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return Un Result con la imagen convertida a RGB.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] hsv = imageMatrix.getMatrix();
        double[][][] rgb = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double H = hsv[i][j][0] * 6;
                double S = hsv[i][j][1];
                double V = hsv[i][j][2];

                int sector = (int) Math.floor(H);
                double f = H - sector;
                double p = V * (1 - S);
                double q = V * (1 - S * f);
                double t = V * (1 - S * (1 - f));

                double R, G, B;
                switch (sector % 6) {
                    case 0:
                        R = V;
                        G = t;
                        B = p;
                        break;
                    case 1:
                        R = q;
                        G = V;
                        B = p;
                        break;
                    case 2:
                        R = p;
                        G = V;
                        B = t;
                        break;
                    case 3:
                        R = p;
                        G = q;
                        B = V;
                        break;
                    case 4:
                        R = t;
                        G = p;
                        B = V;
                        break;
                    case 5:
                        R = V;
                        G = p;
                        B = q;
                        break;
                    default:
                        R = G = B = 0;
                        break;
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