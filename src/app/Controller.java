package app;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView informationIV;

    @FXML
    private AnchorPane valuesAP,histoAP,scatterAP;

    @FXML
    LineChart<String,Integer> valuesLC;

    @FXML
    BarChart valuesBC;

    @FXML
    ScatterChart valuesSC;

    @FXML
    private Label meanLabel,medianLabel,modeLabel,histogramTAB,valuesTAB,scatterTAB;

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
            valuesSC.getData().clear();
            XYChart.Series<String,Double> scatterSerries = dataSet.getScatterData(currPostion, getYAttr(yAttrCB.getValue()));
            valuesSC.getData().addAll(scatterSerries);

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

    }

    private void selectTab(MouseEvent action) {

        if(action.getSource() == valuesTAB){
            valuesAP.setVisible(true);
            histoAP.setVisible(false);
            scatterAP.setVisible(false);
        }
        else if(action.getSource() == histogramTAB){
            valuesAP.setVisible(false);
            histoAP.setVisible(true);
            scatterAP.setVisible(false);
        }
        else if(action.getSource() == scatterTAB){
            valuesAP.setVisible(false);
            histoAP.setVisible(false);
            scatterAP.setVisible(true);
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
        XYChart.Series<String,Double> scatterSerries = dataSet.getScatterData(currPostion, getYAttr(yAttrCB.getValue()));
        valuesLC.getData().addAll(lineSeries);
        valuesBC.getData().addAll(barChartSeries);
        valuesSC.getData().addAll(scatterSerries);

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

        try {
            dataSet.setRows(Parser.getRows("/src/ds/Thyroid_Dataset.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initAttrList(ChoiceBox choiceBox, int pos){

        choiceBox.setItems(attributesList);
        choiceBox.getSelectionModel().select(pos);

    }

}
