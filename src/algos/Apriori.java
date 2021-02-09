package algos;

import inc.DataSet;
import inc.Row;

import java.util.*;

public class Apriori {

    private int minSup,itemsetSize;
    private HashMap<String, Integer> candidateItems,frequentItems;
    private DataSet dataSet;

    public Apriori() {
        minSup = 0;
        candidateItems = new HashMap<>();
        frequentItems = new HashMap<>();
        dataSet = new DataSet();
        itemsetSize = 1;
    }

    public int getMinSup() {
        return minSup;
    }

    public void setMinSup(int minSup) {
        this.minSup = minSup;
    }

    public HashMap<String, Integer> getCandidateItems() {
        return candidateItems;
    }

    public void setCandidateItems(HashMap<String, Integer> candidateItems) {
        this.candidateItems = candidateItems;
    }

    public HashMap<String, Integer> getFrequentItems() {
        return frequentItems;
    }

    public void setFrequentItems(HashMap<String, Integer> frequentItems) {
        this.frequentItems = frequentItems;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void run(){

        System.out.println("Candidates size : " + candidateItems.size());

        getFrequents();

        while(frequentItems.size() > 1 && itemsetSize <= 6){
            generateCandidates();
            getFrequents();
        }


    }

    private void generateCandidates() {

        itemsetSize++;
        candidateItems.clear();
        int cpt;

        System.out.println("Generating candidates " + itemsetSize);

        for(String key1 : frequentItems.keySet()){
            for(String key2 : frequentItems.keySet()){
                if(!key1.equals(key2)){
                    //System.out.println("Original = " + key1 + ", " + key2);
                    ArrayList<String> newCandidates = generateCandidate(new String[]{key1,key2});
                    cpt = 0;
                    for(String candidate: newCandidates) {
                        for(Row row : dataSet.getRows()){
                                if (row.satisfies(candidate)) {
                                    cpt++;
                                }
                        }
                        candidateItems.put(candidate, cpt);
                    }
                }
            }
        }

        System.out.println("Candidate Items:");
        System.out.println(candidateItems.size());
    }

    private ArrayList<String> generateCandidate(String[] strings) {
        ArrayList<String> temp = new ArrayList<>();

        List<String> key1 = Arrays.asList(strings[0].split(","));
        List<String> key2 = Arrays.asList(strings[1].split(","));

        extractCombinations(temp, key1, key2);

        extractCombinations(temp, key2, key1);

        //for(String str : temp)
        //    System.out.println(str);

        return temp;
    }

    private void extractCombinations(ArrayList<String> temp, List<String> key1, List<String> key2) {
        for(String str : key2){
            if(!key1.contains(str)){
                String tempStr = key1.toString().replace("[","").replace("]","").trim();
                if(!temp.contains(str + "," + tempStr) && (str + "," + tempStr).split(",").length == itemsetSize)
                    temp.add(tempStr + "," + str);
            }
        }
    }

    private void getFrequents() {

        frequentItems.clear();
        for(String key : candidateItems.keySet()){
            if(isCandidate(candidateItems.get(key))){
                frequentItems.put(key, candidateItems.get(key));
            }
        }


        System.out.println("Frequent Items:");
        System.out.println(frequentItems.size());

        for(Map.Entry entry : frequentItems.entrySet()){
            System.out.println("Key: " + entry.getKey() + " Values: " + entry.getValue());
        }


    }

    private boolean isCandidate(Integer integer) {
        return integer >= minSup;
    }
}
