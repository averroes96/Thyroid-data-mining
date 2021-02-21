package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KMediods implements Init {

    private int K;
    private int distance;
    private Random rand;
    private int maxIter;
    private double currentCost = 0;
    private double newCost = 0;

    public ObservableList<Row> medoids = FXCollections.observableArrayList();


    public KMediods() {
        this(3, 100, EUCLEDIAN);
    }

    public KMediods(int K, int maxIter, int distance) {
        this.K = K;
        this.maxIter = maxIter;
        this.distance = distance;
        rand = new Random(System.currentTimeMillis());
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public DataSet[] run(DataSet dataSet){

        DataSet[] output = new DataSet[K];

        for (int i = 0; i < K; i++) {
            int random = rand.nextInt(dataSet.getRows().size());
            medoids.add(dataSet.getRows().get(random));
        }

        //boolean changed = true;
        int count = 0;
        while (count < maxIter) {
            //changed = false;
            count++;
            int[] assignment = assign(medoids, dataSet);
            recalculateMedoids(assignment, medoids, output, dataSet);

        }
        
        return output;
    }

    private void recalculateMedoids(int[] assignment, ObservableList<Row> medoids, DataSet[] output, DataSet dataSet) {

        //boolean changed = false;
        for (int i = 0; i < K; i++) {
            output[i] = new DataSet();
            for (int j = 0; j < assignment.length; j++) {
                if (assignment[j] == i) {
                    output[i].getRows().add(dataSet.getRows().get(j));
                }
            }
            if (output[i].size() == 0) { // new random, empty medoid
                //System.out.println("Cluster " + i + " is Empty!");
                medoids.set(i, dataSet.getRows().get(rand.nextInt(dataSet.size())));
                //System.out.println("Mediod " + i + " changed");
                //changed = true;
            } else {
                Row oldMedoid = medoids.get(i);
                Row centroid =  output[i].getRows().get(rand.nextInt(output[i].size()));//dataSet.kNearest(1, oldMedoid, distance).iterator().next();

                medoids.set(i, centroid);
                calculaNewCost(dataSet, medoids);

                if(currentCost <= newCost)
                    medoids.set(i, oldMedoid);
                //else
                    //changed = true;
            }
        }
        //return changed;
    }

    private void calculaNewCost(DataSet dataSet, ObservableList<Row> medoids) {

        newCost = 0;

        for (int i = 0; i < dataSet.getRows().size(); i++) {
            double bestDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);
            //double bestDistance = medoids.get(0).getDissimilarity(dataSet.getRows().get(i));
            //System.out.println("Distance from 0 = " + bestDistance);
            for (int j = 1; j < medoids.size(); j++) {

                double tmpDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(j), 6); //medoids.get(j).getDissimilarity(dataSet.getRows().get(i));
                //System.out.println("Distance from " + j + " = " + tmpDistance);
                if (tmpDistance < bestDistance) {
                    bestDistance = tmpDistance;
                }
            }
            newCost += bestDistance;

        }
    }


    private int[] assign(ObservableList<Row> medoids, DataSet dataSet) {

        currentCost = 0;
        int[] out = new int[dataSet.getRows().size()];

        for (int i = 0; i < dataSet.getRows().size(); i++) {
            double bestDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);
            //double bestDistance = medoids.get(0).getDissimilarity(dataSet.getRows().get(i));
            int bestIndex = 0;
            //System.out.println("Distance from 0 = " + bestDistance);
            for (int j = 1; j < medoids.size(); j++) {

                double tmpDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(j), 6); //medoids.get(j).getDissimilarity(dataSet.getRows().get(i));
                //System.out.println("Distance from " + j + " = " + tmpDistance);
                if (tmpDistance < bestDistance) {
                    bestDistance = tmpDistance;
                    bestIndex = j;

                }
            }
            currentCost += bestDistance;
            out[i] = bestIndex;

        }
        return out;
    }
}
