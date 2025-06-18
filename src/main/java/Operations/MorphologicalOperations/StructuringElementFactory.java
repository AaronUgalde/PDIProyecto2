package Operations.MorphologicalOperations;
/**
 * Fábrica de Elementos Estructurantes.
 */
public class StructuringElementFactory {
    public enum MaskType { SQUARE, CROSS, CIRCLE }

    public static boolean[][] create(MaskType type, int size) {
        int radius = size / 2;
        boolean[][] mask = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int di = i - radius;
                int dj = j - radius;
                switch (type) {
                    case SQUARE:
                        mask[i][j] = true;
                        break;
                    case CROSS:
                        mask[i][j] = (di == 0 || dj == 0);
                        break;
                    case CIRCLE:
                        mask[i][j] = (di*di + dj*dj) <= radius*radius;
                        break;
                }
            }
        }
        return mask;
    }
}