package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public class CLARANS implements Init {

    private int K;
    private Random rand;
    private int maxIters;
    private int maxNeighbors;
    private double minCost;
    private int distance;
    private double totalCost;

    public ObservableList<Row> medoids = FXCollections.observableArrayList();
    public ObservableList<Row> bestNode = FXCollections.observableArrayList();
    public DataSet[] output;

    public CLARANS() {
        this(3, 100);
    }

    public CLARANS(int k, int maxIters) {
        K = k;
        this.maxIters = maxIters;
        rand = new Random(System.currentTimeMillis());
        this.distance = EUCLEDIAN;
        output = new DataSet[k];
        totalCost = 0;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public int getMaxIters() {
        return maxIters;
    }

    public void setMaxIters(int maxIters) {
        this.maxIters = maxIters;
    }

    public int getMaxNeighbors() {
        return maxNeighbors;
    }

    public void setMaxNeighbors(int maxNeighbors) {
        this.maxNeighbors = maxNeighbors;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getMinCost() {
        return minCost;
    }

    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

    public void run(DataSet ds){

        int i = 0;
        minCost = 9999;
        bestNode = FXCollections.observableArrayList();
        this.maxNeighbors = (int) Math.round(0.0125 * K * (ds.size() - K));
        this.output = new DataSet[K];

        for(int c = 0; c < K; c++)
            output[c] = new DataSet();

        while(i < maxIters){

            //System.out.println("Iteration = " + i + " =======================================================");
            selectMedoids(ds);
            totalCost = assignToClusters(ds, medoids);

            //System.out.println("Total cost = " + totalCost);

            int j = 0;

            while(j < maxNeighbors){

                int pos = rand.nextInt(medoids.size());
                Row medoid = medoids.get(pos);
                Row newMedoid;
                if(output[pos].size() != 0)
                    newMedoid = output[pos].getRows().get(rand.nextInt(output[pos].size()));
                else
                    newMedoid = ds.kNearest(1, medoid, distance).iterator().next();

                medoids.set(pos, newMedoid);

                double newCost = assignToClusters(ds, medoids);
                //System.out.println("New cost = " + newCost);

                if(newCost < totalCost)
                    totalCost = newCost;
                else {
                    j++;
                    medoids.set(pos, medoid);
                    assignToClusters(ds, medoids);
                }

            }

            if(totalCost < minCost){
                bestNode = medoids;
                minCost = totalCost;
                //System.out.println("Min Cost = " + minCost);
            }

            i++;

            //System.out.println("===================================================================================");
        }

        assignToClusters(ds, bestNode);

        //System.out.println("Best medoids==============================================");
        /*for(Row row : bestNode){
            System.out.println(row);
        }*/
        //System.out.println("===========================================================");

        //System.out.println("Max Neighbors = " + maxNeighbors);
        //System.out.println("Min cost = " + minCost);

    }

    private double assignToClusters(DataSet dataSet, ObservableList<Row> medoids) {

        double cost = 0;

        for(int c = 0; c < K; c++)
            output[c] = new DataSet();

        for (int i = 0; i < dataSet.getRows().size(); i++) {

            int bestIndex = 0;
            double bestDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);

            for (int j = 1; j < medoids.size(); j++) {
                double tmpDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(j), 6);
                if (tmpDistance < bestDistance) {
                    bestDistance = tmpDistance;
                    bestIndex = j;
                }
            }

            cost += bestDistance;
            output[bestIndex].getRows().add(dataSet.getRows().get(i));

        }

        return cost;
    }

    private void selectMedoids(DataSet ds) {

        //System.out.println("Selected medoid ====================================");
        medoids.clear();
        for (int i = 0; i < K; i++) {
            int random = rand.nextInt(ds.getRows().size());
            medoids.add(ds.getRows().get(random));
            //System.out.println(ds.getRows().get(random));
        }
        //System.out.println("===========================================================");
    }

}
