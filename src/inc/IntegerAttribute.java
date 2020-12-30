package inc;

import java.util.ArrayList;

public class IntegerAttribute implements CentralTendency {

    private ArrayList<Integer> values;
    private double mean,median,mode;

    public IntegerAttribute(ArrayList<Integer> values) {
        this.values = values;
        this.median = calculateMedian();
        this.mode = calculateMode();
        this.mean = calculateMedian();
    }

    public IntegerAttribute() {
        this.values = new ArrayList<>();
        this.mean = 0;
        this.median = 0;
        this.mode = 0;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMode() {
        return mode;
    }

    public void setMode(double mode) {
        this.mode = mode;
    }

    public int getValuesSum(){

        int sum = 0;

        for(Integer intgr : values){
            sum += intgr;
        }

        return sum;
    }

    @Override
    public double calculateMean() {
        return getValuesSum() / values.size();
    }

    @Override
    public double calculateMedian() {
        if(values.size() % 2 == 0)
            return (values.get(values.size() / 2) + values.get((values.size() + 1)/2))/2;
        else
            return values.get(values.size() / 2);
    }

    @Override
    public double calculateMode() {

        double maxValue=0, maxCount=0;
        for(Integer val1 : values){
            int count = 0;
            for(Integer val2 : values){
                if(val1 == val2){
                    count++;
                }
            }
            if(count > maxCount){
                maxCount = count;
                maxValue = val1;
            }
        }

        return maxValue;

    }
}
