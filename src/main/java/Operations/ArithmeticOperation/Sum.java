package Operations.ArithmeticOperation;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;

public class Sum extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el path de la imagen a sumar (tipo String)
        String imagePath = (String) params.get("image");
        BufferedImage secondBufferedImage = ImageIO.read(new File(imagePath));
        if (secondBufferedImage == null) {
            throw new Exception("No se pudo cargar la imagen desde: " + imagePath);
        }
        ImageMatrix secondImage = new ImageMatrix(secondBufferedImage);

        int heigth1 = originalImage.getHeight(), heigth2 = secondBufferedImage.getHeight();
        int width1 = originalImage.getWidth(), width2 = secondBufferedImage.getWidth();

        // Tomar dimensiones mínimas para operar
        int width = Math.max(originalImage.getWidth(), secondImage.getWidth());
        int height = Math.max(originalImage.getHeight(), secondImage.getHeight());
        double[][][] matrix1 = originalImage.getMatrix();
        double[][][] matrix2 = secondImage.getMatrix();
        double[][][] resultMatrix = new double[height][width][3];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                for (int c = 0; c < 3; c++){
                    double value;
                    if(i < heigth1 && j < width1){
                        if(i < heigth2 && j < width2){
                            value = matrix1[i][j][c] + matrix2[i][j][c];
                        }else{
                            value = matrix1[i][j][c];
                        }
                    }else{
                        if(i < heigth2 && j < width2){
                            value = matrix2[i][j][c];
                        }else{
                            value = 0;
                        }
                    }
                    // Evitar valores mayores a 1.0
                    resultMatrix[i][j][c] = Math.min(1.0, value);
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
