package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class MidPointFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        double min = sortedWindow.get(0);
        double max = sortedWindow.get(sortedWindow.size() - 1);
        return (min + max) / 2.0;
    }
}