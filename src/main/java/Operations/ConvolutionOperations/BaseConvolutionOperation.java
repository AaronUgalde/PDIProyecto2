package Operations.ConvolutionOperations;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/**
 * Clase base que implementa la lógica genérica de convolución.
 */
public abstract class BaseConvolutionOperation extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        double[][] kernel = fetchKernel(params);
        validateKernel(kernel);
        if ((Boolean) params.getOrDefault("normalize", Boolean.TRUE)) {
            kernel = normalizeKernel(kernel);
        }

        double[][] resultGray = convolve(originalImage.getMatrix(), originalImage.getWidth(), originalImage.getHeight(), kernel);
        double[][][] outMatrix = toRgbMatrix(resultGray);

        ImageMatrix outImg = new ImageMatrix(outMatrix, TypeOfImage.RGB);
        BufferedImage buf = outImg.toBufferedImage();
        JPanel panel = createPanelFromImage(buf);
        return new Result(panel, outImg);
    }

    protected abstract double[][] fetchKernel(LinkedHashMap<String, Object> params);

    private void validateKernel(double[][] k) {
        if (k == null || k.length % 2 == 0 || k.length != k[0].length) {
            throw new IllegalArgumentException("El kernel debe ser cuadrado de dimensión impar");
        }
    }

    private double[][] convolve(double[][][] yiq, int w, int h, double[][] kernel) {
        int k = kernel.length, r = k / 2;
        double[][] out = new double[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double sum = 0;
                for (int i = -r; i <= r; i++) {
                    for (int j = -r; j <= r; j++) {
                        int yy = reflect(y + i, h);
                        int xx = reflect(x + j, w);
                        sum += yiq[yy][xx][0] * kernel[i + r][j + r];
                    }
                }
                out[y][x] = clamp01(sum);
            }
        }
        return out;
    }

    private int reflect(int p, int max) {
        if (p < 0) return -p - 1;
        if (p >= max) return 2 * max - p - 1;
        return p;
    }

    private double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    private double[][][] toRgbMatrix(double[][] gray) {
        int h = gray.length, w = gray[0].length;
        double[][][] rgb = new double[h][w][3];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                rgb[y][x][0] = gray[y][x];
                rgb[y][x][1] = gray[y][x];
                rgb[y][x][2] = gray[y][x];
            }
        }
        return rgb;
    }

    private double[][] normalizeKernel(double[][] k) {
        double sum = 0;
        for (double[] row : k) for (double v : row) sum += v;
        if (sum == 0) sum = 1;
        int n = k.length;
        double[][] out = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out[i][j] = k[i][j] / sum;
            }
        }
        return out;
    }
}

