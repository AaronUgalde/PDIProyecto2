package Operations.LogicOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

public class Or extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el path de la imagen para la operación OR (tipo String)
        String imagePath = (String) params.get("image");
        BufferedImage secondBufferedImage = ImageIO.read(new File(imagePath));
        if(secondBufferedImage == null) {
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

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                for(int c = 0; c < 3; c++){
                    int val1 = 0, val2 = 0;
                    if(i < height1 && j < width1){
                        val1 = (int)Math.round(matrix1[i][j][c] * 255);
                    }
                    if(i < height2 && j < width2){
                        val2 = (int)Math.round(matrix2[i][j][c] * 255);
                    }
                    int resultVal = val1 | val2;
                    resultMatrix[i][j][c] = resultVal / 255.0;
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
