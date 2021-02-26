package app;

import algos.CLARANS;
import algos.KMeans;
import algos.KMediods;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import inc.Common;
import inc.DataSet;
import inc.Init;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ClusterController implements Initializable,Init {

    @FXML
    Label kmeans,kmedoids,clarans,costLabel,fmeasureLabel,runtimeLabel,chosenLabel;

    @FXML
    VBox kmeansParamsVB,kmedoidsParamsVB,claransParamsVB,clustersVB;

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
    NumberFormat formatter = new DecimalFormat("#0.00");

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initParams();
        initStyle();

        kmeans.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "kmeans";
            chosenLabel.setText("KMeans");
        });

        kmedoids.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "kmedoids";
            chosenLabel.setText("KMedoids");
        });

        clarans.setOnMouseClicked(action -> {
            selectParams(action);
            updateStyle(action);
            selectedAlgo = "clarans";
            chosenLabel.setText("CLARANS");
        });

        runBtn.setOnAction(action -> {
            if(selectedAlgo.equals("kmeans")) {
                try {
                    runKMeans();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if(selectedAlgo.equals("kmedoids")) {
                try {
                    runKMedoids();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if(selectedAlgo.equals("clarans")) {
                try {
                    runCLARANS();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Common.controlDigitField(ncClarans);
        Common.controlDigitField(ncKmeans);
        Common.controlDigitField(ncKmedoids);
        Common.controlDigitField(maxItersClarans);
        Common.controlDigitField(maxItersKmeans);
        Common.controlDigitField(maxItersKmedoids);
        Common.controlDigitField(maxNeighbors);
    }

    private void initStyle() {

        String labelStyle = "    -fx-text-fill: #003f5c;\n" +
                "    -fx-background-color: #eeeeee;\n" +
                "    -fx-alignment: center;\n" +
                "    -fx-font-size: 16;";

        String selectedLabelStyle = "-fx-background-color: #ffa600";

        kmeans.setStyle(labelStyle + selectedLabelStyle);
        chosenLabel.setText("KMeans");
    }

    private void runCLARANS() throws IOException {

        // Time variables
        Instant start,end;
        long runtime;

        // Getting parameters
        int K = ncClarans.getText().trim().isEmpty()? 3 : Integer.parseInt(ncClarans.getText());
        int maxIters = maxItersClarans.getText().trim().isEmpty() ? 100 : Integer.parseInt(maxItersClarans.getText());
        int distance = distanceClarans.getValue().equals("EUCLIDEAN") ? EUCLEDIAN : MANHATTAN ;
        int maxN = maxNeighbors.getText().trim().isEmpty() ? 8 : Integer.parseInt(maxNeighbors.getText());

        CLARANS thyroidClarans = new CLARANS();
        thyroidClarans.maxIters = maxIters;
        thyroidClarans.K = K;
        thyroidClarans.distance = distance;
        thyroidClarans.maxNeighbors = maxN;
        start = Instant.now();
        thyroidClarans.run(dataSet);
        end = Instant.now();

        displayClusters(thyroidClarans.clusters, thyroidClarans.bestMedoids);

        runtime = Duration.between(start, end).toMillis();
        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(formatter.format(thyroidClarans.globalCost));
        fmeasureLabel.setText(formatter.format(getGlobaleFMeasure(dataSet.getRows(), thyroidClarans.clusters)));
    }

    private void displayClusters(DataSet[] output, ObservableList<Row> centroids) throws IOException {

        int clusterCpt = 0;
        clustersVB.getChildren().clear();
        for(DataSet ds : output) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClusterResult.fxml"));
            loader.load();
            Node node = loader.getRoot();
            ClusterResultController iController = loader.getController();
            iController.setClusterInformation(clusterCpt + 1, ds.size(), centroids.get(clusterCpt));
            clustersVB.getChildren().add(node);
            clustersVB.getChildren().get(clusterCpt);
            clusterCpt++;

        }
    }

    private void runKMedoids() throws IOException {

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
        DataSet[] output = thyroidKMediods.run(dataSet);
        end = Instant.now();
        runtime = Duration.between(start, end).toMillis();
        //thyroidKMeans.display();

        displayClusters(output, thyroidKMediods.medoids);
        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(formatter.format(thyroidKMediods.getCurrentCost()));
        fmeasureLabel.setText(formatter.format(getGlobaleFMeasure(dataSet.getRows(), output)));

    }

    private void runKMeans() throws IOException {

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

        displayClusters(thyroidKMeans.getClusters(), thyroidKMeans.getCentroids());

        runtimeLabel.setText(runtime + " ms");
        costLabel.setText(formatter.format(thyroidKMeans.getCost()));
        fmeasureLabel.setText(formatter.format(getGlobaleFMeasure(dataSet.getRows(), thyroidKMeans.getClusters())));

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

    private double getGlobaleFMeasure(ObservableList<Row> rows, DataSet[] outputs){

        HashMap<Double, ObservableList<Row>> classes = new HashMap<>();
        double sum = 0;

        for(Row row : rows){
            if(!classes.containsKey(row.values[0])){
                classes.put(row.values[0], FXCollections.observableArrayList());
            }
            else
                classes.get(row.values[0]).add(row);
        }

        for(double cls : classes.keySet()){
            sum += ((double)classes.get(cls).size() / rows.size()) * getClassMaxFMeasure(classes.get(cls), outputs);
        }

        return sum;

    }

    private double getClassMaxFMeasure(ObservableList<Row> classRows, DataSet[] outputs){

        double max = 0;
        double curr = 0;
        for(DataSet ds : outputs){
            curr = getFMeasure(classRows, ds);
            if(curr > max)
                max = curr;
        }

        return max;
    }

    private double getFMeasure(ObservableList<Row> classRows, DataSet output){

        double classNum = classRows.get(0).values[0];
        double precision = getPrecision(classNum, output);
        double recall = getRecall(output, classRows.size(), classNum);

        return (2 * precision * recall) / (precision + recall);

    }

    private double getPrecision(double classNum, DataSet output){

        double sum = 0;
        for(Row row : output.getRows()){
            if(row.values[0] == classNum)
                sum++;
        }

        return sum / output.size();
    }

    private double getRecall(DataSet output, int size, double classNum){

        double sum = 0;
        for(Row row : output.getRows()){
            if(row.values[0] == classNum)
                sum++;
        }

        return sum / size;
    }

}
