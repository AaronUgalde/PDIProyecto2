package Operations.ConvolutionOperations;

import java.util.LinkedHashMap;

/**
 * Filtros pasa-altas y realce de bordes: Sharpen 1–3, Sobel, Prewitt, Roberts, Frei-Chen,
 * compass Prewitt, Robinson 3 & 5, Kirsch, homogeneidad y diferencia.
 */
public class HighPassConvolution extends BaseConvolutionOperation {

    @Override
    protected double[][] fetchKernel(LinkedHashMap<String, Object> params) {
        String type = ((String) params.get("type")).toLowerCase();
        switch (type) {
            // —— Sharpening ——
            case "sharpen1":
                return new double[][] {
                        {  1, -2,  1 },
                        { -2,  5, -2 },
                        {  1, -2,  1 }
                };
            case "sharpen2":
                return new double[][] {
                        {  0, -1,  0 },
                        { -1,  5, -1 },
                        {  0, -1,  0 }
                };
            case "sharpen3":
                return new double[][] {
                        { -1, -1, -1 },
                        { -1,  9, -1 },
                        { -1, -1, -1 }
                };

            // —— Sobel ——
            case "sobelx":     return new double[][]{{1,0,-1},{2,0,-2},{1,0,-1}};
            case "sobely":     return new double[][]{{-1,-2,-1},{0,0,0},{1,2,1}};

            // —— Prewitt ——
            case "prewittx":   return new double[][]{{1,0,-1},{1,0,-1},{1,0,-1}};
            case "prewitty":   return new double[][]{{-1,-1,-1},{0,0,0},{1,1,1}};

            // —— Roberts ——
            case "robertsx":   return new double[][]{{0,0,-1},{0,1,0},{0,0,0}};
            case "robertsy":   return new double[][]{{-1,0,0},{0,1,0},{0,0,0}};

            // —— Frei-Chen ——
            case "freichenx":  return new double[][]{{1,0,-1},{Math.sqrt(2),0,-Math.sqrt(2)},{1,0,-1}};
            case "freicheny":  return new double[][]{{-1,-Math.sqrt(2),-1},{0,0,0},{1,Math.sqrt(2),1}};

            // —— Otros ——
            case "homogeneidad": return new double[][]{{0,-1,0},{-1,1,-1},{0,-1,0}};
            case "diferencia":   return new double[][]{{0,-1,0},{-1,1,0},{0,0,0}};

            // —— Kernels direccionales ——
            case "prewitt_compass":
                return compass(params, new double[][][] {
                        {{1,1,-1},{1,-2,-1},{1,1,-1}},  // E
                        {{1,-1,-1},{1,-2,-1},{1,1,1}},  // NE
                        {{-1,-1,-1},{1,-2,1},{1,1,1}},  // N
                        {{-1,-1,1},{-1,-2,1},{1,1,1}},  // NO
                        {{-1,1,1},{-1,-2,1},{-1,1,1}},  // O
                        {{1,1,1},{-1,-2,1},{-1,-1,1}},  // SO
                        {{1,1,1},{1,-2,1},{-1,-1,-1}},  // S
                        {{1,1,1},{1,-2,-1},{1,-1,-1}}   // SE
                });
            case "robinson3":
                return compass(params, new double[][][] {
                        {{1,0,-1},{1,0,-1},{1,0,-1}},
                        {{0,-1,-1},{1,0,-1},{1,1,0}},
                        {{-1,-1,-1},{0,0,0},{1,1,1}},
                        {{-1,-1,0},{-1,0,1},{0,1,1}},
                        {{-1,0,1},{-1,0,1},{-1,0,1}},
                        {{0,1,1},{-1,0,1},{-1,-1,0}},
                        {{1,1,1},{0,0,0},{-1,-1,-1}},
                        {{1,1,0},{1,0,-1},{0,-1,-1}}
                });
            case "robinson5":
                return compass(params, new double[][][] {
                        {{1,0,-1},{2,0,-2},{1,0,-1}},
                        {{0,-1,-2},{1,0,-1},{2,1,0}},
                        {{-1,-2,-1},{0,0,0},{1,2,1}},
                        {{-2,-1,0},{-1,0,1},{0,1,2}},
                        {{-1,0,1},{-2,0,2},{-1,0,1}},
                        {{0,1,2},{-1,0,1},{-2,-1,0}},
                        {{1,2,1},{0,0,0},{-1,-2,-1}},
                        {{2,1,0},{1,0,-1},{0,-1,-2}}
                });
            case "kirsch":
                return compass(params, new double[][][] {
                        {{5,-3,-3},{5,0,-3},{5,-3,-3}},
                        {{-3,-3,-3},{5,0,-3},{5,5,-3}},
                        {{-3,-3,-3},{-3,0,-3},{5,5,5}},
                        {{-3,-3,-3},{-3,0,5},{-3,5,5}},
                        {{-3,-3,5},{-3,0,5},{-3,-3,5}},
                        {{-3,5,5},{-3,0,5},{-3,-3,-3}},
                        {{5,5,5},{-3,0,-3},{-3,-3,-3}},
                        {{5,5,-3},{5,0,-3},{-3,-3,-3}}
                });
            default:
                throw new IllegalArgumentException("High-pass desconocido: " + type);
        }
    }

    private double[][] compass(LinkedHashMap<String, Object> params, double[][][] kernels) {
        String dir = ((String) params.get("direction")).toUpperCase();
        String[] dirs = {"E","NE","N","NO","O","SO","S","SE"};
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i].equals(dir)) return kernels[i];
        }
        throw new IllegalArgumentException("Dirección inválida: " + dir);
    }
}
