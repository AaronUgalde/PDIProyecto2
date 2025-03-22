import GUI.ChannelsInterface;
import GUI.GeneralOperationInterface;
import GUI.ImageInterface;
import Model.ImageMatrix;
import Operations.ModelColors.RGB_CMY;
import Operations.ModelColors.RGB_CMYK;
import Operations.ModelColors.RGB_YIQ;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {
            GeneralOperationInterface generalOperationInterface = new GeneralOperationInterface("RGB_YIQ", new LinkedHashMap<>(), new ImageMatrix(ImageIO.read(file)), new RGB_CMYK());
            generalOperationInterface.setVisible(true);
        //    ImageInterface imageInterface = new ImageInterface(ImageIO.read(file));
         //   imageInterface.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
