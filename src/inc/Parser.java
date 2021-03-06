package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Parser {


    // Method for converting a benchmark into a graph
    public  ObservableList<Row> getRows(String filePath, boolean res) throws IOException {

//        FileReader reader = new FileReader( filePath);
        BufferedReader br;
        if(res) {
            InputStream inputStream = getClass().getResourceAsStream(filePath) ;
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            System.out.println("Resource");
        }
        else {
            FileReader fileReader = new FileReader(new File(filePath));
            br = new BufferedReader(fileReader);
        }

        ObservableList<Row> rows = FXCollections.observableArrayList();

        // read line by line
        String line;
        while ((line = br.readLine()) != null) {

            String[] arr = line.split(",");
            Row row = new Row(arr.length);
            for(int i = 0; i < arr.length; i++){
                row.values[i] = Double.parseDouble(arr[i]);
            }
            rows.add(row);
            }

        return rows;

    }
}
