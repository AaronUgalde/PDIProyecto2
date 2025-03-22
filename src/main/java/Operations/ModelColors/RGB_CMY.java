package Operations.ModelColors;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;
import java.util.LinkedHashMap;

public class RGB_CMY extends ModelColor {

    /**
     * Convierte una imagen de RGB a CMY.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return La imagen convertida a CMY.
     */
    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] matrix = imageMatrix.getMatrix();
        double[][] transform = {
                {1},
                {1},
                {1}
        };

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] rgb = {{matrix[x][y][0]}, {matrix[x][y][1]}, {matrix[x][y][2]}};
                double[][] cmy = MatrixOperations.subtract(transform, rgb);

                matrix[x][y][0] = cmy[0][0];
                matrix[x][y][1] = cmy[1][0];
                matrix[x][y][2] = cmy[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.CMY);
    }

    /**
     * Convierte una imagen de CMY a RGB.
     * @param imageMatrix La imagen en formato CMY.
     * @return La imagen convertida a RGB.
     */
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] matrix = imageMatrix.getMatrix();
        double[][] transform = {
                {1},
                {1},
                {1}
        };

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] cmy = {{matrix[x][y][0]}, {matrix[x][y][1]}, {matrix[x][y][2]}};
                double[][] rgb = MatrixOperations.subtract(transform, cmy);

                matrix[x][y][0] = rgb[0][0];
                matrix[x][y][1] = rgb[1][0];
                matrix[x][y][2] = rgb[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.RGB);
    }
}
