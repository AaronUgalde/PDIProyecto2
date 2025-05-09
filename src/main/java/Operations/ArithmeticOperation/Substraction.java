package Operations.ArithmeticOperation;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

public class Substraction extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el path de la imagen a restar (tipo String)
        String imagePath = (String) params.get("image");
        BufferedImage secondBufferedImage = ImageIO.read(new File(imagePath));
        if (secondBufferedImage == null) {
            throw new Exception("No se pudo cargar la imagen desde: " + imagePath);
        }
        ImageMatrix secondImage = new ImageMatrix(secondBufferedImage);

        // Tomar dimensiones mínimas para operar
        int width = Math.min(originalImage.getWidth(), secondImage.getWidth());
        int height = Math.min(originalImage.getHeight(), secondImage.getHeight());
        double[][][] matrix1 = originalImage.getMatrix();
        double[][][] matrix2 = secondImage.getMatrix();
        double[][][] resultMatrix = new double[height][width][3];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                for (int c = 0; c < 3; c++){
                    double value = matrix1[i][j][c] - matrix2[i][j][c];
                    // Evitar valores negativos
                    resultMatrix[i][j][c] = Math.max(0.0, value);
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
