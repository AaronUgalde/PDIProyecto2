package Operations.MorphologicalOperations;

public class DilationOperation extends MorphologyOperation {
    @Override
    protected double compute(int i, int j, int c, double[][][] in, boolean[][] mask) {
        int size = mask.length;
        int r = size/2;
        double max = -Double.MAX_VALUE;
        for (int mi = 0; mi < size; mi++) for (int mj = 0; mj < size; mj++) {
            if (!mask[mi][mj]) continue;
            int y = i + mi - r, x = j + mj - r;
            if (y<0||y>=in.length||x<0||x>=in[0].length) continue;
            max = Math.max(max, in[y][x][c]);
        }
        return (max==-Double.MAX_VALUE ? in[i][j][c] : max);
    }
}