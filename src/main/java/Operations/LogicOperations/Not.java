package Operations.LogicOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class Not extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        double[][][] matrix = originalImage.getMatrix();
        double[][][] resultMatrix = new double[height][width][3];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                for(int c = 0; c < 3; c++){
                    int val = (int)Math.round(matrix[i][j][c] * 255);
                    int resultVal = (~val) & 0xFF;
                    resultMatrix[i][j][c] = resultVal / 255.0;
                }
            }
        }

        ImageMatrix resultImage = new ImageMatrix(resultMatrix, originalImage.getTypeOfImage());
        BufferedImage resultBuffered = resultImage.toBufferedImage();
        JPanel panel = createPanelFromImage(resultBuffered);
        return new Result(panel, resultImage);
    }

    public JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
