package Operations;

import Model.ImageMatrix;
import Model.TypeOfImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Stack;

public class CannyOperation extends OperationFunction {

    @Override
    public Result apply(ImageMatrix originalImage, LinkedHashMap<String, Object> params) throws Exception {
        // Parámetros: sigma, kernelSize (odd), lowThreshold, highThreshold
        double sigma = params.containsKey("sigma") ? (double) params.get("sigma") : 1.0;
        int kernelSize = params.containsKey("kernelSize") ? (int) params.get("kernelSize") : (int) (6 * sigma + 1) | 1;
        double lowThreshold = params.containsKey("lowThreshold") ? (double) params.get("lowThreshold") : 0.05;
        double highThreshold = params.containsKey("highThreshold") ? (double) params.get("highThreshold") : 0.15;

        // 1. Convertir a escala de grises
        double[][] gray = toGrayscale(originalImage);

        // 2. Suavizar con filtro Gaussiano
        double[][] gaussian = gaussianKernel(kernelSize, sigma);
        double[][] smoothed = convolve(gray, gaussian);

        // 3. Cálculo de gradientes
        double[][] gx = convolve(smoothed, SOBEL_X);
        double[][] gy = convolve(smoothed, SOBEL_Y);

        int height = gray.length;
        int width = gray[0].length;

        // 4. Magnitud y ángulo
        double[][] magnitude = new double[height][width];
        double[][] angle = new double[height][width];
        double maxMag = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double dx = gx[i][j];
                double dy = gy[i][j];
                double mag = Math.hypot(dx, dy);
                magnitude[i][j] = mag;
                maxMag = Math.max(maxMag, mag);
                angle[i][j] = Math.toDegrees(Math.atan2(dy, dx));
            }
        }

        // 5. Supresión no máxima
        double[][] nms = nonMaxSuppression(magnitude, angle);

        // 6. Umbralización y histéresis
        boolean[][] edges = hysteresis(nms, lowThreshold * maxMag, highThreshold * maxMag);

        // 7. Crear imagen binaria de bordes
        BufferedImage edgeImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = edges[y][x] ? 0xFFFFFF : 0x000000;
                edgeImg.setRGB(x, y, val);
            }
        }

        // 8. Empaquetar resultado
        JPanel panel = createPanelFromImage(edgeImg);
        ImageMatrix resultMatrix = new ImageMatrix(edgeImg);
        return new Result(panel, resultMatrix);
    }

    private double[][] toGrayscale(ImageMatrix img) {
        int h = img.getHeight();
        int w = img.getWidth();
        double[][] gray = new double[h][w];
        double[][][] m = img.getMatrix();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                double r = m[i][j][0];
                double g = m[i][j][1];
                double b = m[i][j][2];
                gray[i][j] = 0.299 * r + 0.587 * g + 0.114 * b;
            }
        }
        return gray;
    }

    private double[][] convolve(double[][] input, double[][] kernel) {
        int h = input.length;
        int w = input[0].length;
        int k = kernel.length;
        int pad = k / 2;
        double[][] output = new double[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                double sum = 0;
                for (int ki = 0; ki < k; ki++) {
                    for (int kj = 0; kj < k; kj++) {
                        int y = i + ki - pad;
                        int x = j + kj - pad;
                        if (y >= 0 && y < h && x >= 0 && x < w) {
                            sum += input[y][x] * kernel[ki][kj];
                        }
                    }
                }
                output[i][j] = sum;
            }
        }
        return output;
    }

    private double[][] gaussianKernel(int size, double sigma) {
        double[][] kernel = new double[size][size];
        int half = size / 2;
        double sum = 0;
        double s2 = 2 * sigma * sigma;
        for (int i = -half; i <= half; i++) {
            for (int j = -half; j <= half; j++) {
                double val = Math.exp(-(i * i + j * j) / s2) / (Math.PI * s2);
                kernel[i + half][j + half] = val;
                sum += val;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= sum;
            }
        }
        return kernel;
    }

    private double[][] nonMaxSuppression(double[][] mag, double[][] angle) {
        int h = mag.length;
        int w = mag[0].length;
        double[][] out = new double[h][w];
        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                double grad = mag[i][j];
                double ang = angle[i][j];
                // Normalizar ángulo
                if (ang < 0) ang += 180;
                double m1 = 0, m2 = 0;
                // 4 direcciones
                if ((ang >= 0 && ang < 22.5) || (ang >= 157.5 && ang <= 180)) {
                    m1 = mag[i][j - 1];
                    m2 = mag[i][j + 1];
                } else if (ang >= 22.5 && ang < 67.5) {
                    m1 = mag[i - 1][j + 1];
                    m2 = mag[i + 1][j - 1];
                } else if (ang >= 67.5 && ang < 112.5) {
                    m1 = mag[i - 1][j];
                    m2 = mag[i + 1][j];
                } else {
                    m1 = mag[i - 1][j - 1];
                    m2 = mag[i + 1][j + 1];
                }
                out[i][j] = (grad >= m1 && grad >= m2) ? grad : 0;
            }
        }
        return out;
    }

    private boolean[][] hysteresis(double[][] nms, double low, double high) {
        int h = nms.length;
        int w = nms[0].length;
        boolean[][] strong = new boolean[h][w];
        boolean[][] weak = new boolean[h][w];
        // Clasificar píxeles
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (nms[i][j] >= high) strong[i][j] = true;
                else if (nms[i][j] >= low) weak[i][j] = true;
            }
        }
        // Conectividad 8 para histéresis
        boolean[][] edges = new boolean[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (strong[i][j]) {
                    dfs(i, j, strong, weak, edges);
                }
            }
        }
        return edges;
    }

    private void dfs(int i, int j, boolean[][] strong, boolean[][] weak, boolean[][] edges) {
        int h = strong.length;
        int w = strong[0].length;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{i, j});
        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            int y = p[0], x = p[1];
            if (edges[y][x]) continue;
            edges[y][x] = true;
            // Revisar vecinos
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int ny = y + dy, nx = x + dx;
                    if (ny >= 0 && ny < h && nx >= 0 && nx < w && !edges[ny][nx] && weak[ny][nx]) {
                        stack.push(new int[]{ny, nx});
                    }
                }
            }
        }
    }

    private static final double[][] SOBEL_X = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final double[][] SOBEL_Y = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
    };
}
