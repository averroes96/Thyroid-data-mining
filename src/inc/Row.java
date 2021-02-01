package inc;

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
        serumThyroxin = 0;
        serumTriiodothyronine = 0;
        tsh = 0;
        newTsh = 0;
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
                return;
        }
    }

    public boolean satisfies(String[] strings) {

        int cpt = 0;

        for(String string : strings){
            if(string.equals(String.valueOf(attributeClass))){
                cpt++;
                continue;
            }
            if(string.equals(String.valueOf(t3ResinUptakeTest))){
                cpt++;
                continue;
            }
            if(string.equals(String.valueOf(serumThyroxin))){
                cpt++;
                continue;
            }
            if(string.equals(String.valueOf(serumTriiodothyronine))){
                cpt++;
                continue;
            }
            if(string.equals(String.valueOf(tsh))){
                cpt++;
                continue;
            }
            if(string.equals(String.valueOf(newTsh))){
                cpt++;
                continue;
            }
        }

        return cpt == strings.length;
    }
}
