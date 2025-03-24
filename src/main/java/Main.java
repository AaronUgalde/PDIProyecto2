import GUI.ImageInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {
        ImageInterface imageInterface = new ImageInterface(ImageIO.read(file));
        imageInterface.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
