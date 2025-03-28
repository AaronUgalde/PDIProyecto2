package Operations.ModelColors.RGB_CMY;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class CMY_RGB extends ModelColor {
    /**
     * Convierte una imagen de CMY a RGB.
     * @param imageMatrix La imagen en formato CMY.
     * @return La imagen convertida a RGB.
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
                double[][] cmy = {{matrix[x][y][0]}, {matrix[x][y][1]}, {matrix[x][y][2]}};
                double[][] rgb = MatrixOperations.subtract(transform, cmy);

                matrix[x][y][0] = rgb[0][0];
                matrix[x][y][1] = rgb[1][0];
                matrix[x][y][2] = rgb[2][0];
            }
        }

        ImageMatrix resultImageMatrix = new ImageMatrix(matrix, TypeOfImage.RGB);
        JPanel resultPanel = makeJPanel(resultImageMatrix);
        return new Result(resultPanel, resultImageMatrix);
    }
}
