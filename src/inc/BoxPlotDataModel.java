package inc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoxPlotDataModel {

    private ArrayList<Double> allValues;
    private ArrayList<Double> filteredValues;
    private double rawMax;
    private double rawMin;
    private double max;
    private double min;
    private double median;
    private double lowerQuartile;
    private double upperQuartile;
    private double average;
    private double lowerLimit;
    private double upperLimit;

    public ArrayList<Double> getAllValues() {
        return allValues;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getMedian() {
        return median;
    }

    public double getLowerQuartile() {
        return lowerQuartile;
    }

    public double getUpperQuartile() {
        return upperQuartile;
    }

    public double getAverage() {
        return average;
    }

    public double getRawMax() {
        return rawMax;
    }

    public double getRawMin() {
        return rawMin;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public BoxPlotDataModel(ArrayList<Double> values) {
        values.sort(Comparator.naturalOrder());
        this.allValues = values;
        rawMax = allValues.get(allValues.size() - 1);
        rawMin = allValues.get(0);
        findLimits(values);
        filteredValues = removeOutliers(values);
        filteredValues.sort(Comparator.naturalOrder());
        max = filteredValues.get(filteredValues.size() - 1);
        min = filteredValues.get(0);
        median = findMedian(filteredValues);
        lowerQuartile = findLowerQuartile(filteredValues);
        upperQuartile = findUpperQuartile(filteredValues);
        average = filteredValues.stream().mapToDouble(value -> value).average().orElse(0);
    }

    private void findLimits(ArrayList<Double> values) {
        double IQR = IQR(values);
        double lowerQuartile = findLowerQuartile(values);
        double upperQuartile = findUpperQuartile(values);
        lowerLimit = getLowerLimit(lowerQuartile, IQR);
        upperLimit = getUpperLimit(upperQuartile, IQR);
    }

    private ArrayList<Double> removeOutliers(ArrayList<Double> values) {
        double IQR = IQR(values);
        double lowerQuartile = findLowerQuartile(values);
        double upperQuartile = findUpperQuartile(values);
        double lowerLimit = getLowerLimit(lowerQuartile, IQR);
        double upperLimit = getUpperLimit(upperQuartile, IQR);
        Stream<Double> doubleStream = values.stream().filter(value -> value >= lowerLimit && value <= upperLimit);
        return doubleStream.collect(Collectors.toCollection(ArrayList::new));
    }

    private double findMedian(ArrayList<Double> values) {
        return percentile(values, 50);
    }

    private double findLowerQuartile(ArrayList<Double> values) {
        return percentile(values, 25);
    }

    private double findUpperQuartile(ArrayList<Double> values) {
        return percentile(values, 75);
    }

    private double IQR(ArrayList<Double> values) {
        double lowerQuartile = findLowerQuartile(values);
        double upperQuartile = findUpperQuartile(values);
        return upperQuartile - lowerQuartile;
    }

    private double getMin(ArrayList<Double> values) {
        Optional<Double> minOptional = values.stream().min(Comparator.naturalOrder());
        return minOptional.isPresent() ? minOptional.get() : 0;
    }

    private double getMax(ArrayList<Double> values) {
        Optional<Double> maxOptional = values.stream().max(Comparator.naturalOrder());
        return maxOptional.isPresent() ? maxOptional.get() : 0;
    }

    private double getLowerLimit(double lowerQuartile, double IQR) {
        return lowerQuartile - 1.5 * IQR;
    }

    private double getUpperLimit(double upperQuartile, double IQR) {
        return upperQuartile + 1.5 * IQR;
    }


    private static double percentile(ArrayList<Double> values, int percentile) {
        double index = values.size() * (percentile / 100D);
        index = Math.ceil(index);
        int finalIndex = (int) index - 1;
        return values.get(finalIndex);
    }

}