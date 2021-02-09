package algos;

import inc.DataSet;
import inc.Distance;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class KMeans implements Init {

    int k;
    int maxIters;
    DataSet dataSet;
    HashMap<Integer, Row> centroids = new HashMap<>();
    HashMap<Row, Integer> clusters = new HashMap<>();

    public void calculateNumClusters(){
    };

    public void initiateCentroids(){

        for(int i = 0; i < k; i++){
            centroids.put(i, dataSet.getRows().get(i));
        }
    }

    public void init(ObservableList<Row> rows, int distance, HashMap<Integer, Row> centroids, int k) {

        int k1 = 0;
        double dist = 0;
        for(Row row : rows) {
            double minimum = 999999.0;
            for (int j = 0; j < k; j++) {
                if(distance == EUCLEDIAN){
                    dist = Distance.eucledianDistance(centroids.get(j), row);
                }
                else if(distance == MANHATTAN){
                    dist = Distance.manhattanDistance(centroids.get(j), row);
                }
                if (dist < minimum) {
                    minimum = dist;
                    k1 = j;
                }

            }
            clusters.put(row, k1);
        }

    }

    public Row centroidCalculator(ObservableList<Row> rows) {

        int count = 0;
        double sum = 0.0;

        Row centroids = new Row();
        for (int i = 0; i < 6; i++) {
            for(Row row : rows){
                count++;
                sum = sum + row.getValueByPosition(i);
            }
            centroids.set(i, sum / count);
        }
        return centroids;

    }

    public void run(){

        Row row = new Row();
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
            init(dataSet.getRows(),EUCLEDIAN, centroids, k);

        }

    }
}
