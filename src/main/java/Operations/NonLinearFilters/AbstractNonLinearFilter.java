package Operations.NonLinearFilters;

import Model.ImageMatrix;
import Model.Mask;
import Model.MaskType;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class AbstractNonLinearFilter extends OperationFunction {
    @Override
    public Result apply(ImageMatrix input, LinkedHashMap<String, Object> params) throws Exception {
        MaskType maskType = (MaskType) params.get("maskType");
        Integer size = (Integer) params.get("maskSize");
        if (maskType == null || size == null) {
            throw new IllegalArgumentException("maskType and maskSize are required");
        }
        int radius = size / 2;
        List<java.awt.Point> offsets = Mask.getOffsets(maskType, radius);

        int h = input.getHeight();
        int w = input.getWidth();
        double[][][] src = input.getMatrix();
        double[][][] dst = new double[h][w][3];

        for (int c = 0; c < 3; c++) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    List<Double> window = new ArrayList<>();
                    for (java.awt.Point off : offsets) {
                        int yy = (y + off.x + h) % h;
                        int xx = (x + off.y + w) % w;
                        window.add(src[yy][xx][c]);
                    }
                    Collections.sort(window);
                    dst[y][x][c] = compute(window, params);
                }
            }
        }
        ImageMatrix output = new ImageMatrix(dst, input.getTypeOfImage());
        BufferedImage buf = output.toBufferedImage();
        JPanel panel = createPanelFromImage(buf);
        return new Result(panel, output);
    }

    protected abstract double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) throws Exception;
}
