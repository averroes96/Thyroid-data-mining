package app;

import inc.Init;
import inc.Row;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClusterResultController implements Initializable, Init {

    @FXML
    Label clusterNumber,clusterSize,clusterCentroid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClusterInformation(int number, int size, Row centroid){

        clusterNumber.setText("Cluster = " + number);
        clusterSize.setText(" Size = " + size);
        clusterCentroid.setText("Centroid = " + centroid.toString());

    }
}
