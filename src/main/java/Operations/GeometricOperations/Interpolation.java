package Operations.GeometricOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class Interpolation extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se espera el factor de escala (Number)
        Number scaleNum = (Number) params.get("scale");
        if(scaleNum == null){
            throw new Exception("Parámetro 'scale' es requerido.");
        }
        double scale = scaleNum.doubleValue();

        BufferedImage originalBuffered = originalImage.toBufferedImage();
        int w = originalBuffered.getWidth();
        int h = originalBuffered.getHeight();
        int newWidth = (int) (w * scale);
        int newHeight = (int) (h * scale);

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalBuffered.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        // Usar solo interpolación bilineal
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Aplicar la transformación de escala
        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        g2d.drawRenderedImage(originalBuffered, at);
        g2d.dispose();

        ImageMatrix resultImage = new ImageMatrix(scaledImage);
        JPanel panel = createPanelFromImage(scaledImage);
        return new Result(panel, resultImage);
    }

    private JPanel createPanelFromImage(BufferedImage image){
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
