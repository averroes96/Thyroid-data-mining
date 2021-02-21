package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Random;

public class KMeans implements Init {

    int k;
    int maxIters;
    int noChanges;
    double cost;
    int distance;
    int numFeatures;
    DataSet dataSet;
    HashMap<Integer, Row> centroids = new HashMap<>();
    HashMap<Row, Integer> clusters = new HashMap<>();

    public KMeans() {
        k = 1;
        maxIters = 1;
        numFeatures = 1;
        distance = EUCLEDIAN;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getMaxIters() {
        return maxIters;
    }

    public void setMaxIters(int maxIters) {
        this.maxIters = maxIters;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void initiateCentroids(){

        for(int i = 0; i < k; i++){
            Random rand = new Random();
            int randInt = rand.nextInt(dataSet.getRows().size());
            centroids.put(i, dataSet.getRows().get(randInt));
        }
    }

    public void init(ObservableList<Row> rows, int distance, HashMap<Integer, Row> centroids, int k) {

        int k1 = 0;
        double dist = 0;
        cost = 0;

        for(Row row : rows) {
            double minimum = 999999.0;
            for (int j = 0; j < k; j++) {
                if(distance == EUCLEDIAN){
                    dist = Distance.eucledianDistance(centroids.get(j), row, numFeatures);
                }
                else if(distance == MANHATTAN){
                    dist = Distance.manhattanDistance(centroids.get(j), row,  numFeatures);
                }
                if (dist < minimum) {
                    minimum = dist;
                    k1 = j;
                }

            }
            cost += minimum;
            clusters.put(row, k1);
        }

    }

    public Row centroidCalculator(ObservableList<Row> rows) {

        double sum;

        Row centroids = new Row();
        for (int i = 0; i < numFeatures; i++) {
            sum = 0.0;
            for(Row row : rows){
                sum = sum + row.getValueByPosition(i);
            }
            //System.out.println(i + " " + sum + " " + rows.size());
            centroids.set(i, sum / rows.size());
        }
        return centroids;

    }

    public void run(){

        Row row;
        init(dataSet.getRows(), distance, centroids, k);
        //reassigning to new clusters
        for (int i = 0; i < maxIters; i++) {

            for (int j = 0; j < k; j++) {
                ObservableList<Row> temp = FXCollections.observableArrayList();
                for (Row cls : clusters.keySet()) {
                    if (clusters.get(cls) == j) {
                        temp.add(cls);
                    }
                }
                row = centroidCalculator(temp);
                centroids.put(j, row);

            }
            clusters.clear();
            init(dataSet.getRows(), EUCLEDIAN, centroids, k);

        }

    }

    public DataSet[] getClusters(){

        DataSet[] temp = new DataSet[k];
        for(int i = 0; i < k; i++)
            temp[i] = new DataSet();

        for(Row row : clusters.keySet()){
            temp[clusters.get(row)].getRows().add(row);
        }

        return temp;
    }

    public ObservableList<Row> getCentroids(){

        ObservableList<Row> temp = FXCollections.observableArrayList();
        for(Integer val : centroids.keySet())
            temp.add(centroids.get(val));

        return temp;
    }

    public void display(){

        for (Row row : clusters.keySet()) {
            System.out.print(row.getValueByPosition(0) + "\t \t");
            System.out.print(clusters.get(row) + "\n");
        }

        double wcss=0;

        for(int i=0;i<k;i++){
            double sse=0;
            for (Row row : clusters.keySet()) {
                if (clusters.get(row)==i) {
                            //System.out.println(sse);
                    sse+=Math.pow(Distance.eucledianDistance(row, centroids.get(i), numFeatures),2);
                }
            }
            wcss+=sse;
        }
        String dis="";
        if(distance == EUCLEDIAN)
            dis="Euclidean";
        else
            dis="Manhattan";
        System.out.println("\n*********Programmed by Shephalika Shekhar************\n*********Results************\nDistance Metric: "+dis);
        System.out.println("Iterations: "+maxIters);
        System.out.println("Number of Clusters: "+k);
        System.out.println("WCSS: "+wcss);
    }

}
