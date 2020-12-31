package inc;

public class Attribute {

    private int position;
    private double value;

    public Attribute() {
        position = 0;
        value = 0;
    }

    public Attribute(int position, double value) {
        this.position = position;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
