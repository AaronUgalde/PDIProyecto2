package GUI;

import Model.ImageMatrix;
import Operations.ModelColors.*;
import Operations.Operation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageInterface extends JFrame {
    private final ImageMatrix imageMatrix;

    public ImageInterface(BufferedImage image) throws IOException {
        super("Aplicación de Procesamiento de Imágenes");
        this.imageMatrix = new ImageMatrix(image);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // Área para mostrar la imagen
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        add(scrollPane, BorderLayout.CENTER);


        JPanel controlPanel = new JPanel();
        controlPanel.add(new CustomButton("Load image", this::loadImage));
        controlPanel.add(new CustomButton("Mostrar canales RGB", this::showChannels));
        controlPanel.add(new CustomButton("Convertir a YIQ", () -> convertColor(new RGB_YIQ())));
        controlPanel.add(new CustomButton("Convertir a CMY", () -> convertColor(new RGB_CMY())));
        controlPanel.add(new CustomButton("Convertir a CMYK", () -> convertColor(new RGB_CMYK())));
        controlPanel.add(new CustomButton("Convertir a HSI", () -> convertColor(new RGB_HSI())));
        controlPanel.add(new CustomButton("Convertir a HSV", () -> convertColor(new RGB_HSV())));
        controlPanel.add(new CustomButton("Convertir a LMS", () -> convertColor(new RGB_LMS())));
        controlPanel.add(new CustomButton("Convertir a LAB", () -> convertColor(new RGB_LAB())));
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void convertColor(Operation operation) {
        ChannelsInterface channelsInterface = new  ChannelsInterface(operation.apply(imageMatrix));
        channelsInterface.setVisible(true);
    }

    private void showChannels(){
        ChannelsInterface channelsInterface = new ChannelsInterface(imageMatrix);
        channelsInterface.setVisible(true);
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

}
