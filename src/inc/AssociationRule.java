package inc;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AssociationRule {

    SimpleStringProperty left;
    SimpleStringProperty right;
    SimpleIntegerProperty support;
    SimpleDoubleProperty confidence;

    public AssociationRule(String left, String right, double confidence, int support) {
        this.left = new SimpleStringProperty(left);
        this.right = new SimpleStringProperty(right);
        this.confidence = new SimpleDoubleProperty(confidence);
        this.support = new SimpleIntegerProperty(support);
    }

    public String getLeft() {
        return left.get();
    }

    public SimpleStringProperty leftProperty() {
        return left;
    }

    public void setLeft(String left) {
        this.left.set(left);
    }

    public String getRight() {
        return right.get();
    }

    public SimpleStringProperty rightProperty() {
        return right;
    }

    public void setRight(String right) {
        this.right.set(right);
    }

    public int getSupport() {
        return support.get();
    }

    public SimpleIntegerProperty supportProperty() {
        return support;
    }

    public void setSupport(int support) {
        this.support.set(support);
    }

    public double getConfidence() {
        return confidence.get();
    }

    public SimpleDoubleProperty confidenceProperty() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence.set(confidence);
    }

    @Override
    public String toString() {
        return this.left.getValue()
                + " -> " + this.right.getValue()
                + " = { Confidence: "
                + this.confidence.getValue()
                + " and support: "
                + this.support.getValue()
                + "}";
    }
}
