package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class MinFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        return sortedWindow.get(0);
    }
}
