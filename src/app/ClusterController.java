package app;

import algos.CLARANS;
import algos.KMeans;
import algos.KMediods;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import inc.DataSet;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;

public class ClusterController implements Initializable, Init {

    @FXML
    Label kmeans,kmedoids,clarans,costLabel,fmeasureLabel,runtimeLabel;

    @FXML
    VBox kmeansParamsVB,kmedoidsParamsVB,claransParamsVB;

    @FXML
    JFXTextField ncKmeans,ncKmedoids,ncClarans,maxItersKmeans,maxItersKmedoids,maxItersClarans,maxNeighbors;

    @FXML
    JFXToggleButton outliers;

    @FXML
    ChoiceBox<String> distanceKmeans,distanceKmedoids,distanceClarans;

    @FXML
    JFXButton runBtn;
    
    String selectedAlgo = "kmeans";
    DataSet dataSet;
    ObservableList<Row> originalRows = FXCollections.observableArrayList();

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initParams();

        kmeans.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "kmeans";
        });

        kmedoids.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "kmedoids";
        });

        clarans.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "clarans";
        });

        runBtn.setOnAction(action -> {
            if(selectedAlgo.equals("kmeans"))
                runKMeans();
            if(selectedAlgo.equals("kmedoids"))
                runKMedoids();
            if(selectedAlgo.equals("clarans"))
                runCLARANS();
        });
    }

    private void runCLARANS() {

        // Time variables
        Instant start,end;
        long runtime;

        // Getting parameters
        int K = ncClarans.getText().trim().isEmpty()? 3 : Integer.parseInt(ncClarans.getText());
        int maxIters = maxItersClarans.getText().trim().isEmpty() ? 100 : Integer.parseInt(maxItersClarans.getText());
        int distance = distanceClarans.getValue().equals("EUCLIDEAN") ? EUCLEDIAN : MANHATTAN ;
        int maxN = maxNeighbors.getText().trim().isEmpty() ? 8 : Integer.parseInt(maxNeighbors.getText());

        CLARANS thyroidClarans = new CLARANS();
        thyroidClarans.setMaxIters(maxIters);
        thyroidClarans.setK(K);
        thyroidClarans.setDistance(distance);
        thyroidClarans.setMaxNeighbors(maxN);
        start = Instant.now();
        thyroidClarans.run(dataSet);
        end = Instant.now();

        runtime = Duration.between(start, end).toMillis();
        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(String.valueOf(thyroidClarans.getMinCost()));
    }

    private void runKMedoids() {

        // Time variables
        Instant start,end;
        long runtime;

        // Getting parameters
        int K = ncKmedoids.getText().trim().isEmpty()? 3 : Integer.parseInt(ncKmedoids.getText());
        int maxIters = maxItersKmedoids.getText().trim().isEmpty() ? 100 : Integer.parseInt(maxItersKmedoids.getText());
        int distance = distanceKmedoids.getValue().equals("EUCLIDEAN") ? EUCLEDIAN : MANHATTAN ;

        KMediods thyroidKMediods = new KMediods();
        thyroidKMediods.setMaxIter(maxIters);
        thyroidKMediods.setK(K);
        thyroidKMediods.setDistance(distance);

        start = Instant.now();
        thyroidKMediods.run(dataSet);
        end = Instant.now();
        runtime = Duration.between(start, end).toMillis();
        //thyroidKMeans.display();

        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(String.valueOf(thyroidKMediods.getCurrentCost()));

    }

    private void runKMeans() {

        // Time variables
        Instant start,end;
        long runtime;

        // Getting parameters
        int K = ncKmeans.getText().trim().isEmpty()? 3 : Integer.parseInt(ncKmeans.getText());
        int maxIters = maxItersKmeans.getText().trim().isEmpty() ? 100 : Integer.parseInt(maxItersKmeans.getText());
        int distance = distanceKmeans.getValue().equals("EUCLIDEAN") ? EUCLEDIAN : MANHATTAN ;

        if(outliers.isSelected())
            dataSet.setRows(dataSet.IQR());
        else
            dataSet.setRows(originalRows);

        System.out.println(dataSet.size());

        // Running KMeans
        KMeans thyroidKMeans = new KMeans();
        thyroidKMeans.setDataSet(dataSet);
        thyroidKMeans.setNumFeatures(6);
        thyroidKMeans.setMaxIters(maxIters);
        thyroidKMeans.setK(K);
        thyroidKMeans.setDistance(distance);
        thyroidKMeans.initiateCentroids();
        start = Instant.now();
        thyroidKMeans.run();
        end = Instant.now();
        runtime = Duration.between(start, end).toMillis();
        //thyroidKMeans.display();

        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(String.valueOf(thyroidKMeans.getCost()));

    }

    private void initParams() {

        ncKmeans.setText("3");
        ncKmedoids.setText("3");
        ncClarans.setText("3");
        maxItersKmeans.setText("100");
        maxItersClarans.setText("100");
        maxItersKmedoids.setText("100");
        maxNeighbors.setText("8");

        ObservableList<String> distances = FXCollections.observableArrayList();
        distances.add("EUCLIDEAN");
        distances.add("MANHATTAN");

        distanceClarans.setItems(distances);
        distanceKmeans.setItems(distances);
        distanceKmedoids.setItems(distances);

        distanceClarans.setValue("EUCLIDEAN");
        distanceKmeans.setValue("EUCLIDEAN");
        distanceKmedoids.setValue("EUCLIDEAN");

        distanceKmeans.getSelectionModel().select(0);
        distanceKmedoids.getSelectionModel().select(0);
        distanceClarans.getSelectionModel().select(0);
    }

    private void updateStyle(MouseEvent action) {

        String labelStyle = "    -fx-text-fill: #003f5c;\n" +
                "    -fx-background-color: #eeeeee;\n" +
                "    -fx-alignment: center;\n" +
                "    -fx-font-size: 16;";

        String selectedLabelStyle = "-fx-background-color: #ffa600";

        if(action.getSource() == kmeans) {
            kmeans.setStyle(labelStyle + selectedLabelStyle);
            kmedoids.setStyle(labelStyle);
            clarans.setStyle(labelStyle);
        }
        if(action.getSource() == kmedoids) {
            kmedoids.setStyle(labelStyle + selectedLabelStyle);
            kmeans.setStyle(labelStyle);
            clarans.setStyle(labelStyle);
        }
        if(action.getSource() == clarans) {
            clarans.setStyle(labelStyle + selectedLabelStyle);
            kmedoids.setStyle(labelStyle);
            kmeans.setStyle(labelStyle);
        }
    }

    private void selectParams(MouseEvent action) {

        if(action.getSource() == kmeans) {
            kmeansParamsVB.setVisible(true);
            kmedoidsParamsVB.setVisible(false);
            claransParamsVB.setVisible(false);
        }
        if(action.getSource() == kmedoids) {
            kmeansParamsVB.setVisible(false);
            kmedoidsParamsVB.setVisible(true);
            claransParamsVB.setVisible(false);
        }
        if(action.getSource() == clarans) {
            kmeansParamsVB.setVisible(false);
            kmedoidsParamsVB.setVisible(false);
            claransParamsVB.setVisible(true);
        }
    }
}
