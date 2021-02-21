package inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// This class represent one single row(line) of the dataset
public class Row{

    // Attributes of our row
    private int attributeClass;
    private int t3ResinUptakeTest;
    private double serumThyroxin;
    private double serumTriiodothyronine;
    private double tsh;
    private double newTsh;

    // Constructors

    public Row() {
        attributeClass = 0;
        t3ResinUptakeTest = 0;
        serumThyroxin = 0.0;
        serumTriiodothyronine = 0.0;
        tsh = 0.0;
        newTsh = 0.0;
    }

    public Row(int attributeClass, int t3ResinUptakeTest, double serumThyroxin, double serumTriiodothyronine, double tsh, double newTsh) {
        this.attributeClass = attributeClass;
        this.t3ResinUptakeTest = t3ResinUptakeTest;
        this.serumThyroxin = serumThyroxin;
        this.serumTriiodothyronine = serumTriiodothyronine;
        this.tsh = tsh;
        this.newTsh = newTsh;
    }

    // Getters and Setters

    public int getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(int attributeClass) {
        this.attributeClass = attributeClass;
    }

    public int getT3ResinUptakeTest() {
        return t3ResinUptakeTest;
    }

    public void setT3ResinUptakeTest(int t3ResinUptakeTest) {
        this.t3ResinUptakeTest = t3ResinUptakeTest;
    }

    public double getSerumThyroxin() {
        return serumThyroxin;
    }

    public void setSerumThyroxin(double serumThyroxin) {
        this.serumThyroxin = serumThyroxin;
    }

    public double getSerumTriiodothyronine() {
        return serumTriiodothyronine;
    }

    public void setSerumTriiodothyronine(double serumTriiodothyronine) {
        this.serumTriiodothyronine = serumTriiodothyronine;
    }

    public double getTsh() {
        return tsh;
    }

    public void setTsh(double tsh) {
        this.tsh = tsh;
    }

    public double getNewTsh() {
        return newTsh;
    }

    public void setNewTsh(double newTsh) {
        this.newTsh = newTsh;
    }

    @Override
    public String toString() {
        return "{" +
                "attributeClass=" + attributeClass +
                ", t3ResinUptakeTest=" + t3ResinUptakeTest +
                ", serumThyroxin=" + serumThyroxin +
                ", serumTriiodothyronine=" + serumTriiodothyronine +
                ", tsh=" + tsh +
                ", newTsh=" + newTsh +
                '}';
    }

    // Method to get attribute value by position
    public double getValueByPosition(int attrPosition){

        switch (attrPosition){
            case 0:
                return attributeClass;
            case 1:
                return t3ResinUptakeTest;
            case 2:
                return serumThyroxin;
            case 3:
                return serumTriiodothyronine;
            case 4:
                return tsh;
            case 5:
                return newTsh;
            default:
                return 0;
        }
    }

    public void set(int attrPosition, double value){

        switch (attrPosition){
            case 0:
                attributeClass = (int)value;
                break;
            case 1:
                t3ResinUptakeTest = (int)value;
                break;
            case 2:
                serumThyroxin = value;
                break;
            case 3:
                serumTriiodothyronine = value;
                break;
            case 4:
                tsh = value;
                break;
            case 5:
                newTsh = value;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException("Index out of range");
        }
    }

    // Method to get attribute value by position
    public void setValueByPosition(int attrPosition, double value){

        switch (attrPosition){
            case 0:
                attributeClass = (int)value;
                break;
            case 1:
                t3ResinUptakeTest = (int)value;
                break;
            case 2:
                serumThyroxin = value;
                break;
            case 3:
                serumTriiodothyronine = value;
                break;
            case 4:
                tsh = value;
                break;
            case 5:
                newTsh = value;
                break;
            default:
        }
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

        temp.add("A" + attributeClass);
        temp.add("B" + t3ResinUptakeTest);
        temp.add("C" + serumThyroxin);
        temp.add("D" + serumTriiodothyronine);
        temp.add("E" + tsh);
        temp.add("F" + newTsh);

        return temp;
    }

    public String getTransaction(){

        return "B" + t3ResinUptakeTest + " "
                + "C" + serumThyroxin + " "
                + "D" + serumTriiodothyronine + " "
                + "E" + tsh + " "
                + "F" + newTsh;

    }

    public double getDissimilarity(Row row){

        double sum = 0;

        sum += Math.abs(this.attributeClass - row.attributeClass);
        sum += Math.abs(this.t3ResinUptakeTest - row.t3ResinUptakeTest);
        sum += Math.abs(this.serumThyroxin - row.serumThyroxin);
        sum += Math.abs(this.serumTriiodothyronine - row.serumTriiodothyronine);
        sum += Math.abs(this.tsh - row.tsh);
        sum += Math.abs(this.newTsh - row.newTsh);

        return sum;

    }

    @Override
    public boolean equals(Object o) {
        Row temp = (Row)o;

        return attributeClass == temp.attributeClass &&
                t3ResinUptakeTest == temp.t3ResinUptakeTest &&
                serumThyroxin == temp.serumThyroxin &&
                serumTriiodothyronine == temp.serumTriiodothyronine &&
                tsh == temp.tsh &&
                newTsh == temp.newTsh;
    }
}
