package inc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXScrollPane;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class Common {

    public static ObservableList<Row> heapSort(ObservableList<Row> list, int position){

        int n = list.size();
        for(int i = n/2 - 1; i >= 0; i--){
            heapify(list, n, i, position);
        }

        for(int i = n-1; i > 0; i--){
            Row temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);
            heapify(list, i, 0, position);
        }

        return list;

    }

    public static void heapify(ObservableList<Row> list, int n, int i, int pos){

        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        // See if left child of root exists and is
        // greater than root
        if(l < n && list.get(largest).getValueByPosition(pos) < list.get(l).getValueByPosition(pos)){
            largest = l;
        }

        if(r < n && list.get(largest).getValueByPosition(pos) < list.get(r).getValueByPosition(pos)){
            largest = r;
        }

        if(largest != i){
            Row temp = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, temp);
            heapify(list, n, largest, pos);
        }

    }

    public static ObservableList<Double> heapSort(ObservableList<Double> list){

        int n = list.size();
        for(int i = n/2 - 1; i >= 0; i--){
            heapify(list, n, i);
        }

        for(int i = n-1; i > 0; i--){
            double temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);
            heapify(list, i, 0);
        }

        return list;

    }

    public static void heapify(ObservableList<Double> list, int n, int i){

        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        // See if left child of root exists and is
        // greater than root
        if(l < n && list.get(largest) < list.get(l)){
            largest = l;
        }

        if(r < n && list.get(largest) < list.get(r)){
            largest = r;
        }

        if(largest != i){
            double temp = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, temp);
            heapify(list, n, largest);
        }

    }

    public static void loadDialog(JFXDialog dialog, JFXDialogLayout layout, StackPane stackPane, JFXScrollPane scrollPane){

        stackPane.setVisible(true);
        JFXButton btn = new JFXButton("Return");
        btn.setCancelButton(true);
        dialog = new JFXDialog(stackPane, layout , JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed(action -> {
            stackPane.setVisible(false);
            scrollPane.setVisible(false);
        });
        JFXDialog finalDialog = dialog;
        btn.setOnAction(Action -> {
            finalDialog.close();
            stackPane.setVisible(false);
            btn.setDefaultButton(false);
            scrollPane.setVisible(false);
        });
        layout.setActions(btn);
        dialog.show();

    }

    public static void customDialog(JFXDialog dialog, StackPane stackPane, JFXScrollPane scrollPane, String title, String body){
        JFXDialogLayout layout = new JFXDialogLayout();
        initLayout(layout, title, body);

        loadDialog(dialog, layout, stackPane, scrollPane);
    }

    public static void initLayout(JFXDialogLayout layout, String header, String body){

        Label label = new Label(header);
        layout.setHeading(label);
        layout.setBody(new Text(body));

    }
}
