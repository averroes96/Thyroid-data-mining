package app;

import inc.DataSet;
import inc.Parser;
import inc.Row;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView informationIV;

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

    }
}
