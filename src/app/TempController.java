package app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TempController {
    @FXML
    private TextArea textInput;

    private static Pattern CSV_NUMBER_PATTERN = Pattern.compile("((-?\\d+(\\.\\d+)?)\\s*,\\s*)*(-?\\d+(\\.\\d+)?)\\s*");
    private static String SEPARATOR = ",";

    //TODO: move parsing and validating to concrete model

    @FXML
    public void onGenerateClick() {
        String text = textInput.getText();
        if (!validateNumbers(text)) {
            showInvalidDataDialog();
            return;
        }

        ArrayList<Double> numbers;
        try {
            numbers = parseNumbers(text);
        } catch (IllegalArgumentException ex) {
            showParsingErrorDialog();
            return;
        }
        createBoxPlotStage(numbers);
    }

    private boolean validateNumbers(String text) {
        String[] stringNumbers = text.split(SEPARATOR);
        if (stringNumbers.length < 3) {
            return false;
        }
        return text.matches(CSV_NUMBER_PATTERN.pattern());
    }

    private ArrayList<Double> parseNumbers(String text) throws IllegalArgumentException {
        String[] stringNumbers = text.split(SEPARATOR);
        ArrayList<Double> numbers = new ArrayList<>(stringNumbers.length);
        for (String number : stringNumbers) {
            number = number.trim();
            numbers.add(Double.parseDouble(number));
        }
        return numbers;
    }

    private void createBoxPlotStage(ArrayList<Double> numbers) {

        Stage stage = BoxPlotController.createNewStage(numbers);
        stage.show();
    }

    private void showParsingErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText("Došlo k chybě při parsování dat");
        alert.setContentText("Tato chyba neměla nikdy nastat, proto pro vás nemůžeme nic udělat.");

        alert.showAndWait();
    }

    private void showInvalidDataDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText("Nebyly zadány validní hodnoty");
        alert.setContentText("Pro odstranění chyby zadejte alespoň 3 hodnoty v CSV formátu.\nNapříklad: 14, 25.20, 60, -20.47 ");

        alert.showAndWait();
    }


}