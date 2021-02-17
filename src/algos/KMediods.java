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

    public DataSet[] run(DataSet dataSet){

        ObservableList<Row> medoids = FXCollections.observableArrayList();
        DataSet[] output = new DataSet[K];

        for (int i = 0; i < K; i++) {
            int random = rand.nextInt(dataSet.getRows().size());
            System.out.println(random);
            medoids.add(dataSet.getRows().get(random));
        }


        boolean changed = true;
        int count = 0;
        while (changed && count < maxIter) {
            changed = false;
            count++;
            int[] assignment = assign(medoids, dataSet);
            changed = recalculateMedoids(assignment, medoids, output, dataSet);

            System.out.println("Iteration = " + count);
            for(Row row : medoids)
                System.out.println(row);

        }

        System.out.println(count);

        return output;
    }

    private boolean recalculateMedoids(int[] assignment, ObservableList<Row> medoids, DataSet[] output, DataSet dataSet) {

        boolean changed = false;
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
                changed = true;
            } else {
                Row centroid = output[i].average();
                Row oldMedoid = medoids.get(i);
                medoids.set(i, dataSet.kNearest(1, centroid, distance).iterator().next());
                if (!medoids.get(i).equals(oldMedoid)) {
                    changed = true;
                    //System.out.println("Mediod " + i + " changed");
                }
            }
        }
        return changed;
    }

    private int[] assign(ObservableList<Row> medoids, DataSet dataSet) {

        int[] out = new int[dataSet.getRows().size()];
        for (int i = 0; i < dataSet.getRows().size(); i++) {
            //double bestDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);
            double bestDistance = medoids.get(0).getDissimilarity(dataSet.getRows().get(i));
            int bestIndex = 0;
            //System.out.println("Distance from 0 = " + bestDistance);
            for (int j = 1; j < medoids.size(); j++) {

                double tmpDistance = medoids.get(j).getDissimilarity(dataSet.getRows().get(i));
                //System.out.println("Distance from " + j + " = " + tmpDistance);
                if (tmpDistance < bestDistance) {
                    bestDistance = tmpDistance;
                    bestIndex = j;

                }
            }
            out[i] = bestIndex;

        }
        return out;
    }
}
