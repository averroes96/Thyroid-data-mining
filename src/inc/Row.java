package inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// This class represent one single row(line) of the dataset
public class Row{

    // Attributes of our row
    int nbrFeatures;
    public double[] values;
    // Constructors

    public Row(int nbrFeatures) {
        this.nbrFeatures = nbrFeatures;
        values = new double[nbrFeatures];
    }

    public Row(double[] values) {
        this.values = values;
        this.nbrFeatures = values.length;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 1; i < values.length; i++)
            s.append(values[i]).append(", ");

        return s.toString();
    }

    // Method to get attribute value by position
    public double get(int attrPosition){

        return values[attrPosition];
    }

    public void set(int attrPosition, double value){

        if(attrPosition >= nbrFeatures)
            throw new IndexOutOfBoundsException();

        values[attrPosition] = value;
    }

    public boolean satisfies(String candidate) {

        int cpt = 0;
        int stringLength = candidate.split(",").length;


        String[] strs = candidate.split(",");
        for(String str : strs){
            if(getAllValues().contains(str.trim()))
                cpt++;
        }
        return cpt == stringLength;
    }

    public ArrayList<String> getAllValues(){

        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < nbrFeatures; i++){
            temp.add("I" + (i+1) + "_" + values[i]);
        }

        return temp;
    }

    public String getTransaction(){

        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < nbrFeatures; i++){
            temp.append("I").append(i + 1).append("_").append(values[i]).append(" ");
        }

        return temp.toString().substring(0, temp.length() - 1);

    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Row))
            return false;

        Row temp = (Row)o;

        for(int i = 0; i < nbrFeatures; i++){
            if(temp.values[i] != this.values[i])
                return false;
        }

        return true;
    }
}
