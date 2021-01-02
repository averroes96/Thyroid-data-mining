package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class Parser {


    // Method for converting a benchmark into a graph
    public static ObservableList<Row> getRows(String filePath) throws IOException {

        String currPath = new File("").getAbsolutePath();
        System.out.println(currPath);

        FileReader reader = new FileReader(currPath + filePath);
        BufferedReader br = new BufferedReader(reader);
        ObservableList<Row> rows = FXCollections.observableArrayList();

        // read line by line
        String line;
        while ((line = br.readLine()) != null) {

            String[] arr = line.split(",");
            Row row = new Row();
            row.setAttributeClass(Integer.parseInt(arr[0]));
            row.setT3ResinUptakeTest(Integer.parseInt(arr[1]));
            row.setSerumThyroxin(Double.parseDouble(arr[2]));
            row.setSerumTriiodothyronine(Double.parseDouble(arr[3]));
            row.setTsh(Double.parseDouble(arr[4]));
            row.setNewTsh(Double.parseDouble(arr[4]));

            rows.add(row);
            }

        return rows;

    }
}
