package Operations.NonLinearFilters;
import java.util.List;
import java.util.LinkedHashMap;

public class MaxMinFilter extends AbstractNonLinearFilter {
    @Override
    protected double compute(List<Double> sortedWindow, LinkedHashMap<String, Object> params) {
        double min = sortedWindow.get(0);
        double max = sortedWindow.get(sortedWindow.size() - 1);
        double center = sortedWindow.get(sortedWindow.size()/2);
        double diffMax = Math.abs(center - max);
        double diffMin = Math.abs(center - min);
        return diffMax < diffMin ? max : min;
    }
}