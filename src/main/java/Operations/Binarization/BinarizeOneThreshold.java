package Operations.Binarization;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.RGB_YIQ.RGB_YIQ;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class BinarizeOneThreshold extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        Result yiqResult = new RGB_YIQ().apply(originalImage, new LinkedHashMap<>());
        ImageMatrix yiqImageMatrix = yiqResult.getImageMatrix();
        double threshold = (double) params.get("threshold");
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        double[][][] yiqMatrix = yiqImageMatrix.getMatrix();
        double[][] binMatrix = new double[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                double y = yiqMatrix[i][j][0];
                double binValue = (y >= threshold) ? 1.0 : 0.0;
                binMatrix[i][j] = binValue;
            }
        }

        yiqImageMatrix.setChannel(0, binMatrix);
        yiqImageMatrix.setChannel(1, binMatrix);
        yiqImageMatrix.setChannel(2, binMatrix);
        JPanel resultPanel = convertToGrayscale(yiqImageMatrix, 0);
        return new Result(resultPanel, new ImageMatrix(yiqImageMatrix.getMatrix(), TypeOfImage.RGB));
    }
}
