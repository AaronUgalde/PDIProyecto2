package Operations.MorphologicalOperations;

public class ErosionOperation extends MorphologyOperation {
    @Override
    protected double compute(int i, int j, int c, double[][][] in, boolean[][] mask) {
        int size = mask.length;
        int r = size/2;
        double min = Double.MAX_VALUE;
        for (int mi = 0; mi < size; mi++) for (int mj = 0; mj < size; mj++) {
            if (!mask[mi][mj]) continue;
            int y = i + mi - r, x = j + mj - r;
            if (y<0||y>=in.length||x<0||x>=in[0].length) continue;
            min = Math.min(min, in[y][x][c]);
        }
        return (min==Double.MAX_VALUE ? in[i][j][c] : min);
    }
}