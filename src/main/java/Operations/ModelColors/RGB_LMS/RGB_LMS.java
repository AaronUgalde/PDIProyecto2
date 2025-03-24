package Operations.ModelColors.RGB_LMS;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_LMS extends ModelColor {
    // Matriz de transformación RGB to LMS (Hunter-Pointer-Estévez)
    private static final double[][] RGB_TO_LMS = {
            {0.3811, 0.5783, 0.0402},
            {0.1967, 0.7244, 0.0782},
            {0.0241, 0.1228, 0.8444}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] rgb = imageMatrix.getMatrix();
        double[][][] lms = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Aplicar transformación lineal
                lms[i][j][0] = RGB_TO_LMS[0][0] * rgb[i][j][0]
                        + RGB_TO_LMS[0][1] * rgb[i][j][1]
                        + RGB_TO_LMS[0][2] * rgb[i][j][2];

                lms[i][j][1] = RGB_TO_LMS[1][0] * rgb[i][j][0]
                        + RGB_TO_LMS[1][1] * rgb[i][j][1]
                        + RGB_TO_LMS[1][2] * rgb[i][j][2];

                lms[i][j][2] = RGB_TO_LMS[2][0] * rgb[i][j][0]
                        + RGB_TO_LMS[2][1] * rgb[i][j][1]
                        + RGB_TO_LMS[2][2] * rgb[i][j][2];
            }
        }

        ImageMatrix lmsImage = new ImageMatrix(lms, TypeOfImage.LMS);
        return new Result(makeJPanel(lmsImage), lmsImage);
    }
}