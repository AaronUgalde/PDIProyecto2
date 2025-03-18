package GUI;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.ModelColors.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ChannelsInterface extends JFrame {
    private final ImageMatrix imageMatrix;

    public ChannelsInterface(ImageMatrix matrix){
        super("Aplicación de Procesamiento de Imágenes");
        this.imageMatrix = matrix;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // Área para mostrar la imagen
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.X_AXIS));

        if(imageMatrix.getTypeOfImage() != TypeOfImage.RGB) {
            for (int i = 0; i < imageMatrix.getTypeOfImage().getChannels(); i++) {
                imagesPanel.add(new JLabel(new ImageIcon(channelToBufferedImage(imageMatrix.getChannel(i)))));
            }
        }else{
            for(int i = 0; i<imageMatrix.getTypeOfImage().getChannels(); i++){
                imagesPanel.add(new JLabel(new ImageIcon(getRGBChannel(i))));
            }
        }
        JScrollPane scrollPane = new JScrollPane(imagesPanel);
        add(scrollPane, BorderLayout.CENTER);

        //Area para mostrar los botones
        JPanel controlPanel = new JPanel();
        controlPanel.add(new CustomButton("Convertir a RGB", this::toRGB));
        controlPanel.add(new CustomButton("Load image", this::loadImage));
        add(controlPanel, BorderLayout.SOUTH);

    }

    private void toRGB() {
        try {
            switch (imageMatrix.getTypeOfImage()){
                case RGB:
                    ImageInterface fromRGB = new ImageInterface(imageMatrix.toBufferedImage());
                    fromRGB.setVisible(true);
                    break;
                case HSV:
                    RGB_HSV fromHSV = new RGB_HSV();
                    ImageInterface fromHSVFrame = new ImageInterface(fromHSV.deApply(imageMatrix).toBufferedImage());
                    fromHSVFrame.setVisible(true);
                    break;
                case HSI:
                    RGB_HSI fromHSI = new RGB_HSI();
                    ImageInterface fromHSIFrame = new ImageInterface(fromHSI.deApply(imageMatrix).toBufferedImage());
                    fromHSIFrame.setVisible(true);
                    break;
                case YIQ:
                    RGB_YIQ fromYIQ = new RGB_YIQ();
                    ImageInterface fromYIQFrame = new ImageInterface(fromYIQ.deApply(imageMatrix).toBufferedImage());
                    fromYIQFrame.setVisible(true);
                    break;
                case CMY:
                    RGB_CMY fromCMY = new RGB_CMY();
                    ImageInterface fromCMYFrame = new ImageInterface(fromCMY.deApply(imageMatrix).toBufferedImage());
                    fromCMYFrame.setVisible(true);
                    break;
                case CMYK:
                    RGB_CMYK fromCMYK = new RGB_CMYK();
                    ImageInterface fromCMYKFrame = new ImageInterface(fromCMYK.deApply(imageMatrix).toBufferedImage());
                    fromCMYKFrame.setVisible(true);
                    break;
                case LMS:
                    RGB_LMS fromLMS = new RGB_LMS();
                    ImageInterface fromLMSFrame = new ImageInterface(fromLMS.deApply(imageMatrix).toBufferedImage());
                    fromLMSFrame.setVisible(true);
                    break;
                case LAB:
                    RGB_LAB fromLAB = new RGB_LAB();
                    ImageInterface fromLABFrame = new ImageInterface(fromLAB.deApply(imageMatrix).toBufferedImage());
                    fromLABFrame.setVisible(true);
                    break;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de conversión: " + e.getMessage());
        }
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageInterface newInterface = new ImageInterface(ImageIO.read(file));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage());
            }
        }
    }

    private BufferedImage channelToBufferedImage(double[][] channel) {
        int height = channel.length;
        int width = channel[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (channel[i][j] < min) min = channel[i][j];
                if (channel[i][j] > max) max = channel[i][j];
            }
        }
        double range = max - min;
        if (range == 0) {
            range = 1;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = (int) Math.round(((channel[i][j] - min) / range) * 255);
                int rgb = (value << 16) | (value << 8) | value;
                img.setRGB(j, i, rgb);
            }
        }
        return img;
    }

    private BufferedImage getRGBChannel(int channelIndex) {
        int height = imageMatrix.getHeight();
        int width = imageMatrix.getWidth();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int value = (int) Math.round((imageMatrix.getMatrix()[i][j][channelIndex]) * 255);
                if(channelIndex == 0){
                    int rgb = (value << 16);
                    img.setRGB(j, i, rgb);
                }
                if(channelIndex == 1){
                    int rgb = (value << 8);
                    img.setRGB(j, i, rgb);
                }
                if(channelIndex == 2){
                    int rgb = (value);
                    img.setRGB(j, i, rgb);
                }
            }
        }
        return img;
    }
}
