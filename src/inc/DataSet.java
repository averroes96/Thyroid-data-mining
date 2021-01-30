package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

// This class is the general concept of our entire dataset

public class DataSet implements Init {

    // rows is the list that contains the dataset's entire rows
    private ObservableList<Row> rows = FXCollections.observableArrayList();

    // Constructors

    public DataSet() {
    }

    public DataSet(ObservableList<Row> rows) {
        this.rows = rows;
    }

    // Getters and Setters

    public ObservableList<Row> getRows() {
        return rows;
    }

    public void setRows(ObservableList<Row> rows) {
        this.rows = rows;
    }

    // Method to calculate the mean of an attribute by a given position "attrPosition"
    public double calculateMean(int attrPosition){

        double mean = 0;

        mean = getAttributeSum(attrPosition) / rows.size();

        return mean;
    }

    // Method to calculate the median of an attribute by a given position "attrPosition"
    public double calculateMedian(int attrPosition){

        double median = 0;

        median = getAttributeMedian(attrPosition, rows.size() % 2 == 0 ? PAIR : ODD);

        return median;

    }

    // Method to calculate the mode of an attribute by a given position "attrPosition"
    public double calculateMode(int attrPosition){

        double maxValue=0, maxCount=0;

        for(Row val1 : rows){
            int count = 0;
            for(Row val2 : rows){
                if(val1.getValueByPosition(attrPosition) == val2.getValueByPosition(attrPosition)){
                    count++;
                }
            }
            if(count > maxCount){
                maxCount = count;
                maxValue = val1.getValueByPosition(attrPosition);
            }
        }

        return maxValue;

    }

    // Method to get an attribute's total sum
    private double getAttributeSum(int pos) {

        double sum = 0;

        switch (pos){
            case 0:
                for(Row row: rows){
                    sum += row.getAttributeClass();
                }
                break;
            case 1:
                for(Row row: rows){
                    sum += row.getT3ResinUptakeTest();
                }
                break;
            case 2:
                for(Row row: rows){
                    sum += row.getSerumThyroxin();
                }
                break;
            case 3:
                for(Row row: rows){
                    sum += row.getSerumTriiodothyronine();
                }
                break;
            case 4:
                for(Row row: rows){
                    sum += row.getTsh();
                }
                break;
            case 5:
                for(Row row: rows){
                    sum += row.getNewTsh();
                }
                break;
            default:

        }

        return sum;

    }

    // Method to get an attribute's median according to position and the dataset size "sizeType"
    // sizeType can be : 1 or 2 otherwise it will return 0
    private double getAttributeMedian(int pos, int sizeType){

        if(sizeType == PAIR) {

            switch (pos) {
                case 0:
                    return (double) (rows.get(rows.size() / 2).getAttributeClass() + rows.get((rows.size() + 1) / 2).getAttributeClass()) / 2;
                case 1:
                    return (double) (rows.get(rows.size() / 2).getT3ResinUptakeTest() + rows.get((rows.size() + 1) / 2).getT3ResinUptakeTest()) / 2;
                case 2:
                    return (rows.get(rows.size() / 2).getSerumThyroxin() + rows.get((rows.size() + 1) / 2).getSerumThyroxin()) / 2;
                case 3:
                    return (rows.get(rows.size() / 2).getSerumTriiodothyronine() + rows.get((rows.size() + 1) / 2).getSerumTriiodothyronine()) / 2;
                case 4:
                    return (rows.get(rows.size() / 2).getTsh() + rows.get((rows.size() + 1) / 2).getTsh()) / 2;
                case 5:
                    return (rows.get(rows.size() / 2).getNewTsh() + rows.get((rows.size() + 1) / 2).getNewTsh()) / 2;
                default:
                    return 0;
            }

        }
        else if(sizeType == ODD){
            switch (pos) {
                case 0:
                    return rows.get(rows.size() / 2).getAttributeClass();
                case 1:
                    return rows.get(rows.size() / 2).getT3ResinUptakeTest();
                case 2:
                    return rows.get(rows.size() / 2).getSerumThyroxin();
                case 3:
                    return rows.get(rows.size() / 2).getSerumTriiodothyronine();
                case 4:
                    return rows.get(rows.size() / 2).getTsh();
                case 5:
                    return rows.get(rows.size() / 2).getNewTsh();
                default:
                    return 0;
            }
        }
        else
            return 0;

    }

    public XYChart.Series<String,Integer> getOccurenceCount(int attrPosition){

        ArrayList<Double> values = getUniqueValues(attrPosition);
        ArrayList<Integer> counts = getOccurences(values, attrPosition);
        XYChart.Series<String,Integer> lineSeries = new XYChart.Series<>();

        for(int i = 0; i < values.size(); i++){
            lineSeries.getData().add(new XYChart.Data<>(String.valueOf(values.get(i)),counts.get(i)));
        }

        return lineSeries;

    }

    public XYChart.Series<String,Integer> getHistogramCount(int attrPosition){

        ArrayList<Double> values = getUniqueValues(attrPosition);
        ArrayList<String> ranges = new ArrayList<>();

        double range =  (values.get(values.size()-1) - values.get(0)) / 10;
        double minVal = values.get(0);
        double maxVal = values.get(values.size()-1);

        while(minVal < maxVal){
            double rangeEdge = minVal + range;
            minVal = Math.floor(minVal*1e2)/1e2;
            rangeEdge = Math.floor(rangeEdge*1e2)/1e2;
            ranges.add(minVal + " -> " + rangeEdge);
            minVal = rangeEdge;
        }

        ArrayList<Integer> counts = getHistogramOccurences(ranges, attrPosition);
        XYChart.Series<String,Integer> lineSeries = new XYChart.Series<>();

        for(int i = 0; i < ranges.size(); i++){
            lineSeries.getData().add(new XYChart.Data<>(String.valueOf(ranges.get(i)),counts.get(i)));
        }

        return lineSeries;

    }

    private ArrayList<Integer> getHistogramOccurences(ArrayList<String> ranges, int attrPosition) {

        ArrayList<Integer> temp = new ArrayList<>();

        for (String range : ranges){
            String[] edges = range.split("->");

            double minval = Float.parseFloat(edges[0]);
            double maxval = Float.parseFloat(edges[1]);
            double max = getArrtibuteMax(attrPosition);
            int cpt = 0;

            for(Row row : rows){
                if(maxval == max && row.getValueByPosition(attrPosition)>= minval)
                    cpt++;
                if(row.getValueByPosition(attrPosition) >= minval && row.getValueByPosition(attrPosition) < maxval){
                    cpt++;
                }
            }
            temp.add(cpt);
        }

        return temp;
    }

    private ArrayList<Integer> getOccurences(ArrayList<Double> values, int pos) {

        ArrayList<Integer> temp = new ArrayList<>();

        for(Double dbl : values){
            int cpt = 0;
            for(Row row : rows){
                if(row.getValueByPosition(pos) == dbl){
                    cpt++;
                }
            }
            temp.add(cpt);
        }

        return temp;

    }

    private ArrayList<Double> getUniqueValues(int pos) {

        ArrayList<Double> temp = new ArrayList<>();

        for(Row row : rows){
            if(!temp.contains(row.getValueByPosition(pos))){
                temp.add(row.getValueByPosition(pos));
            }
        }

        return temp;
    }

    public XYChart.Series<Number, Number> getScatterData(int currPostion, int yAttr) {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for(Row row : rows){
            series.getData().add(new XYChart.Data<>(row.getValueByPosition(currPostion), row.getValueByPosition(yAttr)));
        }

        return series;
    }

    public double getArrtibuteMin(int pos){

        double min = rows.get(0).getValueByPosition(pos);

        for(Row row : rows) {
            if(row.getValueByPosition(pos) < min)
                min = row.getValueByPosition(pos);
        }

        return min;
    }

    public double getArrtibuteMax(int pos){

        double max = rows.get(0).getValueByPosition(pos);

        for(Row row : rows) {
            if(row.getValueByPosition(pos) > max)
                max = row.getValueByPosition(pos);
        }

        return max;
    }


    private ArrayList<Double> getValuesByPosition(int currPostion) {

        ArrayList<Double> values = new ArrayList<>();

        for(Row row : rows)
            values.add(row.getValueByPosition(currPostion));

        return values;

    }


}
