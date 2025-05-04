package Operations.ConvolutionOperations;

import java.util.LinkedHashMap;

/**
 * Operaciones de filtros pasa-bajas: box, gaussiano y suavizados 7×7, 9×9, 11×11.
 */
public class LowPassConvolution extends BaseConvolutionOperation {

    @Override
    protected double[][] fetchKernel(LinkedHashMap<String, Object> params) {
        String type = (String) params.get("type");
        switch (type.toLowerCase()) {
            case "box": {
                int size = (Integer) params.getOrDefault("size", 3);
                double v = 1.0 / (size * size);
                double[][] k = new double[size][size];
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++)
                        k[i][j] = v;
                return k;
            }
            case "gauss": {
                int size = (Integer) params.getOrDefault("size", 5);
                double sigma = (Double) params.getOrDefault("sigma", 1.0);
                return genGauss(size, sigma);
            }
            case "suavizado7":
                return genPattern(7, new int[]{
                        0,0,1,1,1,0,0,
                        0,1,1,1,1,1,0,
                        1,1,1,1,1,1,1,
                        1,1,1,6,1,1,1,
                        1,1,1,1,1,1,1,
                        0,1,1,1,1,1,0,
                        0,0,1,1,1,0,0
                });
            case "suavizado9":
                return genPattern(9, new int[]{
                        0,0,1,1,1,1,1,0,0,
                        0,1,1,1,1,1,1,1,0,
                        1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,
                        1,1,1,1,9,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,
                        0,1,1,1,1,1,1,1,0,
                        0,0,1,1,1,1,1,0,0
                });
            case "suavizado11":
                return genPattern(11, new int[]{
                        0,0,1,1,1,1,1,1,1,0,0,
                        0,1,1,1,1,1,1,1,1,1,0,
                        1,1,1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,9,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,1,1,
                        1,1,1,1,1,1,1,1,1,1,1,
                        0,1,1,1,1,1,1,1,1,1,0,
                        0,0,1,1,1,1,1,1,1,0,0
                });
            default:
                throw new IllegalArgumentException("Low-pass desconocido: " + type);
        }
    }

    private double[][] genGauss(int n, double sigma) {
        double[][] k = new double[n][n];
        int c = n/2;
        double sum = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                double v = Math.exp(-((x - c)*(x - c) + (y - c)*(y - c)) / (2 * sigma * sigma));
                k[y][x] = v;
                sum += v;
            }
        }
        for (int y = 0; y < n; y++)
            for (int x = 0; x < n; x++)
                k[y][x] /= sum;
        return k;
    }

    private double[][] genPattern(int n, int[] data) {
        double[][] k = new double[n][n];
        double sum = 0;
        int idx = 0;
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            k[i][j] = data[idx++];
            sum += k[i][j];
        }
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) k[i][j] /= sum;
        return k;
    }
}