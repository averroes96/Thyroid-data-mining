package inc;

public class Distance {

    public static double eucledianDistance(Row row1, Row row2, int numFeatures) {
        double sum = 0.0;
        for(int i = 0; i < numFeatures; i++) {
            sum += ((row1.getValueByPosition(i) - row2.getValueByPosition(i)) * (row1.getValueByPosition(i) - row2.getValueByPosition(i)));
        }
        //System.out.println(sum);
        return Math.sqrt(sum);
    }

    public static double manhattanDistance(Row row1, Row row2, int numFeatures){
        double sum = 0.0;
        for(int i = 0; i < numFeatures; i++) {
            sum += (Math.abs(row1.getValueByPosition(i) - row2.getValueByPosition(i)));
        }
        return sum;
    }
}
