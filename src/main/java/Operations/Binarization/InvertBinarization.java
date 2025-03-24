package Operations.Binarization;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.util.LinkedHashMap;

public class InvertBinarization extends OperationFunction {
    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        if(originalImage.getTypeOfImage() != TypeOfImage.YIQ){
            throw new IllegalArgumentException("YIQ image is not YIQ");
        }
        ImageMatrix resultImage = invertChannel(originalImage, 0);
        JPanel resultPanel = convertToGrayscale(resultImage, 0);
        return new Result(resultPanel, resultImage);
    }
}
