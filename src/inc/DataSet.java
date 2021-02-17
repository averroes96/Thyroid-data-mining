package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.HashMap;

// This class is the general concept of our entire dataset

public class DataSet implements Init,Cloneable {

    // rows is the list that contains the dataset's entire rows
    private ObservableList<Row> rows;

    // Constructors

    public DataSet() {
        rows = FXCollections.observableArrayList();
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


    public ObservableList<Double> getValuesByPosition(int currPostion) {

        ObservableList<Double> values = FXCollections.observableArrayList();

        for(Row row : rows)
            values.add(row.getValueByPosition(currPostion));

        return values;

    }

    public ObservableList<Double> normalizedData(){

        ObservableList<Double> temp = FXCollections.observableArrayList();
        int count = 0;
        double sum = 0.0;

        for(Row row : getRows()){
            for (int i = 0; i < 6; i++){
                count++;
                //System.out.println(getNormalizedValue(row.getValueByPosition(i), i));
                sum = sum + row.getValueByPosition(i);
            }
            //System.out.println(sum/count);
            temp.add(sum/count);
        }

        return temp;

    }

    public double getNormalizedValue(double val, int pos){

        //System.out.println(getArrtibuteMax(pos));
        //System.out.println(getArrtibuteMin(pos));

        return ((val - getArrtibuteMin(pos)) / (getArrtibuteMax(pos) - getArrtibuteMin(pos)));

    }

    public void discretize(int pos, int binSize){

        ObservableList<Double> values = Common.heapSort(getValuesByPosition(pos));

        HashMap<Integer, ArrayList<Double>> bins;
        ObservableList<Double> discretedData = FXCollections.observableArrayList();

        bins = equalFrequencyPartition(values, binSize);

        /*
        System.out.println("Before Smothing");

        for(Integer bin : bins.keySet()){
            System.out.println(bin + ":");
            for(Double val : bins.get(bin)){
                System.out.println(val);
            }
        }*/

        smoothingByBounderies(bins);

        /*
        System.out.println("After Smothing");

        for(Integer bin : bins.keySet()){
            System.out.println(bin + ":");
            for(Double val : bins.get(bin)){
                System.out.println(val);
            }
        }*/

        for(Integer bin : bins.keySet()){
            discretedData.addAll(bins.get(bin));
        }

        for(int i = 0; i < getRows().size(); i++) {
            if(!discretedData.contains(getRows().get(i).getValueByPosition(pos))){
                int cpt = 0;
                while(discretedData.get(cpt) < getRows().get(i).getValueByPosition(pos) && cpt < discretedData.size()){
                    cpt++;
                }

                if(cpt == 0)
                    getRows().get(i).set(pos, discretedData.get(0));
                else if(cpt == discretedData.size())
                    getRows().get(i).set(pos, discretedData.get(discretedData.size() - 1));
                else{
                    double minVal = getRows().get(i).getValueByPosition(pos) - discretedData.get(cpt - 1);
                    double maxVal = discretedData.get(cpt) - getRows().get(i).getValueByPosition(pos);
                    //System.out.println("Min= " + minVal);
                    //System.out.println("Max= " + maxVal);
                    if(minVal <= maxVal){
                        getRows().get(i).set(pos, discretedData.get(cpt - 1));
                    }
                    else
                        getRows().get(i).set(pos, discretedData.get(cpt));
                }
            }
        }

        /*
        ArrayList<Double> tempRows = new ArrayList<>();
        ArrayList<Double> tempDisc = new ArrayList<>();
        for (Double val : getValuesByPosition(pos)) {
            if(!tempRows.contains(val)){
                tempRows.add(val);
            }
        }

        for (Double val : discretedData) {
            if(!tempDisc.contains(val)){
                tempDisc.add(val);
            }
        }

        System.out.println(tempRows.size());
        System.out.println(tempDisc.size());*/

        /*
        for (Double val : discretedData){
            System.out.print(val + " ");
        }*/

    }

    private void smoothingByBounderies(HashMap<Integer, ArrayList<Double>> bins) {

        for(Integer bin : bins.keySet()){

            for(int i = 0; i < bins.get(bin).size(); i++){
                double minVal = bins.get(bin).get(i) - bins.get(bin).get(0);
                double maxVal = bins.get(bin).get(bins.get(bin).size() - 1) - bins.get(bin).get(i);
                //System.out.println("Min= " + minVal);
                //System.out.println("Max= " + maxVal);
                if(minVal <= maxVal){
                    bins.get(bin).set(i, bins.get(bin).get(0));
                }
                else
                    bins.get(bin).set(i, bins.get(bin).get(bins.get(bin).size() - 1));
            }
        }

    }

    private HashMap<Integer, ArrayList<Double>> equalFrequencyPartition(ObservableList<Double> values, int binSize) {

        HashMap<Integer, ArrayList<Double>> temp = new HashMap<>();
        ArrayList<Double> tempList = new ArrayList<>();
        int cpt = 0;

        for(int i = 1; i <= values.size(); i++){

            tempList.add(values.get(i-1));

            if( i % binSize == 0 || i == values.size()){
                temp.put(cpt, tempList);
                cpt++;
                tempList = new ArrayList<>();
            }
        }

        return temp;
    }

    public void IQR(){

        ObservableList<Double> temp = Common.heapSort(normalizedData());
        ObservableList<Double> inliers = FXCollections.observableArrayList();

        double median = 0.0;
        double Q1Median = 0,Q3Median = 0;
        boolean size = temp.size() % 2 == 0;
        double[] quantiles = calculateQMedians(Q1Median, Q3Median, temp);
        Q1Median = quantiles[0];
        Q3Median = quantiles[1];

        System.out.println("MIN = " + Q1Median + " MAX = " + Q3Median);
        if(size) {
            median = temp.get(temp.size() / 2);
        }
        else
            median = (temp.get(temp.size() / 2) + temp.get((temp.size() + 1) / 2)) / 2;

        int cpt = 0;
        double IQR = Q3Median - Q1Median;
        for(double val : temp){
            if(val >= Q1Median - 1.5*IQR && val <= Q3Median + 1.5*IQR)
                cpt++;
        }

        //System.out.println(cpt);
    }

    private double[] calculateQMedians(double Q1, double Q3, ObservableList<Double> temp) {

        int size = temp.size();
        int half = size / 2;
        ObservableList<Double> Q1List = FXCollections.observableArrayList();
        ObservableList<Double> Q3List = FXCollections.observableArrayList();

        if(size % 2 == 0){
            if(half % 2 == 0) {
                for (int i = 0; i < half; i++) {
                    Q1List.add(temp.get(i));
                }
                for (int i = half + 1; i < temp.size(); i++) {
                    Q3List.add(temp.get(i));
                }
            }
            else{
                System.out.println(half);
                for (int i = 0; i < half - 1; i++) {
                    Q1List.add(temp.get(i));
                }
                for (int i = half + 1; i <= temp.size() - 1 ; i++) {
                    //System.out.println(i);
                    Q3List.add(temp.get(i));
                }
            }
        }
        else{
            for (int i = 0; i < half; i++) {
                Q1List.add(temp.get(i));
            }
            for (int i = half + 1; i < temp.size(); i++) {
                Q3List.add(temp.get(i));
            }
        }

        if(Q1List.size() % 2 == 0)
            Q1 = (Q1List.get(Q1List.size() / 2) + Q1List.get((Q1List.size() / 2) + 1)) / 2;
        else
            Q1 = Q1List.get(Q1List.size() / 2);

        if(Q3List.size() % 2 == 0)
            Q3 = (Q3List.get(Q3List.size() / 2) + Q3List.get((Q3List.size() / 2) + 1)) / 2;
        else
            Q3 = Q3List.get(Q3List.size() / 2);

        int cpt = 0;
        for(Double val : Q1List){
            System.out.println("Q1 (" + cpt + ") => " + val);
            cpt++;
        }

        return new double[]{Q1, Q3};

    }

    @Override
    public DataSet clone() throws CloneNotSupportedException{

        DataSet ds = (DataSet)super.clone();
        ds.rows = FXCollections.observableArrayList();

        ds.rows.addAll(this.rows);

        return ds;
    }

    public int size(){
        return rows.size();
    }

    public ObservableList<Row> kNearest(int k, Row row, int distance) {

        HashMap<Row, Double> closest = new HashMap<>();
        ObservableList<Row> temp = FXCollections.observableArrayList();
        double max = 9999;
        for (Row tmp : rows) {
            double d = Distance.calculateDistance(distance, row, tmp, 6);
            if ( d < max  && !row.equals(tmp)) {
                closest.put(tmp, d);
                if (closest.size() > k)
                    max = removeFarthest(closest,distance);
            }

        }

        temp.addAll(closest.keySet());

        System.out.println(temp.size());
        return temp ;
    }

    private double removeFarthest(HashMap<Row, Double> closest, int distance) {

        Row tmp = null;// ; = vector.get(0);
        double max = 0;
        for (Row row : closest.keySet()) {
            double d = closest.get(row);

            if (max < d) {
                max = d;
                tmp = row;
            }
            // System.out.println("d="+d+"\t"+max);
        }
        closest.remove(tmp);

        return max;
    }

    public Row average() {

        Row tmpOut = new Row();

        for (int i = 0; i < 6; i++) {
            double sum=0;

            for (int j = 0; j < this.size(); j++) {
                sum += this.getRows().get(j).getValueByPosition(i);
            }
            tmpOut.set (i, sum / this.size());

        }
        return tmpOut;
    }
}
