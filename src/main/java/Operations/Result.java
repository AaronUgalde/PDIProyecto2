package Operations;

import Model.ImageMatrix;

import javax.swing.*;

public class Result {
    JPanel panel;
    ImageMatrix imageMatrix;
    public Result(JPanel panel, ImageMatrix imageMatrix) {
        this.panel = panel;
        this.imageMatrix = imageMatrix;
    }

    public JPanel getPanel() {
        return panel;
    }

    public ImageMatrix getImageMatrix() {
        return imageMatrix;
    }
}
