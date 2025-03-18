package Operations.ModelColors;

import Model.ImageMatrix;
import Model.TypeOfImage;

public class RGB_CMYK extends ModelColor{
    @Override
    public ImageMatrix deApply(ImageMatrix imageMatrix) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        double[][][] CMYMatrix = new double[height][width][3];
        double[][][] CMYKMatrix = imageMatrix.getMatrix();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double k = CMYKMatrix[x][y][3];
                CMYMatrix[x][y][0] = CMYKMatrix[x][y][0] * (1 - k) + k;
                CMYMatrix[x][y][1] = CMYKMatrix[x][y][1] * (1 - k) + k;
                CMYMatrix[x][y][2] = CMYKMatrix[x][y][2] * (1 - k) + k;
            }
        }
        RGB_CMY rgb_cmy = new RGB_CMY();
        return rgb_cmy.deApply(new ImageMatrix(CMYMatrix, TypeOfImage.CMY));
    }

    @Override
    public ImageMatrix apply(ImageMatrix imageMatrix) {
        RGB_CMY fromCMY = new RGB_CMY();
        ImageMatrix CMYImageMatrix = fromCMY.apply(imageMatrix);
        int height = imageMatrix.getHeight();
        int width = imageMatrix.getWidth();
        double[][][] CMYMatrix = CMYImageMatrix.getMatrix();
        double[][][] CMYKMatrix = new double[height][width][4];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                double k = Math.min(CMYMatrix[x][y][0], Math.min(CMYMatrix[x][y][1], CMYMatrix[x][y][2]));
                CMYKMatrix[x][y][0] = (CMYMatrix[x][y][0] - k) / (1 - k);
                CMYKMatrix[x][y][1] = (CMYMatrix[x][y][1] - k) / (1 - k);
                CMYKMatrix[x][y][2] = (CMYMatrix[x][y][2] - k) / (1 - k);
                CMYKMatrix[x][y][3] = k;
            }
        }
        return new ImageMatrix(CMYKMatrix, TypeOfImage.CMYK);
    }
}
