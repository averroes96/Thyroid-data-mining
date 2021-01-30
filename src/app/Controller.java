package app;

import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXScrollPane;
import inc.Common;
import inc.DataSet;
import inc.Parser;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView informationIV,boxPlotIV;

    @FXML
    private JFXButton displayBtn;

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
    private ObservableList<String> attributesList = FXCollections.observableArrayList();
    private int currPostion = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getDataSet();

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

        valuesTAB.setOnMouseClicked(action -> {
            selectTab(action);
        });

        histogramTAB.setOnMouseClicked(action -> {
            selectTab(action);
        });

        scatterTAB.setOnMouseClicked(action -> {
            selectTab(action);
        });

        boxPlotTAB.setOnMouseClicked(action -> {
            selectTab(action);
        });

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

    private void getDataSet() {
        Parser parser = new Parser();
        try {
            dataSet.setRows(parser.getRows("/ds/Thyroid_Dataset.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initAttrList(ChoiceBox choiceBox, int pos){

        choiceBox.setItems(attributesList);
        choiceBox.getSelectionModel().select(pos);

    }

}
