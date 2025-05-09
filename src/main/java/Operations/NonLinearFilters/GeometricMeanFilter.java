package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class GeometricMeanFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        double prodLog = 0;
        int N = sortedWindow.size();
        for (double v : sortedWindow) {
            prodLog += Math.log(v + 1e-12);
        }
        return Math.exp(prodLog / N);
    }
}