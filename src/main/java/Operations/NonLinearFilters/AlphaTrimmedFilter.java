package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class AlphaTrimmedFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        Integer p = (Integer) params.get("p");
        int N = sortedWindow.size();
        int trim = p;
        double sum = 0;
        for (int i = trim; i < N - trim; i++) sum += sortedWindow.get(i);
        return sum / (N - 2 * trim);
    }
}