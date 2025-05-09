package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class MedianFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        return sortedWindow.get(sortedWindow.size() / 2);
    }
}