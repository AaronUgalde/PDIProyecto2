package Operations.RelationalOperation;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

public class NotEqual extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        String imagePath = (String) params.get("image");
        BufferedImage secondBufferedImage = ImageIO.read(new File(imagePath));
        if(secondBufferedImage == null){
            throw new Exception("No se pudo cargar la imagen desde: " + imagePath);
        }
        ImageMatrix secondImage = new ImageMatrix(secondBufferedImage);

        int height1 = originalImage.getHeight();
        int height2 = secondImage.getHeight();
        int width1 = originalImage.getWidth();
        int width2 = secondImage.getWidth();
        int width = Math.max(width1, width2);
        int height = Math.max(height1, height2);

        double[][][] matrix1 = originalImage.getMatrix();
        double[][][] matrix2 = secondImage.getMatrix();
        double[][][] resultMatrix = new double[height][width][3];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                for (int c = 0; c < 3; c++){
                    double val1 = (i < height1 && j < width1) ? matrix1[i][j][c] : 0.0;
                    double val2 = (i < height2 && j < width2) ? matrix2[i][j][c] : 0.0;
                    resultMatrix[i][j][c] = (val1 != val2) ? 1.0 : 0.0;
                }
            }
        }

        ImageMatrix resultImage = new ImageMatrix(resultMatrix, originalImage.getTypeOfImage());
        BufferedImage resultBuffered = resultImage.toBufferedImage();
        JPanel panel = createPanelFromImage(resultBuffered);
        return new Result(panel, resultImage);
    }

    private JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
