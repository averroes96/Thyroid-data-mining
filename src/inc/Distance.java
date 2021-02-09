package inc;

public class Distance {

    public static double eucledianDistance(Row row1, Row row2) {
        double sum = 0.0;
        for(int i = 0; i < 6; i++) {
            sum += ((row1.getValueByPosition(i) - row2.getValueByPosition(i)) * (row1.getValueByPosition(i) - row2.getValueByPosition(i)));
        }
        return Math.sqrt(sum);
    }

    public static double manhattanDistance(Row row1, Row row2){
        double sum = 0.0;
        for(int i = 0; i < 6; i++) {
            sum += (Math.abs(row1.getValueByPosition(i) - row2.getValueByPosition(i)));
        }
        return sum;
    }
}
