package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class HarmonicMeanFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        double sumInv = 0;
        for (double v : sortedWindow) {
            sumInv += 1.0 / (v + 1e-12);
        }
        return sortedWindow.size() / sumInv;
    }
}