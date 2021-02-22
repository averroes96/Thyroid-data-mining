package app;

import algos.*;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXScrollPane;
import inc.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable,Init {

    @FXML
    private ImageView informationIV,boxPlotIV;

    @FXML
    private JFXButton displayBtn,clusteringBtn,aprioriBtn;

    @FXML
    private AnchorPane valuesAP,histoAP,scatterAP,boxPlotAP;

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
    private Label meanLabel,medianLabel,modeLabel,histogramTAB,valuesTAB,scatterTAB,boxPlotTAB;

    @FXML
    private ChoiceBox<String> attributeCB,yAttrCB;

    private DataSet dataSet = new DataSet();
    private DataSet discretData = new DataSet();
    private ObservableList<String> attributesList = FXCollections.observableArrayList();
    private int currPostion = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getDataSet(dataSet);
        getDataSet(discretData);

        attributesList.addAll("Class attribute",
                "T3-resin uptake test",
                "Total Serum thyroxin",
                "Total serum triiodothyronine",
                "basal thyroid-stimulating hormone",
                "Maximal absolute difference of TSH value");

        initAttrList(attributeCB, 0);
        initAttrList(yAttrCB, 5);

        initFields();
        getResults();

        attributeCB.setOnAction(action -> {
            getSelectedPosition(attributeCB.getSelectionModel().getSelectedItem());
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

        informationIV.setOnMouseClicked(action -> {
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

        aprioriBtn.setOnAction(action -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "Apriori.fxml"));
            startStage(loader);
        });

        clusteringBtn.setOnAction(action -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "Cluster.fxml"));
            startStage(loader);
        });

        /*
        dataSet.discretize(0, 4);
        dataSet.discretize(1, 4);
        dataSet.discretize(2, 4);
        dataSet.discretize(3, 4);
        dataSet.discretize(4, 4);
        dataSet.discretize(5, 4);*/

        //dataSet.IQR();

        /*KMediods thyroidKMediods = new KMediods();
        DataSet[] clusters = thyroidKMediods.run(dataSet);
        int k = 1;
        for(DataSet ds : clusters){
            System.out.println("Cluster = " + k + " Size = " + ds.size());
            for(Row row : ds.getRows())
                System.out.println(row);
            k++;
        }*/

        /*int k = 1;
        CLARANS thyroidClarans = new CLARANS();
        thyroidClarans.setMaxIters(1000);
        thyroidClarans.run(dataSet);

        DataSet[] result = thyroidClarans.output;

        for(DataSet ds : result){
            System.out.println("Cluster " + k + " = " + ds.size());
            for(Row row : ds.getRows()){
                System.out.println(row);
            }
            k++;
        }*/

        /*

        discretData.discretize(1, 4);


        Apriori apriori = new Apriori();
        apriori.setDataSet(discretData);
        apriori.setMinSup(4);
        HashMap<String, Integer> candidates = new HashMap<>();
        for (Row row : discretData.getRows()){

            for(String str : row.getAllValues()){
                if(!candidates.containsKey(str)){
                    candidates.put(str, 1);
                }
                else
                    candidates.put(str, candidates.get(str) + 1);
            }
        }
        apriori.setCandidateItems(candidates);
        apriori.run();*/


        /*
        for(double val : Common.heapSort(dataSet.normalizedData())){
            System.out.println(val);
        }


        KMeans thyroidKMeans = new KMeans();
        thyroidKMeans.setDataSet(dataSet);
        thyroidKMeans.setK(3);
        thyroidKMeans.setMaxIters(100);
        thyroidKMeans.setNumFeatures(6);
        thyroidKMeans.setDistance(EUCLEDIAN);
        thyroidKMeans.initiateCentroids();
        thyroidKMeans.run();
        thyroidKMeans.display();

        /*
        discretData.discretize(2, 5);

        System.out.println("Cloned:\n\n");
        for(Row row : discretData.getRows()){
            System.out.println(row.getValueByPosition(2));
        }

        System.out.println("Original:\n\n");
        for(Row row : dataSet.getRows()){
            System.out.println(row.getValueByPosition(2));
        }*/

        //dataSet.getMinCost();


        dataSet.discretize(1, 10);
        dataSet.discretize(2, 10);
        dataSet.discretize(3, 10);
        dataSet.discretize(4, 10);
        dataSet.discretize(5, 10);

        Apriori4j apriori = new Apriori4j();
        ArrayList<String> transactions = new ArrayList<>();
        for(Row row : dataSet.getRows())
            transactions.add(row.getTransaction());
        int minSup = 2;
        double minConf = 0.40;
        apriori.display(transactions.size(), transactions, minSup, minConf);

    }

    private void startStage(FXMLLoader loader) {
        AnchorPane root = null;
        try {
            root = loader.load();
            ClusterController cc = loader.getController();
            cc.setDataSet(dataSet);
            cc.originalRows = dataSet.getRows();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private String getDisplayedDataSet() throws IOException {
        DataSet temp = new DataSet();
        Parser parser = new Parser();

        temp.setRows(parser.getRows("/ds/Thyroid_Dataset.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        for(Row row : temp.getRows()){
            stringBuilder.append(row).append("\n");
        }

        return stringBuilder.toString();
    }

    private void initScatterChart() {
        valuesSC.getData().clear();
        double xAxisMin = dataSet.getArrtibuteMin(currPostion);
        double xAxisMax = dataSet.getArrtibuteMax(currPostion);
        double xRange = (xAxisMax - xAxisMin)/10;
        xAxis.setLowerBound(xAxisMin);
        xAxis.setUpperBound(xAxisMax);
        xAxis.setTickUnit(xRange);
        xAxis.setLabel(attributeCB.getValue());
        double yAxisMin = dataSet.getArrtibuteMin(getYAttr(yAttrCB.getValue()));
        double yAxisMax = dataSet.getArrtibuteMax(getYAttr(yAttrCB.getValue()));
        double yRange = (yAxisMax - yAxisMin)/10;
        yAxis.setLowerBound(yAxisMin);
        yAxis.setUpperBound(yAxisMax);
        yAxis.setTickUnit(yRange);
        xAxis.setLabel(yAttrCB.getValue());
        XYChart.Series<Number,Number> scatterSeries = dataSet.getScatterData(currPostion, getYAttr(yAttrCB.getValue()));
        valuesSC.getData().add(scatterSeries);


    }

    private void selectTab(MouseEvent action) {

        if(action.getSource() == valuesTAB){
            valuesAP.setVisible(true);
            histoAP.setVisible(false);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == histogramTAB){
            valuesAP.setVisible(false);
            histoAP.setVisible(true);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == scatterTAB){
            valuesAP.setVisible(false);
            histoAP.setVisible(false);
            scatterAP.setVisible(true);
            boxPlotAP.setVisible(false);
        }
        else if(action.getSource() == boxPlotTAB){
            valuesAP.setVisible(false);
            histoAP.setVisible(false);
            scatterAP.setVisible(false);
            boxPlotAP.setVisible(true);
        }
    }

    private void getSelectedPosition(String selectedItem) {

        switch (selectedItem){
            case "Class attribute": currPostion = 0; break;
            case "T3-resin uptake test": currPostion = 1; break;
            case "Total Serum thyroxin": currPostion = 2; break;
            case "Total serum triiodothyronine": currPostion = 3; break;
            case "basal thyroid-stimulating hormone": currPostion = 4; break;
            case "Maximal absolute difference of TSH value": currPostion = 5; break;
        }
    }

    private void initFields() {

        meanLabel.setText(String.valueOf(dataSet.calculateMean(currPostion)));
        modeLabel.setText(String.valueOf(dataSet.calculateMode(currPostion)));
        dataSet.setRows(Common.heapSort(dataSet.getRows(), currPostion));
        medianLabel.setText(String.valueOf(dataSet.calculateMedian(currPostion)));

    }

    private void getResults() {

        dataSet.setRows(Common.heapSort(dataSet.getRows(), currPostion));
        valuesLC.getData().clear();
        valuesBC.getData().clear();
        valuesSC.getData().clear();
        XYChart.Series<String,Integer> lineSeries = dataSet.getOccurenceCount(currPostion);
        XYChart.Series<String,Integer> barChartSeries = dataSet.getHistogramCount(currPostion);
        valuesBC.getData().add(barChartSeries);
        valuesLC.getData().add(lineSeries);
        initScatterChart();
        initBoxPlot(currPostion);

    }

    private void initBoxPlot(int currPostion) {
        String name = "/ds/boxplots/" + currPostion + ".png";
        new ZoomIn(boxPlotIV).play();
        boxPlotIV.setImage(new Image(ClassLoader.class.getResourceAsStream(name),
                boxPlotIV.getFitWidth(), boxPlotIV.getFitHeight(), false, false));
    }

    private int getYAttr(String values) {

        switch (values){
            case "Class attribute": return 0;
            case "T3-resin uptake test": return 1;
            case "Total Serum thyroxin": return 2;
            case "Total serum triiodothyronine": return 3;
            case "basal thyroid-stimulating hormone": return 4;
            case "Maximal absolute difference of TSH value": return 5;
            default: return  -1;
        }
    }

    private void getDataSet(DataSet ds) {

        Parser parser = new Parser();
        try {
            ds.setRows(parser.getRows("/ds/Thyroid_Dataset.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initAttrList(ChoiceBox choiceBox, int pos){

        choiceBox.setItems(attributesList);
        choiceBox.getSelectionModel().select(pos);

    }

}
