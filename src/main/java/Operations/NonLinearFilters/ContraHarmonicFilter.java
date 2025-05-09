package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class ContraHarmonicFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        Double Q = (Double) params.get("q");
        double num = 0, den = 0;
        for (double v : sortedWindow) {
            num += Math.pow(v, Q + 1);
            den += Math.pow(v, Q);
        }
        return num / (den + 1e-12);
    }
}

