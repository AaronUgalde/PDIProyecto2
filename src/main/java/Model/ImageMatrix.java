package Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageMatrix {
    // Matriz interna: [altura][anchura][canal] con canales en el orden R, G, B (o cualquier otro modelo de 3 canales)
    private double[][][] matrix;
    private final TypeOfImage typeOfImage;
    private final int width;
    private final int height;

    // Constructor que crea la matriz a partir de un BufferedImage, normalizando los valores a [0, 1]
    public ImageMatrix(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        this.typeOfImage = TypeOfImage.RGB;
        this.width = image.getWidth();
        this.height = image.getHeight();
        matrix = new double[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                matrix[i][j][0] = r / 255.0;
                matrix[i][j][1] = g / 255.0;
                matrix[i][j][2] = b / 255.0;
            }
        }
    }

    public ImageMatrix(BufferedImage image) {
        this.typeOfImage = TypeOfImage.RGB;
        this.width = image.getWidth();
        this.height = image.getHeight();
        matrix = new double[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                matrix[i][j][0] = r / 255.0;
                matrix[i][j][1] = g / 255.0;
                matrix[i][j][2] = b / 255.0;
            }
        }
    }

    public ImageMatrix(double[][][] matrix, TypeOfImage typeOfImage) {
        this.typeOfImage = typeOfImage;
        this.matrix = matrix;
        this.height = matrix.length;       // Corrected: height is first dimension
        this.width = matrix[0].length;     // Corrected: width is second dimension
    }

    // Convierte la matriz interna a un BufferedImage para mostrar o guardar la imagen
    public BufferedImage toBufferedImage() {
        if (this.typeOfImage != TypeOfImage.RGB) {
            throw new UnsupportedOperationException("Type of image is not RGB.");
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = (int) Math.round(matrix[i][j][0] * 255);
                int g = (int) Math.round(matrix[i][j][1] * 255);
                int b = (int) Math.round(matrix[i][j][2] * 255);
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(j, i, rgb);
            }
        }
        return image;
    }

    public double[][] getChannel(int channelIndex) {
        if (channelIndex >= typeOfImage.getChannels()) {
            throw new IllegalArgumentException("Invalid channel index");
        }
        double[][] channel = new double[height][width];
        for (int i = 0; i < height; i++) {      // Loop over height first
            for (int j = 0; j < width; j++) {   // Then loop over width
                channel[i][j] = matrix[i][j][channelIndex];
            }
        }
        return channel;
    }
    // Getters para la matriz, ancho y alto
    public double[][][] getMatrix() {
        return matrix;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public TypeOfImage getTypeOfImage() {
        return typeOfImage;
    }
}
