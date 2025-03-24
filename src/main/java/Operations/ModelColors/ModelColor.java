package Operations.ModelColors;

import Model.ImageMatrix;
import Operations.OperationFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ModelColor extends OperationFunction {

    private static BufferedImage extractChannelImage(int channel, ImageMatrix resultImage) {
        int width = resultImage.getWidth();
        int height = resultImage.getHeight();
        BufferedImage channelImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double[][] iChannel = resultImage.getChannel(channel);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = (int) Math.round(iChannel[j][i] * 255);
                int r = 0, g = 0, b = 0;
                if (resultImage.getTypeOfImage().name().equals("RGB")) {
                    if (channel == 0) r = pixel;
                    else if (channel == 1) g = pixel;
                    else if (channel == 2) b = pixel;
                } else if (resultImage.getTypeOfImage().name().equals("CMY")) {
                    int c = (channel == 0) ? pixel : 0;
                    int m = (channel == 1) ? pixel : 0;
                    int y = (channel == 2) ? pixel : 0;
                    r = 255 - c; g = 255 - m; b = 255 - y;
                } else if (resultImage.getTypeOfImage().name().equals("CMYK")) {
                    int c = (channel == 0) ? pixel : 0;
                    int m = (channel == 1) ? pixel : 0;
                    int y = (channel == 2) ? pixel : 0;
                    int k = (channel == 3) ? pixel : 0;
                    r = 255 - Math.min(255, c * (255 - k) + k);
                    g = 255 - Math.min(255, m * (255 - k) + k);
                    b = 255 - Math.min(255, y * (255 - k) + k);
                } else {
                    r = pixel; g = pixel; b = pixel;
                }
                int rgb = (r << 16) | (g << 8) | b;
                channelImg.setRGB(i, j, rgb);
            }
        }
        return channelImg;
    }

    public static JPanel makeJPanel(ImageMatrix imageMatrix) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        for(int i = 0; i<imageMatrix.getTypeOfImage().name().length(); i++) {
            JLabel label = new JLabel(imageMatrix.getTypeOfImage().name().substring(i, i+1));
            BufferedImage channelImage = extractChannelImage(i, imageMatrix);
            label.setIcon(new ImageIcon(channelImage));
            panel.add(label);
        }
        return panel;
    }
}
