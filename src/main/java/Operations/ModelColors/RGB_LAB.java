package Operations.ModelColors;

import Model.ImageMatrix;
import Model.MatrixOperations;
import Model.TypeOfImage;
import java.util.LinkedHashMap;

public class RGB_LAB extends ModelColor {

    // Matriz de conversión de LMS a Lαβ
    private static final double[][] LMS_TO_LAB = {
            {1.0 / Math.sqrt(3.0), 0, 0},
            {0, 1.0 / Math.sqrt(6.0), 0},
            {0, 0, 1.0 / Math.sqrt(2.0)}
    };

    // Calculamos la inversa de LMS_TO_LAB para la conversión inversa
    private static final double[][] LAB_TO_LMS = MatrixOperations.invert(LMS_TO_LAB);

    /**
     * Convierte una imagen de RGB a LAB.
     * @param imageMatrix La imagen en formato RGB.
     * @param parameters Mapa de parámetros (no se usan en esta operación).
     * @return La imagen convertida a LAB.
     * @throws Exception
     */
    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) throws Exception {
        // Primero convertir RGB a LMS utilizando la operación definida en RGB_LMS
        ImageMatrix lmsImage = new RGB_LMS().apply(imageMatrix, new LinkedHashMap<>());

        double[][][] lms = lmsImage.getMatrix();
        int width = lmsImage.getWidth();
        int height = lmsImage.getHeight();
        double[][][] lab = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Aplicar logaritmo a cada canal
                double[][] logLMS = {
                        {Math.log(lms[i][j][0])},
                        {Math.log(lms[i][j][1])},
                        {Math.log(lms[i][j][2])}
                };

                // Multiplicar por la matriz de transformación LMS_TO_LAB
                double[][] labValues = MatrixOperations.multiply(LMS_TO_LAB, logLMS);

                lab[i][j][0] = labValues[0][0];
                lab[i][j][1] = labValues[1][0];
                lab[i][j][2] = labValues[2][0];
            }
        }
        return new ImageMatrix(lab, TypeOfImage.LAB);
    }

    /**
     * Regresa una imagen de LAB a RGB.
     * @param imageMatrix La imagen en formato LAB.
     * @return La imagen convertida a RGB.
     */
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        double[][][] lab = imageMatrix.getMatrix();
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] lms = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double[][] labValues = {
                        {lab[i][j][0]},
                        {lab[i][j][1]},
                        {lab[i][j][2]}
                };

                // Aplicar la matriz inversa para obtener logLMS
                double[][] logLMS = MatrixOperations.multiply(LAB_TO_LMS, labValues);

                // Aplicar exponencial para obtener los valores LMS
                lms[i][j][0] = Math.exp(logLMS[0][0]);
                lms[i][j][1] = Math.exp(logLMS[1][0]);
                lms[i][j][2] = Math.exp(logLMS[2][0]);
            }
        }

        // Convertir LMS a RGB utilizando la operación de deApply de RGB_LMS
        return new RGB_LMS().deApply(new ImageMatrix(lms, TypeOfImage.LMS));
    }
}
