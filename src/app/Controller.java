package app;

import inc.Common;
import inc.DataSet;
import inc.Parser;
import inc.Row;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView informationIV;

    @FXML
    private Label meanLabel,medianLabel,modeLabel;

    @FXML
    private ChoiceBox<String> attributeCB;

    private DataSet dataSet = new DataSet();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            dataSet.setRows(Parser.getRows("/src/ds/Thyroid_Dataset.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Row row : dataSet.getRows()){
            System.out.println(row);
        }

        dataSet.setRows(Common.heapSort(dataSet.getRows(),3));

        System.out.println("============================== After Sorting =====================================");

        for(Row row : dataSet.getRows()){
            System.out.println(row);
        }

    }
}
