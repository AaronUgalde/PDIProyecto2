package Operations.GeometricOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.awt.geom.AffineTransform;

public class Translation extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Se esperan los parámetros de translación: tx y ty (Number)
        Number txNum = (Number) params.get("tx");
        Number tyNum = (Number) params.get("ty");
        if (txNum == null || tyNum == null) {
            throw new Exception("Parámetros 'tx' y 'ty' son requeridos.");
        }
        int tx = txNum.intValue();
        int ty = tyNum.intValue();

        BufferedImage originalBuffered = originalImage.toBufferedImage();
        int w = originalBuffered.getWidth();
        int h = originalBuffered.getHeight();

        // Calcular nuevas dimensiones para incluir la imagen trasladada
        int extraX = Math.abs(tx);
        int extraY = Math.abs(ty);
        int newWidth = w + extraX;
        int newHeight = h + extraY;

        BufferedImage translatedImage = new BufferedImage(newWidth, newHeight, originalBuffered.getType());
        Graphics2D g2d = translatedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Ajustar el origen en caso de desplazamientos negativos
        int offsetX = tx < 0 ? extraX : 0;
        int offsetY = ty < 0 ? extraY : 0;

        // Aplicar la translación
        AffineTransform at = new AffineTransform();
        at.translate(offsetX + tx, offsetY + ty);
        g2d.drawRenderedImage(originalBuffered, at);
        g2d.dispose();

        ImageMatrix resultImage = new ImageMatrix(translatedImage);
        JPanel panel = createPanelFromImage(translatedImage);
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
