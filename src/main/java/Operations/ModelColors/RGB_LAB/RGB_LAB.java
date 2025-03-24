package Operations.ModelColors.RGB_LAB;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import Operations.ModelColors.RGB_LMS.RGB_LMS;

import javax.swing.*;
import java.util.LinkedHashMap;

public class RGB_LAB extends ModelColor {

    private static final double[][] LMS_TO_LAB = {
            {1.0 / Math.sqrt(3.0), 0, 0},
            {0, 1.0 / Math.sqrt(6.0), 0},
            {0, 0, 1.0 / Math.sqrt(2.0)}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        // Convertir RGB a LMS
        RGB_LMS rgbToLms = new RGB_LMS();
        Result lmsResult = rgbToLms.apply(imageMatrix, parameters);
        ImageMatrix lmsImage = lmsResult.getImageMatrix();

        double[][][] lms = lmsImage.getMatrix();
        int width = lmsImage.getWidth();
        int height = lmsImage.getHeight();
        double[][][] lab = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double L = lms[i][j][0];
                double M = lms[i][j][1];
                double S = lms[i][j][2];

                // Aplicar logaritmo con protección para valores cero
                double logL = (L > 0) ? Math.log(L) : 0;
                double logM = (M > 0) ? Math.log(M) : 0;
                double logS = (S > 0) ? Math.log(S) : 0;

                // Multiplicar por matriz de transformación
                double L_ = (1.0 / Math.sqrt(3)) * (logL + logM + logS);
                double A_ = (1.0 / Math.sqrt(6)) * (logL + logM - 2 * logS);
                double B_ = (1.0 / Math.sqrt(2)) * (logL - logM);

                lab[i][j][0] = L_;
                lab[i][j][1] = A_;
                lab[i][j][2] = B_;
            }
        }

        ImageMatrix labImage = new ImageMatrix(lab, TypeOfImage.LAB);
        JPanel resultPanel = makeJPanel(labImage);
        return new Result(resultPanel, labImage);
    }
}