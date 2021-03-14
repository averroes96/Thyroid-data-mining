package inc;

public class AssociationRule {

    public String left;
    public String right;
    public int support;
    public double confidence;

    public AssociationRule(String left, String right, double confidence, int support) {
        this.left = left;
        this.right = right;
        this.confidence = confidence;
        this.support = support;
    }

    @Override
    public String toString() {
        return this.left + " -> " + this.right + " = { Confidence: " + this.confidence + " and support: " + this.support + "}";
    }
}
