package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class ArithmeticMeanFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        double sum = 0;
        for (double v : sortedWindow) sum += v;
        return sum / sortedWindow.size();
    }
}
