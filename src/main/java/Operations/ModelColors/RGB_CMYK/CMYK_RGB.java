package Operations.ModelColors.RGB_CMYK;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.ModelColors.RGB_CMY.CMY_RGB;
import Operations.Result;
import java.util.LinkedHashMap;

public class CMYK_RGB extends ModelColor {

    /**
     * Convierte una imagen de CMYK a RGB.
     *
     * @param imageMatrix La imagen en formato CMYK.
     * @param parameters  Mapa de parámetros (no se utilizan en esta operación).
     * @return Un objeto Result que contiene el JPanel con la imagen y el ImageMatrix convertido.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int height = imageMatrix.getHeight();
        int width = imageMatrix.getWidth();
        double[][][] CMYKMatrix = imageMatrix.getMatrix();
        double[][][] CMYMatrix = new double[height][width][3];

        // Paso 1: Convertir de CMYK a CMY
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double k = CMYKMatrix[x][y][3];

                CMYMatrix[x][y][0] = CMYKMatrix[x][y][0] * (1 - k) + k;
                CMYMatrix[x][y][1] = CMYKMatrix[x][y][1] * (1 - k) + k;
                CMYMatrix[x][y][2] = CMYKMatrix[x][y][2] * (1 - k) + k;
            }
        }

        ImageMatrix cmyImageMatrix = new ImageMatrix(CMYMatrix, TypeOfImage.CMY);

        // Paso 2: Convertir de CMY a RGB
        CMY_RGB toRGB = new CMY_RGB();

        return toRGB.apply(cmyImageMatrix, parameters);
    }
}
