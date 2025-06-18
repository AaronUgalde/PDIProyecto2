package Operations.MorphologicalOperations;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;
import GUI.OperationCategory;
import GUI.OperationInfo;
import GUI.ButtonConfig;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

public abstract class MorphologyOperation extends OperationFunction {
    @Override
    public Result apply(ImageMatrix original, LinkedHashMap<String, Object> params) throws Exception {
        StructuringElementFactory.MaskType type =
                (StructuringElementFactory.MaskType) params.get("maskType");
        int size = (Integer) params.get("maskSize");
        boolean[][] mask = StructuringElementFactory.create(type, size);

        double[][][] in = original.getMatrix();
        int h = original.getHeight(), w = original.getWidth();
        double[][][] out = new double[h][w][in[0][0].length];

        // Procesar canal por canal
        for (int c = 0; c < in[0][0].length; c++) {
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    out[i][j][c] = compute(i, j, c, in, mask);
                }
            }
        }
        ImageMatrix resImg = new ImageMatrix(out, original.getTypeOfImage());
        JPanel panel = createPanelFromImage(resImg.toBufferedImage());
        return new Result(panel, resImg);
    }

    // Cálculo morfológico en posición (i,j) para canal c
    protected abstract double compute(int i, int j, int c, double[][][] in, boolean[][] mask);
}