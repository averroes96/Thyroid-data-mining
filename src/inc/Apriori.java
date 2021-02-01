package inc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class Apriori {

    private int minSup;
    private HashMap<String, Integer> candidateItems,frequentItems;
    private DataSet dataSet;

    public Apriori() {
        minSup = 0;
        candidateItems = new HashMap<>();
        frequentItems = new HashMap<>();
        dataSet = new DataSet();
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

        getFirstFrequents();

        while(frequentItems.size() > 0){
            generateCandidates();
        }

    }

    private void generateCandidates() {

        candidateItems.clear();
        int cpt = 0;
        for(String key1 : frequentItems.keySet()){
            for(String key2 : frequentItems.keySet()){
                if(!key1.equals(key2)){
                    for(Row row : dataSet.getRows()){
                        if(row.satisfies(new String[]{key1,key2})){
                            cpt++;
                        }
                    }
                    if(isCandidate(cpt)){
                        String newKey = key1 + "," + key2;
                        candidateItems.put(newKey, cpt);
                    }
                }
            }
        }
    }

    private void getFirstFrequents() {

        for(String key : candidateItems.keySet()){
            if(isCandidate(candidateItems.get(key))){
                frequentItems.put(key, candidateItems.get(key));
            }
        }

    }

    private boolean isCandidate(Integer integer) {
        return integer >= minSup;
    }
}
