package app;

import algos.AprioriAlgorithm;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXScrollPane;
import inc.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable,Init {

    @FXML
    ImageView boxPlotIV;

    @FXML
    JFXButton displayBtn,clusteringBtn,aprioriBtn,uploadBtn,overviewBtn,infoBtn;

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
    private Label meanLabel,medianLabel,modeLabel,histogramTAB,valuesTAB,scatterTAB,boxPlotTAB;

    @FXML
    private ChoiceBox<String> attributeCB,yAttrCB;

    private DataSet dataSet;
    private DataSet discretData;
    private ObservableList<String> attributesList = FXCollections.observableArrayList();
    File selectedFile = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataSet = getDataSet("/ds/Thyroid_Dataset.txt", true);
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

        System.out.println(attributesList.size());

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
