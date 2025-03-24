package Operations.ModelColors.RGB_YIQ;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import javax.swing.*;
import java.util.LinkedHashMap;

public class YIQ_RGB extends ModelColor {
    // Matriz inversa exacta calculada a partir de la matriz NTSC
    private static final double[][] YIQ_TO_RGB = {
            {1.000,  0.956,  0.619},
            {1.000, -0.272, -0.647},
            {1.000, -1.106, 1.703}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] yiq = imageMatrix.getMatrix();
        double[][][] rgb = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double Y = yiq[i][j][0];
                double I = yiq[i][j][1];
                double Q = yiq[i][j][2];

                // Conversión directa con clampeo
                double R = Y + 0.956*I + 0.619*Q;
                double G = Y - 0.272*I - 0.647*Q;
                double B = Y - 1.106*I + 1.703*Q;

                // Asegurar valores en [0,1]
                rgb[i][j][0] = Math.max(0, Math.min(1, R));
                rgb[i][j][1] = Math.max(0, Math.min(1, G));
                rgb[i][j][2] = Math.max(0, Math.min(1, B));
            }
        }

        ImageMatrix rgbImage = new ImageMatrix(rgb, TypeOfImage.RGB);
        return new Result(makeJPanel(rgbImage), rgbImage);
    }
}