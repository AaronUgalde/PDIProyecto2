package Operations.ModelColors.RGB_LMS;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import javax.swing.*;
import java.util.LinkedHashMap;

public class LMS_RGB extends ModelColor {
    // Matriz inversa verificada (LMS to RGB)
    private static final double[][] LMS_TO_RGB = {
            {4.4679, -3.5873, 0.1193},
            {-1.2186, 2.3809, -0.1624},
            {0.0497, -0.2439, 1.2045}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] lms = imageMatrix.getMatrix();
        double[][][] rgb = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Aplicar matriz inversa
                double R = LMS_TO_RGB[0][0] * lms[i][j][0]
                        + LMS_TO_RGB[0][1] * lms[i][j][1]
                        + LMS_TO_RGB[0][2] * lms[i][j][2];

                double G = LMS_TO_RGB[1][0] * lms[i][j][0]
                        + LMS_TO_RGB[1][1] * lms[i][j][1]
                        + LMS_TO_RGB[1][2] * lms[i][j][2];

                double B = LMS_TO_RGB[2][0] * lms[i][j][0]
                        + LMS_TO_RGB[2][1] * lms[i][j][1]
                        + LMS_TO_RGB[2][2] * lms[i][j][2];

                // Clampear valores y evitar NaN
                rgb[i][j][0] = Math.max(0, Math.min(1, R));
                rgb[i][j][1] = Math.max(0, Math.min(1, G));
                rgb[i][j][2] = Math.max(0, Math.min(1, B));
            }
        }

        ImageMatrix rgbImage = new ImageMatrix(rgb, TypeOfImage.RGB);
        return new Result(makeJPanel(rgbImage), rgbImage);
    }
}