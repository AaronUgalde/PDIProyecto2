package Operations.ModelColors;

import Model.ImageMatrix;
import Model.TypeOfImage;
import java.util.LinkedHashMap;
import static java.lang.Math.*;

public class RGB_HSI extends ModelColor {

    /**
     * Convierte una imagen de RGB a HSI.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return La imagen convertida a HSI.
     */
    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
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
        return new ImageMatrix(hsi, TypeOfImage.HSI);
    }

    /**
     * Convierte una imagen de HSI a RGB.
     * @param imageMatrix La imagen en formato HSI.
     * @return La imagen convertida a RGB.
     */
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
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
                    // Sector de tono
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
        return new ImageMatrix(rgb, TypeOfImage.RGB);
    }
}
