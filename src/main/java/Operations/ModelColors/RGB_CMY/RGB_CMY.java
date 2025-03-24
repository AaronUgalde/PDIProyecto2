package Operations.ModelColors.RGB_CMY;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_CMY extends ModelColor {

    /**
     * Convierte una imagen de RGB a CMY.
     *
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters  Mapa de parámetros (no se utilizan en esta operación).
     * @return La imagen convertida a CMY.
     */
    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
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

        ImageMatrix resultImageMatrix = new ImageMatrix(matrix, TypeOfImage.CMY);
        JPanel resultPanel = makeJPanel(resultImageMatrix);
        return  new Result(resultPanel, resultImageMatrix);
    }
}
