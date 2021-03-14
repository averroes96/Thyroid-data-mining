package app;

import algos.CLARANS;
import algos.KMeans;
import algos.KMediods;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.*;
import inc.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable,Init {

    @FXML
    ImageView boxPlotIV;

    @FXML
    JFXButton displayBtn,clusteringBtn,aprioriBtn,uploadBtn,overviewBtn,infoBtn,runBtn;

    @FXML
    AnchorPane valuesAP,histoAP,scatterAP,boxPlotAP,overviewAP,clusterAP,aprioriAP;

    @FXML
    LineChart<String,Integer> valuesLC;

    @FXML
    BarChart valuesBC;

    @FXML
    ScatterChart valuesSC;

    @FXML
    javafx.scene.chart.NumberAxis yAxis,xAxis;

    @FXML
    JFXDialog dialog;

    @FXML
    StackPane stackPane;

    @FXML
    JFXScrollPane scrollPane;

    @FXML
    JFXTextField ncKmeans,maxItersKmeans,ncKmedoids,maxItersKmedoids,ncClarans,maxItersClarans,maxNeighbors;

    @FXML
    Label meanLabel,medianLabel,modeLabel,histogramTAB,valuesTAB,scatterTAB,boxPlotTAB,kmeans,kmedoids,clarans,costLabel,fmeasureLabel,runtimeLabel;

    @FXML
    ChoiceBox<String> attributeCB,yAttrCB,distanceKmeans,distanceKmedoids,distanceClarans;

    @FXML
    VBox kmeansParamsVB,kmedoidsParamsVB,claransParamsVB,clustersVB;

    @FXML
    JFXToggleButton outliers;

    DataSet dataSet;
    DataSet discretData;
    ObservableList<String> attributesList = FXCollections.observableArrayList();
    File selectedFile = null;
    String selectedAlgo = "kmeans";
    ObservableList<Row> originalRows = FXCollections.observableArrayList();
    NumberFormat formatter = new DecimalFormat("#0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataSet = getDataSet("/ds/Thyroid_Dataset.txt", true);
        System.out.println(dataSet.size());
        discretData = getDataSet("/ds/Thyroid_Dataset.txt", true);

        valuesTAB.setId("selected-label");
        valuesTAB.setGraphic(getIcon("dist_primary.png"));

        initAttrList(attributeCB, 0);
        initAttrList(yAttrCB, 5);

        initFields();
        getResults();

        attributeCB.setOnAction(action -> {
            initFields();
            getResults();
        });

        yAttrCB.setOnAction(action -> {
            initScatterChart();
        });

        valuesTAB.setOnMouseClicked(this::selectTab);

        histogramTAB.setOnMouseClicked(this::selectTab);

        scatterTAB.setOnMouseClicked(this::selectTab);

        boxPlotTAB.setOnMouseClicked(this::selectTab);

        uploadBtn.setOnAction(action -> {
            uploadDataSet();

            /*initAttrList(attributeCB, 0);
            initAttrList(yAttrCB, 5);
            initFields();
            getResults();*/
        });

        infoBtn.setOnAction(action -> {
            InputStream in = getClass().getResourceAsStream("/ds/Thyroid_Dataset_Information.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder text = new StringBuilder();
            String line = "";
            while (true) {
                try {
                    if ((line = reader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text.append(line).append("\n");
            }

            Common.customDialog(dialog, stackPane, scrollPane, "", text.toString());

            scrollPane.setVisible(true);
            scrollPane.setContent(stackPane);
        });

        displayBtn.setOnAction(action -> {
            String text = null;
            String title = "Total number of rows:\t" + dataSet.getRows().size();
            try {
                text = getDisplayedDataSet();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Common.customDialog(dialog, stackPane, scrollPane, title, text);
            scrollPane.setVisible(true);
            scrollPane.setContent(stackPane);
        });

        overviewBtn.setOnAction(this::selectMenu);
        overviewBtn.setId("menu-btn-selected");

        aprioriBtn.setOnAction(this::selectMenu);

        clusteringBtn.setOnAction(this::selectMenu);

        //================================== Clustering tab ======================================================

        initParams();
        initStyle();

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

    private void uploadDataSet() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(".TXT files", "*.txt")
        );

        selectedFile = fileChooser.showOpenDialog(uploadBtn.getScene().getWindow());

        if (selectedFile != null) {
            dataSet = getDataSet(selectedFile.getAbsolutePath(), false);
        }

    }

    private String getDisplayedDataSet() throws IOException {

        Parser parser = new Parser();
        DataSet temp = new DataSet(parser.getRows("/ds/Thyroid_Dataset.txt", true));

        StringBuilder stringBuilder = new StringBuilder();
        for(Row row : temp.getRows()){
            stringBuilder.append(row).append("\n");
        }

        return stringBuilder.toString();
    }

    private void initScatterChart() {

        int index = attributeCB.getSelectionModel().getSelectedIndex();

        valuesSC.getData().clear();
        double xAxisMin = dataSet.getArrtibuteMin(yAttrCB.getSelectionModel().getSelectedIndex());
        double xAxisMax = dataSet.getArrtibuteMax(yAttrCB.getSelectionModel().getSelectedIndex());
        double xRange = (xAxisMax - xAxisMin)/10;
        xAxis.setLowerBound(xAxisMin);
        xAxis.setUpperBound(xAxisMax);
        xAxis.setTickUnit(xRange);
        xAxis.setLabel(attributeCB.getValue());
        double yAxisMin = dataSet.getArrtibuteMin(index);
        double yAxisMax = dataSet.getArrtibuteMax(index);
        double yRange = (yAxisMax - yAxisMin)/10;
        yAxis.setLowerBound(yAxisMin);
        yAxis.setUpperBound(yAxisMax);
        yAxis.setTickUnit(yRange);
        xAxis.setLabel(yAttrCB.getValue());
        XYChart.Series<Number,Number> scatterSeries = dataSet.getScatterData(index, yAttrCB.getSelectionModel().getSelectedIndex());
        valuesSC.getData().add(scatterSeries);


    }

    private void selectMenu(ActionEvent event){

        if(event.getSource() == overviewBtn){
            overviewBtn.setId("menu-btn-selected");
            aprioriBtn.setId("menu-btn");
            clusteringBtn.setId("menu-btn");

            overviewAP.setVisible(true);
            aprioriAP.setVisible(false);
            clusterAP.setVisible(false);
        }

        if(event.getSource() == aprioriBtn){
            overviewBtn.setId("menu-btn");
            aprioriBtn.setId("menu-btn-selected");
            clusteringBtn.setId("menu-btn");

            overviewAP.setVisible(false);
            aprioriAP.setVisible(true);
            clusterAP.setVisible(false);
        }

        if(event.getSource() == clusteringBtn){
            overviewBtn.setId("menu-btn");
            aprioriBtn.setId("menu-btn");
            clusteringBtn.setId("menu-btn-selected");

            overviewAP.setVisible(false);
            aprioriAP.setVisible(false);
            clusterAP.setVisible(true);
        }
    }

    private void selectTab(MouseEvent action) {

        if(action.getSource() == valuesTAB){

            valuesTAB.setId("selected-label");
            histogramTAB.setId("menu-label");
            scatterTAB.setId("menu-label");
            boxPlotTAB.setId("menu-label");

            valuesTAB.setGraphic(getIcon("dist_primary.png"));
            histogramTAB.setGraphic(getIcon("histo_white.png"));
            scatterTAB.setGraphic(getIcon("scatter_white.png"));
            boxPlotTAB.setGraphic(getIcon("box_white.png"));

            valuesAP.setVisible(true);
            histoAP.setVisible(false);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == histogramTAB){

            valuesTAB.setId("menu-label");
            histogramTAB.setId("selected-label");
            scatterTAB.setId("menu-label");
            boxPlotTAB.setId("menu-label");

            valuesTAB.setGraphic(getIcon("dist_white.png"));
            histogramTAB.setGraphic(getIcon("histo_primary.png"));
            scatterTAB.setGraphic(getIcon("scatter_white.png"));
            boxPlotTAB.setGraphic(getIcon("box_white.png"));

            valuesAP.setVisible(false);
            histoAP.setVisible(true);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == scatterTAB){

            valuesTAB.setId("menu-label");
            histogramTAB.setId("menu-label");
            scatterTAB.setId("selected-label");
            boxPlotTAB.setId("menu-label");

            valuesTAB.setGraphic(getIcon("dist_white.png"));
            histogramTAB.setGraphic(getIcon("histo_white.png"));
            scatterTAB.setGraphic(getIcon("scatter_primary.png"));
            boxPlotTAB.setGraphic(getIcon("box_white.png"));

            valuesAP.setVisible(false);
            histoAP.setVisible(false);
            scatterAP.setVisible(true);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == boxPlotTAB){

            valuesTAB.setId("menu-label");
            histogramTAB.setId("menu-label");
            scatterTAB.setId("menu-label");
            boxPlotTAB.setId("selected-label");

            valuesTAB.setGraphic(getIcon("dist_white.png"));
            histogramTAB.setGraphic(getIcon("histo_white.png"));
            scatterTAB.setGraphic(getIcon("scatter_white.png"));
            boxPlotTAB.setGraphic(getIcon("box_primary.png"));

            valuesAP.setVisible(false);
            histoAP.setVisible(false);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(true);
        }
    }

    private ImageView getIcon(String s) {

        return new ImageView(new Image(
                ClassLoader.class.getResourceAsStream(ICONS_PATH + s),
                32, 32, false, false));
    }

    private void initFields() {

        int index = attributeCB.getSelectionModel().getSelectedIndex();

        meanLabel.setText(String.valueOf(dataSet.calculateMean(index)));
        modeLabel.setText(String.valueOf(dataSet.calculateMode(index)));
        dataSet.setRows(Common.heapSort(dataSet.getRows(), index));
        medianLabel.setText(String.valueOf(dataSet.calculateMedian(index)));

    }

    private void getResults() {

        int index = attributeCB.getSelectionModel().getSelectedIndex();
        dataSet.setRows(Common.heapSort(dataSet.getRows(), index));
        valuesLC.getData().clear();
        valuesBC.getData().clear();
        valuesSC.getData().clear();
        XYChart.Series<String,Integer> lineSeries = dataSet.getOccurenceCount(index);
        XYChart.Series<String,Integer> barChartSeries = dataSet.getHistogramCount(index);
        valuesBC.getData().add(barChartSeries);
        valuesLC.getData().add(lineSeries);
        initScatterChart();
        initBoxPlot(index);

    }

    private void initBoxPlot(int currPostion) {
        String name = "/ds/boxplots/" + currPostion + ".png";
        new ZoomIn(boxPlotIV).play();
        boxPlotIV.setImage(new Image(ClassLoader.class.getResourceAsStream(name),
                boxPlotIV.getFitWidth(), boxPlotIV.getFitHeight(), false, false));
    }

    private DataSet getDataSet(String filePath, boolean type) {

        Parser parser = new Parser();
        DataSet ds = new DataSet();
        try {
            ObservableList<Row> temp = parser.getRows(filePath, type);
            ds = new DataSet(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<String> temp = FXCollections.observableArrayList();

        for(int i = 1; i <= ds.getNumFeatures(); i++){
            temp.add("Attribute " + i);
        }

        attributesList = temp;

        return ds;

    }

    private void initAttrList(ChoiceBox choiceBox, int pos){

        choiceBox.setItems(attributesList);
        choiceBox.getSelectionModel().select(pos);

    }

}
