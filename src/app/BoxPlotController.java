package app;

import inc.BoxPlotDataModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.text.DecimalFormat;

public class BoxPlotController {

    Stage stage;

    @FXML
    private AnchorPane panel;

    @FXML
    private Group canvas;
    private static final int spaceForOnePoint = 20;
    private static final int minWidth = 400;
    private static final int minHeight = 600;
    private static final int numberAxisPosition = 200;
    private static final int paddingTopBot = 25;
    private static final int gapBetweenChartAndAxis = 30;
    private static final int chartPaddingLeft = numberAxisPosition + gapBetweenChartAndAxis;
    private static final DecimalFormat df = new DecimalFormat(".##");
    private static final int spaceForOneChar = 7;
    private static int numberOfPoints = 21;

    private double svgWidth = 400;
    private double svgHeight = 720;
    private BoxPlotDataModel model;


    public static Stage createNewStage(ArrayList<Double> numbers) {
        FXMLLoader fxmlLoader = new FXMLLoader(BoxPlotController.class.getResource("BoxPlot.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        BoxPlotController controller = fxmlLoader.getController();

        Stage boxPlotStage = new Stage();
        boxPlotStage.setTitle("Box plot - visualization");
        boxPlotStage.setMinWidth(minWidth);
        boxPlotStage.setMinHeight(minHeight);
        boxPlotStage.setScene(new Scene(root, controller.svgWidth, controller.svgHeight));
        controller.stage = boxPlotStage;
        controller.setData(new BoxPlotDataModel(numbers));
        controller.afterInitialize();
        return boxPlotStage;
    }

    void setData(BoxPlotDataModel model) {
        this.model = model;
    }

    public void afterInitialize() {
        setGUI();

        panel.widthProperty().addListener((obs, oldVal, newVal) -> {

            setGUI();
        });

        panel.heightProperty().addListener((obs, oldVal, newVal) -> {
            svgHeight = newVal.doubleValue();
            canvas.getChildren().clear();
            setGUI();
        });
    }

    private void setGUI() {
        svgWidth = panel.getWidth();
        svgHeight = panel.getHeight() - paddingTopBot * 2;
        canvas.setLayoutY(paddingTopBot);
        canvas.getChildren().clear();
        createLegend();
        createNumberAxis();
        createIqrBox();
        createMedianLine();
        createMinMaxAxis();
        createValuesOnAxis();
        createAveragePoint();
    }

    private void createLegend() {
        int firstPosition = 12;
        int textGapY = 20;
        createLegendItem(Color.GREEN, "Aritmetický průměr", firstPosition);
        createLegendItem(Color.RED, "Horní kvartil", firstPosition + textGapY);
        createLegendItem(Color.PURPLE, "Medián", firstPosition + textGapY * 2);
        createLegendItem(Color.BLUE, "Dolní kvartil", firstPosition + textGapY * 3);

    }

    private void createLegendItem(Color color, String text, int positionY) {
        Circle circle = new Circle();
        circle.setCenterX(20);
        circle.setCenterY(positionY - 3);
        circle.setRadius(4);
        circle.setFill(color);
        canvas.getChildren().add(circle);

        Text label = new Text();
        label.setText(text);
        label.setX(30);
        label.setY(positionY);
        canvas.getChildren().add(label);
    }

    private void createNumberAxis() {
        numberOfPoints = (int) Math.round(svgHeight / spaceForOnePoint);

        double range = model.getRawMax() - model.getRawMin();

        int block = (int) Math.ceil(range / (numberOfPoints - 1));
        for (int i = 0; i < numberOfPoints; i++) {
            double number = model.getRawMin() + block * i;
            double positionY = convertValueToAxis(number);
            Text label = new Text();
            String stringRepresentation = parseNumber(number);
            label.setText(stringRepresentation);
            int charCount = stringRepresentation.length();

            label.setX((numberAxisPosition) - (charCount) * spaceForOneChar - 5); //space for characters plus some reserve
            label.setY(positionY + 0.005d * svgHeight);
            canvas.getChildren().add(label);
            Line numberLine = new Line();
            numberLine.setStartX(numberAxisPosition - 3);
            numberLine.setEndX(numberAxisPosition + 3);
            numberLine.setStartY(positionY);
            numberLine.setEndY(positionY);
            canvas.getChildren().add(numberLine);
        }
        double minOnAxis = model.getRawMin() + block;
        double maxOnAxis = model.getRawMin() + block * (numberOfPoints - 1);
        double minY = convertValueToAxis(model.getRawMin() < minOnAxis ? model.getRawMin() : minOnAxis);
        double maxY = convertValueToAxis(model.getRawMax() > maxOnAxis ? model.getRawMax() : maxOnAxis);
        Line lineBetweenMinAndMax = new Line();
        lineBetweenMinAndMax.setStartX(numberAxisPosition);
        lineBetweenMinAndMax.setEndX(numberAxisPosition);
        lineBetweenMinAndMax.setStartY(minY);
        lineBetweenMinAndMax.setEndY(maxY);
        canvas.getChildren().add(lineBetweenMinAndMax);
    }

    private String parseNumber(double number) {
        if (number == Math.floor(number)) {
            return String.valueOf((int) number);
        } else
            return df.format(number);
    }

    private void createAveragePoint() {
        Circle circle = createCircleOnAxis(model.getAverage());
        circle.setRadius(3);
        circle.setFill(Color.GREEN);
        canvas.getChildren().add(circle);
    }

    private void createValuesOnAxis() {
        model.getAllValues().forEach(aDouble -> canvas.getChildren().add(createCircleOnAxis(aDouble)));
    }

    private void createIqrBox() {
        double lowerQuartileY = convertValueToAxis(model.getLowerQuartile());
        double upperQuartileY = convertValueToAxis(model.getUpperQuartile());

        Line lowerQuartileLine = new Line();
        lowerQuartileLine.setStroke(Color.BLUE);
        lowerQuartileLine.setStartX(chartPaddingLeft);
        lowerQuartileLine.setEndX(svgWidth - gapBetweenChartAndAxis);
        lowerQuartileLine.setStartY(lowerQuartileY);
        lowerQuartileLine.setEndY(lowerQuartileY);
        canvas.getChildren().add(lowerQuartileLine);

        Line upperQuartileLine = new Line();
        upperQuartileLine.setStroke(Color.RED);
        upperQuartileLine.setStartX(chartPaddingLeft);
        upperQuartileLine.setEndX(svgWidth - gapBetweenChartAndAxis);
        upperQuartileLine.setStartY(upperQuartileY);
        upperQuartileLine.setEndY(upperQuartileY);
        canvas.getChildren().add(upperQuartileLine);

        Line leftJoinLine = new Line();
        leftJoinLine.setStroke(Color.BLACK);
        leftJoinLine.setStartX(chartPaddingLeft);
        leftJoinLine.setEndX(chartPaddingLeft);
        leftJoinLine.setStartY(lowerQuartileY);
        leftJoinLine.setEndY(upperQuartileY);
        canvas.getChildren().add(leftJoinLine);

        Line rightJoinLine = new Line();
        rightJoinLine.setStroke(Color.BLACK);
        rightJoinLine.setStartX(svgWidth - gapBetweenChartAndAxis);
        rightJoinLine.setEndX(svgWidth - gapBetweenChartAndAxis);
        rightJoinLine.setStartY(lowerQuartileY);
        rightJoinLine.setEndY(upperQuartileY);
        canvas.getChildren().add(rightJoinLine);
    }

    private void createMedianLine() {
        double medianY = convertValueToAxis(model.getMedian());
        Line medianLine = new Line();
        medianLine.setStroke(Color.PURPLE);
        medianLine.setStartX(chartPaddingLeft);
        medianLine.setEndX(svgWidth - gapBetweenChartAndAxis);
        medianLine.setStartY(medianY);
        medianLine.setEndY(medianY);
        canvas.getChildren().add(medianLine);
    }

    private void createMinMaxAxis() {
        double minY = convertValueToAxis(model.getMin());
        Line minLine = new Line();
        minLine.setStartX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2 - 0.05d * svgWidth);
        minLine.setEndX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2 + 0.05d * svgWidth);
        minLine.setStartY(minY);
        minLine.setEndY(minY);
        canvas.getChildren().add(minLine);

        double maxY = convertValueToAxis(model.getMax());
        Line maxLine = new Line();
        maxLine.setStartX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2 - 0.05d * svgWidth);
        maxLine.setEndX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2 + 0.05d * svgWidth);
        maxLine.setStartY(maxY);
        maxLine.setEndY(maxY);
        canvas.getChildren().add(maxLine);

        Line lineBetweenMinAndMax = new Line();
        lineBetweenMinAndMax.setStartX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2);
        lineBetweenMinAndMax.setEndX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2);
        lineBetweenMinAndMax.setStartY(minY);
        lineBetweenMinAndMax.setEndY(maxY);
        canvas.getChildren().add(lineBetweenMinAndMax);
    }

    private Circle createCircleOnAxis(double value) {
        Circle circle = new Circle();
        circle.setCenterX((chartPaddingLeft + svgWidth - gapBetweenChartAndAxis) / 2);
        circle.setCenterY(convertValueToAxis(value));
        circle.setRadius(2);
        return circle;
    }

    public double convertValueToAxis(double value) {
        double res = (value - model.getRawMin()) / ((model.getRawMax()) - model.getRawMin());
        // revert for lowest number from bottom to highest number top
        return svgHeight - (res * svgHeight);
    }

}
