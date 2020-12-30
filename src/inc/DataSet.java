package inc;

import java.util.ArrayList;

public class DataSet {

    private IntegerAttribute attributeClass;
    private IntegerAttribute t3ResinUptakeTest;
    private DoubleAttribute serumThyroxin;
    private DoubleAttribute serumTriiodothyronine;
    private DoubleAttribute tsh;
    private DoubleAttribute newTsh;

    public DataSet() {
        attributeClass = new IntegerAttribute();
        t3ResinUptakeTest = new IntegerAttribute();
        serumThyroxin = new DoubleAttribute();
        serumTriiodothyronine = new DoubleAttribute();
        tsh = new DoubleAttribute();
        newTsh = new DoubleAttribute();
    }

    public DataSet(IntegerAttribute attributeClass, IntegerAttribute t3ResinUptakeTest, DoubleAttribute serumThyroxin, DoubleAttribute serumTriiodothyronine, DoubleAttribute tsh, DoubleAttribute newTsh) {
        this.attributeClass = attributeClass;
        this.t3ResinUptakeTest = t3ResinUptakeTest;
        this.serumThyroxin = serumThyroxin;
        this.serumTriiodothyronine = serumTriiodothyronine;
        this.tsh = tsh;
        this.newTsh = newTsh;
    }

    public IntegerAttribute getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(IntegerAttribute attributeClass) {
        this.attributeClass = attributeClass;
    }

    public IntegerAttribute getT3ResinUptakeTest() {
        return t3ResinUptakeTest;
    }

    public void setT3ResinUptakeTest(IntegerAttribute t3ResinUptakeTest) {
        this.t3ResinUptakeTest = t3ResinUptakeTest;
    }

    public DoubleAttribute getSerumThyroxin() {
        return serumThyroxin;
    }

    public void setSerumThyroxin(DoubleAttribute serumThyroxin) {
        this.serumThyroxin = serumThyroxin;
    }

    public DoubleAttribute getSerumTriiodothyronine() {
        return serumTriiodothyronine;
    }

    public void setSerumTriiodothyronine(DoubleAttribute serumTriiodothyronine) {
        this.serumTriiodothyronine = serumTriiodothyronine;
    }

    public DoubleAttribute getTsh() {
        return tsh;
    }

    public void setTsh(DoubleAttribute tsh) {
        this.tsh = tsh;
    }

    public DoubleAttribute getNewTsh() {
        return newTsh;
    }

    public void setNewTsh(DoubleAttribute newTsh) {
        this.newTsh = newTsh;
    }
}
