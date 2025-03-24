package Operations.ModelColors.RGB_LAB;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.ModelColor;
import Operations.Result;
import Operations.ModelColors.RGB_LMS.LMS_RGB;

import javax.swing.*;
import java.util.LinkedHashMap;

public class LAB_RGB extends ModelColor {

    private static final double[][] LAB_TO_LMS = {
            {Math.sqrt(3), 0, 0},
            {0, Math.sqrt(6), 0},
            {0, 0, Math.sqrt(2)}
    };

    @Override
    public Result apply(ImageMatrix imageMatrix, LinkedHashMap<String, Object> parameters) {
        double[][][] lab = imageMatrix.getMatrix();
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] lms = new double[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double L_ = lab[i][j][0];
                double A_ = lab[i][j][1];
                double B_ = lab[i][j][2];

                // Transformación inversa
                double logL = (Math.sqrt(3) * L_ + Math.sqrt(6) * A_ + Math.sqrt(2) * B_) / 3;
                double logM = (Math.sqrt(3) * L_ + Math.sqrt(6) * A_ - 2 * Math.sqrt(2) * B_) / 3;
                double logS = (Math.sqrt(3) * L_ - Math.sqrt(6) * A_) / 3;

                // Aplicar exponencial
                lms[i][j][0] = Math.exp(logL);
                lms[i][j][1] = Math.exp(logM);
                lms[i][j][2] = Math.exp(logS);
            }
        }

        // Convertir LMS a RGB
        LMS_RGB lmsToRgb = new LMS_RGB();
        Result rgbResult = lmsToRgb.apply(new ImageMatrix(lms, TypeOfImage.LMS), parameters);

        JPanel resultPanel = makeJPanel(rgbResult.getImageMatrix());
        return new Result(resultPanel, rgbResult.getImageMatrix());
    }
}