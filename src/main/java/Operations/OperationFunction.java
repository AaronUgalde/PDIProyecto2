package Operations;

import Model.ImageMatrix;
import Model.TypeOfImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public abstract class OperationFunction {
    // Realiza la operación y retorna el panel con el resultado
    public abstract Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception;

    public JPanel convertToGrayscale(ImageMatrix img, int channel) {
        int height = img.getHeight();
        int width = img.getWidth();
        double[][][] yiq = img.getMatrix();
        double[][][] grayMatrix = new double[height][width][3];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                double y = yiq[i][j][channel]; // Canal Y
                grayMatrix[i][j][0] = y;
                grayMatrix[i][j][1] = y;
                grayMatrix[i][j][2] = y;
            }
        }
        // Se crea una nueva imagen del tipo RGB para mostrar el resultado
        ImageMatrix grayImage = new ImageMatrix(grayMatrix, TypeOfImage.RGB);
        return createPanelFromImage(grayImage.toBufferedImage());
    }

    private JPanel createPanelFromImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public ImageMatrix invertChannel(ImageMatrix img, int channel) {
        int height = img.getHeight();
        int width = img.getWidth();
        double[][][] matrix = img.getMatrix();
        double[][] invertedChannel = new double[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                double y = matrix[i][j][channel];
                double invValue = 1.0 - y;
                invertedChannel[i][j] = invValue;
            }
        }

        img.setChannel(channel, invertedChannel);
        return img;
    }
}
