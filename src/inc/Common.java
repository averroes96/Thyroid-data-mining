package inc;

import javafx.collections.ObservableList;

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
}
