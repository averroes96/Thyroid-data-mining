package app;

import inc.Common;
import inc.DataSet;
import inc.Parser;
import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView informationIV;

    @FXML
    private AnchorPane valuesAP;

    @FXML
    LineChart<String,Integer> valuesLC;

    @FXML
    private Label meanLabel,medianLabel,modeLabel;

    @FXML
    private ChoiceBox<String> attributeCB;

    private DataSet dataSet = new DataSet();
    private ObservableList<String> attributesList = FXCollections.observableArrayList();
    private int currPostion = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getDataSet();
        initAttrList();

        initFields();
        getResults();

        attributeCB.setOnAction(action -> {
            getSelectedPosition(attributeCB.getSelectionModel().getSelectedItem());
            initFields();
            getResults();
        });

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
        XYChart.Series<String,Integer> lineSeries = dataSet.getOccurenceCount(currPostion);
        valuesLC.getData().addAll(lineSeries);

    }

    private void getDataSet() {

        try {
            dataSet.setRows(Parser.getRows("/src/ds/Thyroid_Dataset.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Row row : dataSet.getRows()){
            System.out.println(row);
        }

    }

    private void initAttrList(){
        attributesList.addAll("Class attribute",
                "T3-resin uptake test",
                "Total Serum thyroxin",
                "Total serum triiodothyronine",
                "basal thyroid-stimulating hormone",
                "Maximal absolute difference of TSH value");

        attributeCB.setItems(attributesList);
        attributeCB.getSelectionModel().select(0);
    }
}
