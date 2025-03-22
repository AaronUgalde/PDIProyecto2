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
import javax.swing.border.EmptyBorder;

public class ChannelsInterface extends JFrame {
    private final ImageMatrix imageMatrix;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color TITLE_COLOR = new Color(60, 60, 70);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public ChannelsInterface(ImageMatrix matrix) {
        super("Canales de Imagen");
        this.imageMatrix = matrix;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLocationRelativeTo(null);

        // Panel de título
        JPanel titlePanel = createTitlePanel("Visualización de Canales - " + imageMatrix.getTypeOfImage().toString());
        add(titlePanel, BorderLayout.NORTH);

        // Área para mostrar la imagen
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayout(1, 0, 10, 0));
        imagesPanel.setBackground(BACKGROUND_COLOR);
        imagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (imageMatrix.getTypeOfImage() != TypeOfImage.RGB) {
            char[] nameOfChannels = imageMatrix.getTypeOfImage().getNameOfChannels().toCharArray();
            String[] channelNames = {"Canal " + nameOfChannels[0], "Canal " + nameOfChannels[1], "Canal " + nameOfChannels[2], "Canal " + nameOfChannels[2]};
            for (int i = 0; i < imageMatrix.getTypeOfImage().getNumberOfChannels(); i++) {
                JPanel channelPanel = createChannelPanel(channelToBufferedImage(imageMatrix.getChannel(i)),
                        channelNames[i]);
                imagesPanel.add(channelPanel);
            }
        } else {
            String[] rgbChannelNames = {"Canal R", "Canal G", "Canal B"};
            for (int i = 0; i < imageMatrix.getTypeOfImage().getNumberOfChannels(); i++) {
                JPanel channelPanel = createChannelPanel(getRGBChannel(i), rgbChannelNames[i]);
                imagesPanel.add(channelPanel);
            }
        }
        JScrollPane scrollPane = new JScrollPane(imagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Área para mostrar los botones
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 15, 5, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TITLE_COLOR);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createChannelPanel(BufferedImage image, String channelName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(channelName, JLabel.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.add(nameLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        panel.add(createStyledButton("Convertir a RGB", this::toRGB, new Color(70, 130, 180)));
        panel.add(createStyledButton("Cargar Imagen", this::loadImage, new Color(60, 179, 113)));

        return panel;
    }

    private JButton createStyledButton(String text, Runnable action, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });

        button.addActionListener(e -> action.run());
        return button;
    }

    private String[] getChannelNames(TypeOfImage type) {
        switch (type) {
            case HSV: return new String[]{"Canal H", "Canal S", "Canal V"};
            case HSI: return new String[]{"Canal H", "Canal S", "Canal I"};
            case YIQ: return new String[]{"Canal Y", "Canal I", "Canal Q"};
            case CMY: return new String[]{"Canal C", "Canal M", "Canal Y"};
            case CMYK: return new String[]{"Canal C", "Canal M", "Canal Y", "Canal K"};
            case LMS: return new String[]{"Canal L", "Canal M", "Canal S"};
            case LAB: return new String[]{"Canal L", "Canal A", "Canal B"};
            default: return new String[]{"Canal 1", "Canal 2", "Canal 3"};
        }
    }

    private void toRGB() {
        try {
            switch (imageMatrix.getTypeOfImage()) {
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
            showError("Error de conversión", e);
        }
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageInterface newInterface = new ImageInterface(ImageIO.read(file));
                newInterface.setVisible(true);
            } catch (IOException ex) {
                showError("Error al cargar la imagen", ex);
            }
        }
    }

    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage(),
                title,
                JOptionPane.ERROR_MESSAGE
        );
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
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = (int) Math.round((imageMatrix.getMatrix()[i][j][channelIndex]) * 255);
                if (channelIndex == 0) {
                    int rgb = (value << 16);
                    img.setRGB(j, i, rgb);
                }
                if (channelIndex == 1) {
                    int rgb = (value << 8);
                    img.setRGB(j, i, rgb);
                }
                if (channelIndex == 2) {
                    int rgb = value;
                    img.setRGB(j, i, rgb);
                }
            }
        }
        return img;
    }
}