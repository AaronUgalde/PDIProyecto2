package Operations.ModelColors;

import Model.ImageMatrix;
import Model.TypeOfImage;
import java.util.LinkedHashMap;

public class RGB_CMYK extends ModelColor {

    /**
     * Convierte una imagen de RGB a CMYK.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return La imagen convertida a CMYK.
     */
    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        RGB_CMY fromCMY = new RGB_CMY();
        ImageMatrix CMYImageMatrix = fromCMY.apply(imageMatrix, parameters);
        int height = imageMatrix.getHeight();
        int width = imageMatrix.getWidth();
        double[][][] CMYMatrix = CMYImageMatrix.getMatrix();
        double[][][] CMYKMatrix = new double[height][width][4];

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double c = CMYMatrix[x][y][0];
                double m = CMYMatrix[x][y][1];
                double yC = CMYMatrix[x][y][2];
                double k = Math.min(c, Math.min(m, yC));

                CMYKMatrix[x][y][0] = (c - k) / (1 - k);
                CMYKMatrix[x][y][1] = (m - k) / (1 - k);
                CMYKMatrix[x][y][2] = (yC - k) / (1 - k);
                CMYKMatrix[x][y][3] = k;
            }
        }
        return new ImageMatrix(CMYKMatrix, TypeOfImage.CMYK);
    }

    /**
     * Convierte una imagen de CMYK a RGB.
     * @param imageMatrix La imagen en formato CMYK.
     * @return La imagen convertida a RGB.
     */
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] CMYKMatrix = imageMatrix.getMatrix();
        double[][][] CMYMatrix = new double[height][width][3];

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double k = CMYKMatrix[x][y][3];
                CMYMatrix[x][y][0] = CMYKMatrix[x][y][0] * (1 - k) + k;
                CMYMatrix[x][y][1] = CMYKMatrix[x][y][1] * (1 - k) + k;
                CMYMatrix[x][y][2] = CMYKMatrix[x][y][2] * (1 - k) + k;
            }
        }

        RGB_CMY rgb_cmy = new RGB_CMY();
        return rgb_cmy.deApply(new ImageMatrix(CMYMatrix, TypeOfImage.CMY));
    }
}
