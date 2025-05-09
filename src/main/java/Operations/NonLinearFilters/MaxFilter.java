package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class MaxFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        return sortedWindow.get(sortedWindow.size() - 1);
    }
}