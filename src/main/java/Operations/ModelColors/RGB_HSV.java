package Operations.ModelColors;

import Model.ImageMatrix;
import Model.TypeOfImage;

public class RGB_HSV extends ModelColor {

    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix) {
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
        return new ImageMatrix(hsv, TypeOfImage.HSV);
    }

    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
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
                    default: // Nunca debería llegar aquí por el módulo 6
                        R = G = B = 0;
                        break;
                }

                rgb[i][j][0] = Math.max(0, Math.min(1, R));
                rgb[i][j][1] = Math.max(0, Math.min(1, G));
                rgb[i][j][2] = Math.max(0, Math.min(1, B));
            }
        }
        return new ImageMatrix(rgb, TypeOfImage.RGB);
    }
}