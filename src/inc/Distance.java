package inc;

public class Distance implements Init {

    public static double eucledianDistance(Row row1, Row row2, int numFeatures) {
        double sum = 0.0;
        for(int i = 1; i < numFeatures; i++) {
            sum += ((row1.values[i] - row2.values[i]) * (row1.values[i] - row2.values[i]));
        }
        return Math.sqrt(sum);
    }

    public static double manhattanDistance(Row row1, Row row2, int numFeatures){
        double sum = 0.0;
        for(int i = 1; i < numFeatures; i++) {
            sum += (Math.abs(row1.values[i] - row2.values[i]));
        }
        return sum;
    }

    public static double calculateDistance(int type, Row row1, Row row2, int numFeatures){

        if(type == EUCLEDIAN)
            return eucledianDistance(row1, row2, numFeatures);
        else
            return manhattanDistance(row1, row2, numFeatures);

    }
}
