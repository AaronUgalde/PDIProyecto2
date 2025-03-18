package Operations.ModelColors;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;

public class RGB_YIQ extends ModelColor{

    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][] transform = {
                {1, 0.956, 0.621},
                {1, -0.272, -0.647},
                {1, -1.106, 1.703}
        };
        double[][][] matrix = imageMatrix.getMatrix();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] yiq = {{matrix[x][y][0]}, {matrix[x][y][1]},{matrix[x][y][2]}};
                double[][] rgb = MatrixOperations.multiply(transform, yiq);
                matrix[x][y][0] = rgb[0][0];
                matrix[x][y][1] = rgb[1][0];
                matrix[x][y][2] = rgb[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.RGB);
    }

    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][] transform = {
                {0.299, 0.587, 0.114},
                {0.596, -0.274, -0.322},
                {0.211, -0.523, 0.312}
        };
        double[][][] matrix = imageMatrix.getMatrix();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] rgb = {{matrix[x][y][0]}, {matrix[x][y][1]},{matrix[x][y][2]}};
                double[][] yiq = MatrixOperations.multiply(transform, rgb);
                matrix[x][y][0] = yiq[0][0];
                matrix[x][y][1] = yiq[1][0];
                matrix[x][y][2] = yiq[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.YIQ);
    }
}
