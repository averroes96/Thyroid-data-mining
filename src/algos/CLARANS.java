package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public class CLARANS implements Init {

    public int K; // number of clusters
    public Random rand;
    public int maxIters; // max number of iterations
    public int maxNeighbors; // max number of neighbors
    public double globalCost; // best found cost
    public int distance; // distance measure
    public double localCost;

    public ObservableList<Row> medoids = FXCollections.observableArrayList(); // list of medoids
    public ObservableList<Row> bestMedoids = FXCollections.observableArrayList(); // list of best medoids
    public DataSet[] clusters;

    public CLARANS() {
        this(3, 100);
    }

    public CLARANS(int k, int maxIters) {
        K = k;
        this.maxIters = maxIters;
        rand = new Random(System.currentTimeMillis());
        distance = EUCLEDIAN;
        clusters = new DataSet[k];
        localCost = 0;
        maxNeighbors = 0;
    }

    public void run(DataSet ds){

        int i = 0;
        globalCost = 999999999;
        bestMedoids = FXCollections.observableArrayList();
        clusters = new DataSet[K];
        if(maxNeighbors == 0)
            maxNeighbors = (int) Math.round(0.0125 * K * (ds.size() - K));

        for(int c = 0; c < K; c++) // initialise the clusters
            clusters[c] = new DataSet();

        while(i < maxIters){

            selectMedoids(ds);
            localCost = assignToClusters(ds, medoids);

            int j = 0;

            while(j < maxNeighbors){ // enter the neighbors loop

                int pos = rand.nextInt(medoids.size());
                Row medoid = medoids.get(pos);
                Row newMedoid;
                if(clusters[pos].size() != 0) // if cluster is empty try the nearest instant
                    newMedoid = clusters[pos].getRows().get(rand.nextInt(clusters[pos].size()));
                else
                    newMedoid = ds.kNearest(1, medoid, distance).iterator().next();

                medoids.set(pos, newMedoid);

                double newCost = assignToClusters(ds, medoids);
                //System.out.println("New cost = " + newCost);

                if(newCost < localCost)
                    localCost = newCost;
                else {
                    j++;
                    medoids.set(pos, medoid);
                    assignToClusters(ds, medoids);
                }

            }

            if(localCost < globalCost){
                bestMedoids = medoids;
                globalCost = localCost;
                //System.out.println("Min Cost = " + globalCost);
            }

            i++;

        }

        assignToClusters(ds, bestMedoids);

    }

    private double assignToClusters(DataSet dataSet, ObservableList<Row> medoids) {

        double cost = 0;

        for(int c = 0; c < K; c++)
            clusters[c] = new DataSet();

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
            clusters[bestIndex].getRows().add(dataSet.getRows().get(i));

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
