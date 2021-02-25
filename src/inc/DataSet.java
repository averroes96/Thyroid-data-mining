package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.HashMap;

// This class is the general concept of our entire dataset

public class DataSet implements Init,Cloneable {

    // rows is the list that contains the dataset's entire rows
    ObservableList<Row> rows;
    int numFeatures;

    // Constructors

    public DataSet(int numFeatures) {
        rows = FXCollections.observableArrayList();
        for(Row row : rows){
            row = new Row(numFeatures);
        }
    }

    public DataSet(ObservableList<Row> rows) {
        this.rows = rows;
        numFeatures = this.rows.get(0).nbrFeatures;
    }

    public DataSet() {
        rows = FXCollections.observableArrayList();
        numFeatures = 0;
    }

    // Getters and Setters

    public ObservableList<Row> getRows() {
        return rows;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
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
                if(val1.values[attrPosition] == val2.values[attrPosition]){
                    count++;
                }
            }
            if(count > maxCount){
                maxCount = count;
                maxValue = val1.values[attrPosition];
            }
        }

        return maxValue;

    }

    // Method to get an attribute's total sum
    private double getAttributeSum(int pos) {

        double sum = 0;

        for(Row row: rows){
            sum += row.values[pos];
        }

        return sum;

    }

    // Method to get an attribute's median according to position and the dataset size "sizeType"
    // sizeType can be : 1 or 2 otherwise it will return 0
    private double getAttributeMedian(int pos, int sizeType){

        if(sizeType == PAIR)
            return (rows.get(rows.size() / 2).values[pos] + rows.get((rows.size() + 1) / 2).values[pos]) / 2;
        else if (sizeType == ODD)
            return rows.get(rows.size() / 2).values[pos];
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
                if(maxval == max && row.values[attrPosition] >= minval)
                    cpt++;
                if(row.values[attrPosition] >= minval && row.values[attrPosition] < maxval){
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
                if(row.values[pos] == dbl){
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
            if(!temp.contains(row.values[pos])){
                temp.add(row.values[pos]);
            }
        }

        return temp;
    }

    public XYChart.Series<Number, Number> getScatterData(int currPostion, int yAttr) {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for(Row row : rows){
            series.getData().add(new XYChart.Data<>(row.values[currPostion], row.values[yAttr]));
        }

        return series;
    }

    public double getArrtibuteMin(int pos){

        double min = rows.get(0).values[pos];

        for(Row row : rows) {
            if(row.values[pos] < min)
                min = row.values[pos];
        }

        return min;
    }

    public double getArrtibuteMax(int pos){

        double max = rows.get(0).values[pos];

        for(Row row : rows) {
            if(row.values[pos] > max)
                max = row.values[pos];
        }

        return max;
    }


    public ObservableList<Double> getValuesByPosition(int currPostion) {

        ObservableList<Double> values = FXCollections.observableArrayList();

        for(Row row : rows)
            values.add(row.values[currPostion]);

        return values;

    }

    public ObservableList<Double> normalizedData(){

        ObservableList<Double> temp = FXCollections.observableArrayList();
        double sum;

        for(Row row : getRows()){
            sum = 0;
            for (int i = 1; i < 6; i++){
                sum = sum + row.values[i];
            }
            temp.add(sum/5);
        }

        return temp;

    }

    public double getNormalizedValue(double val, int pos){

        return ((val - getArrtibuteMin(pos)) / (getArrtibuteMax(pos) - getArrtibuteMin(pos)));

    }

    public void discretize(int pos, int binSize){

        ObservableList<Double> values = Common.heapSort(getValuesByPosition(pos));

        HashMap<Integer, ArrayList<Double>> bins;
        ObservableList<Double> discretedData = FXCollections.observableArrayList();

        bins = equalFrequencyPartition(values, binSize);

        smoothingByBounderies(bins);

        for(Integer bin : bins.keySet()){
            discretedData.addAll(bins.get(bin));
        }

        for(int i = 0; i < getRows().size(); i++) {
            if(!discretedData.contains(getRows().get(i).values[pos])){
                int cpt = 0;
                while(discretedData.get(cpt) < getRows().get(i).values[pos] && cpt < discretedData.size()){
                    cpt++;
                }

                if(cpt == 0)
                    getRows().get(i).set(pos, discretedData.get(0));
                else if(cpt == discretedData.size())
                    getRows().get(i).set(pos, discretedData.get(discretedData.size() - 1));
                else{
                    double minVal = getRows().get(i).values[pos] - discretedData.get(cpt - 1);
                    double maxVal = discretedData.get(cpt) - getRows().get(i).values[pos];
                    if(minVal <= maxVal){
                        getRows().get(i).set(pos, discretedData.get(cpt - 1));
                    }
                    else
                        getRows().get(i).set(pos, discretedData.get(cpt));
                }
            }
        }

    }

    private void smoothingByBounderies(HashMap<Integer, ArrayList<Double>> bins) {

        for(Integer bin : bins.keySet()){

            for(int i = 0; i < bins.get(bin).size(); i++){
                double minVal = bins.get(bin).get(i) - bins.get(bin).get(0);
                double maxVal = bins.get(bin).get(bins.get(bin).size() - 1) - bins.get(bin).get(i);
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

    public ObservableList<Row> IQR(){

        ObservableList<Double> temp = Common.heapSort(normalizedData());
        ObservableList<Row> inliers = FXCollections.observableArrayList();

        double Q1Median,Q3Median;
        double[] quantiles = calculateQMedians(temp);
        Q1Median = quantiles[0];
        Q3Median = quantiles[1];

        System.out.println("MIN = " + Q1Median + " MAX = " + Q3Median);

        double IQR = Q3Median - Q1Median;
        for(Row row : this.rows){
            double sum = 0;
            for(int i = 1; i < 6; i++){
                sum += row.values[i];
            }
            double val = sum / 5;

            //System.out.println(val);

            if(val > Q1Median - (1.5*IQR) && val < Q3Median + (1.5*IQR))
                inliers.add(row);
        }

        System.out.println(inliers.size());
        return inliers;
    }

    private double[] calculateQMedians(ObservableList<Double> temp) {

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
                for (int i = 0; i < half - 1; i++) {
                    Q1List.add(temp.get(i));
                }
                for (int i = half + 1; i <= temp.size() - 1 ; i++) {
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

        double Q1;
        if(Q1List.size() % 2 == 0)
            Q1 = (Q1List.get(Q1List.size() / 2) + Q1List.get((Q1List.size() / 2) + 1)) / 2;
        else
            Q1 = Q1List.get(Q1List.size() / 2);

        double Q3;
        if(Q3List.size() % 2 == 0)
            Q3 = (Q3List.get(Q3List.size() / 2) + Q3List.get((Q3List.size() / 2) + 1)) / 2;
        else
            Q3 = Q3List.get(Q3List.size() / 2);

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
        }
        closest.remove(tmp);

        return max;
    }

    public Row average() {

        Row tmpOut = new Row(numFeatures);

        for (int i = 0; i < 6; i++) {
            double sum=0;

            for (int j = 0; j < this.size(); j++) {
                sum += this.getRows().get(j).values[i];
            }
            tmpOut.set (i, sum / this.size());

        }
        return tmpOut;
    }

    public void getMinCost(){

        ObservableList<Row> first = getRowsByClass(1);
        ObservableList<Row> second = getRowsByClass(2);
        ObservableList<Row> third = getRowsByClass(3);


        Row firstMean = getMeanByClass(1, first);
        Row secondMean = getMeanByClass(2, second);
        Row thirdMean = getMeanByClass(3, third);

        double firstCost = getCostByClass(first, firstMean);
        double secondCost = getCostByClass(second, secondMean);
        double thirdCost = getCostByClass(third, thirdMean);
    }

    private double getCostByClass(ObservableList<Row> temp, Row mean) {

        double distance;
        double cost = 0;
        for(Row row : temp){
            distance = Distance.calculateDistance(EUCLEDIAN, mean, row, 6);
            cost += distance;
        }

        return cost;
    }

    private Row getMeanByClass(int classNumber, ObservableList<Row> temp) {

        double globalSum;
        double localSum;
        Row row = new Row(numFeatures);
        row.set(0, classNumber);
        for(int i = 1; i < 6; i++) {
            localSum = 0;
            globalSum = 0;
            for (Row tmp : temp) {
                localSum += tmp.values[i];
            }
            globalSum += localSum / temp.size();
            row.set(i, globalSum);
        }

        return row;
    }

    private ObservableList<Row> getRowsByClass(int classNumber) {

        ObservableList<Row> temp = FXCollections.observableArrayList();

        for(Row row : rows){
            if(row.values[0] == classNumber)
                temp.add(row);
        }

        return temp;
    }
}
