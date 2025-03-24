package Operations.ModelColors.RGB_YIQ;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_YIQ extends ModelColor {
    // Matriz de conversión NTSC estándar (RGB a YIQ)
    private static final double[][] RGB_TO_YIQ = {
            {0.299,   0.587,    0.114},
            {0.595716, -0.274453, -0.321263},
            {0.211456, -0.522591, 0.311135}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] rgb = imageMatrix.getMatrix();
        double[][][] yiq = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Cálculos directos para mejor rendimiento
                yiq[i][j][0] = RGB_TO_YIQ[0][0] * rgb[i][j][0]
                        + RGB_TO_YIQ[0][1] * rgb[i][j][1]
                        + RGB_TO_YIQ[0][2] * rgb[i][j][2];

                yiq[i][j][1] = RGB_TO_YIQ[1][0] * rgb[i][j][0]
                        + RGB_TO_YIQ[1][1] * rgb[i][j][1]
                        + RGB_TO_YIQ[1][2] * rgb[i][j][2];

                yiq[i][j][2] = RGB_TO_YIQ[2][0] * rgb[i][j][0]
                        + RGB_TO_YIQ[2][1] * rgb[i][j][1]
                        + RGB_TO_YIQ[2][2] * rgb[i][j][2];
            }
        }

        ImageMatrix yiqImage = new ImageMatrix(yiq, TypeOfImage.YIQ);
        return new Result(makeJPanel(yiqImage), yiqImage);
    }
}