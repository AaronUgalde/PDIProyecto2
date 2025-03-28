package Operations.GeometricOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class Rotation extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el ángulo de rotación en grados (Double)
        Double angleDegrees = (Double) params.get("angle");
        if (angleDegrees == null) {
            throw new Exception("Parámetro 'angle' es requerido.");
        }

        // Convertir grados a radianes
        double angle = Math.toRadians(angleDegrees);
        BufferedImage originalBuffered = originalImage.toBufferedImage();
        int w = originalBuffered.getWidth();
        int h = originalBuffered.getHeight();

        // Calcular las nuevas dimensiones para contener toda la imagen rotada
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        // Crear una nueva imagen con las dimensiones calculadas
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, originalBuffered.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        // Configurar interpolación para mayor calidad
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Trasladar el contexto para centrar la imagen original en la nueva imagen
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);
        // Rotar en torno al centro de la imagen original
        at.rotate(angle, w / 2.0, h / 2.0);

        g2d.drawRenderedImage(originalBuffered, at);
        g2d.dispose();

        ImageMatrix resultImage = new ImageMatrix(rotatedImage);
        JPanel panel = createPanelFromImage(rotatedImage);
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
