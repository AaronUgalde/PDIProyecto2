package Model;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Mask {
    public static List<Point> getOffsets(MaskType type, int radius) {
        List<Point> offsets = new ArrayList<>();
        switch(type) {
            case HORIZONTAL:
                for (int dx = -radius; dx <= radius; dx++) offsets.add(new Point(0, dx));
                break;
            case VERTICAL:
                for (int dy = -radius; dy <= radius; dy++) offsets.add(new Point(dy, 0));
                break;
            case CROSS:
                for (int dx = -radius; dx <= radius; dx++) offsets.add(new Point(0, dx));
                for (int dy = -radius; dy <= radius; dy++) if (dy != 0) offsets.add(new Point(dy, 0));
                break;
            case SQUARE:
                for (int dy = -radius; dy <= radius; dy++)
                    for (int dx = -radius; dx <= radius; dx++)
                        offsets.add(new Point(dy, dx));
                break;
            case X:
                for (int d = -radius; d <= radius; d++) offsets.add(new Point(d, d));
                for (int d = -radius; d <= radius; d++) if (d != 0) offsets.add(new Point(d, -d));
                break;
            case DIAMOND:
                for (int dy = -radius; dy <= radius; dy++) {
                    int span = radius - Math.abs(dy);
                    for (int dx = -span; dx <= span; dx++) offsets.add(new Point(dy, dx));
                }
                break;
        }
        return offsets;
    }
}