package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Random;

public class KMediods implements Init {

    private int K;
    private int distance;
    private Random rand;
    private int maxIter;

    public KMediods() {
        this(4, 100, EUCLEDIAN);
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

    public void run(DataSet dataSet){

        ObservableList<Row> medoids = FXCollections.observableArrayList();
        ObservableList<DataSet> output = FXCollections.observableArrayList();

        for (int i = 0; i < K; i++) {
            int random = rand.nextInt(dataSet.getRows().size());
            medoids.add(dataSet.getRows().get(random));
        }

        boolean changed = true;
        int count = 0;
        while (changed && count < maxIter) {
            changed = false;
            count++;
            int[] assignment = assign(medoids, dataSet);
            changed = recalculateMedoids(assignment, medoids, output, dataSet);

        }
    }

    private boolean recalculateMedoids(int[] assignment, ObservableList<Row> medoids, ObservableList<DataSet> output, DataSet dataSet) {

        boolean changed = false;
        for (int i = 0; i < K; i++) {

            for (int j = 0; j < assignment.length; j++) {
                if (assignment[j] == i) {
                    output.get(i).getRows().add(dataSet.getRows().get(j));
                }
            }
            if (output.get(i).size() == 0) { // new random, empty medoid
                medoids.set(i, dataSet.getRows().get(rand.nextInt(dataSet.size())));
                changed = true;
            } else {
                //Instance centroid = DatasetTools.average(output[i]);
                //Instance oldMedoid = medoids[i];
                //medoids[i] = data.kNearest(1, centroid, dm).iterator().next();
                //if (!medoids[i].equals(oldMedoid))
                //    changed = true;
            }
        }
        return changed;
    }

    private int[] assign(ObservableList<Row> medoids, DataSet dataSet) {

        int[] out = new int[dataSet.getRows().size()];
        for (int i = 0; i < dataSet.getRows().size(); i++) {
            double bestDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);
            int bestIndex = 0;
            for (int j = 1; j < medoids.size(); j++) {
                double tmpDistance = Distance.calculateDistance(distance, dataSet.getRows().get(i), medoids.get(0), 6);
                if (tmpDistance > bestDistance) {
                    bestDistance = tmpDistance;
                    bestIndex = j;
                }
            }
            out[i] = bestIndex;

        }
        return out;
    }
}
