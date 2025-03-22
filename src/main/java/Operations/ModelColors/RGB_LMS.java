package Operations.ModelColors;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;
import java.util.LinkedHashMap;

public class RGB_LMS extends ModelColor {

    /**
     * Convierte una imagen de RGB a LMS.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se utilizan en esta operación).
     * @return La imagen convertida a LMS.
     * @throws Exception
     */
    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) throws Exception {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        // Matriz de transformación para convertir de RGB a LMS
        double[][] transform = {
                {0.3811, 0.5783, 0.0402},
                {0.1967, 0.7244, 0.0782},
                {0.0241, 0.1228, 0.8444}
        };
        double[][][] matrix = imageMatrix.getMatrix();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] rgb = {
                        {matrix[x][y][0]},
                        {matrix[x][y][1]},
                        {matrix[x][y][2]}
                };
                double[][] lms = MatrixOperations.multiply(transform, rgb);
                matrix[x][y][0] = lms[0][0];
                matrix[x][y][1] = lms[1][0];
                matrix[x][y][2] = lms[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.LMS);
    }

    /**
     * Regresa una imagen de LMS a RGB usando la transformación inversa.
     * @param imageMatrix La imagen en formato LMS.
     * @return La imagen convertida a RGB.
     */
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        // Matriz de transformación para convertir de LMS a RGB
        double[][] transform = {
                {4.4679, -3.5873, 0.1193},
                {-1.2186, 2.3809, -0.1624},
                {0.0497, -0.2439, 1.2045}
        };
        double[][][] matrix = imageMatrix.getMatrix();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double[][] lms = {
                        {matrix[x][y][0]},
                        {matrix[x][y][1]},
                        {matrix[x][y][2]}
                };
                double[][] rgb = MatrixOperations.multiply(transform, lms);
                matrix[x][y][0] = rgb[0][0];
                matrix[x][y][1] = rgb[1][0];
                matrix[x][y][2] = rgb[2][0];
            }
        }
        return new ImageMatrix(matrix, TypeOfImage.RGB);
    }
}
