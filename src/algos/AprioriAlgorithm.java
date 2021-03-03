package algos;

import inc.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AprioriAlgorithm {

    public ArrayList<String> transactions;
    public HashMap<String, Integer> candidates, frequentItems;
    public int minSup;
    public double minConfidence;
    int currItemset;

    public AprioriAlgorithm() {
        transactions = new ArrayList<>();
        frequentItems = new HashMap<>();
        candidates = new HashMap<>();
        minSup = 0;
        minConfidence = 0;
    }

    public AprioriAlgorithm(ArrayList<String> transactions) {
        this.transactions = transactions;
        frequentItems = new HashMap<>();
        candidates = new HashMap<>();
        minSup = 0;
        minConfidence = 0;
    }

    public AprioriAlgorithm(ArrayList<String> transactions, int minSup, double minConfidence) {
        this.transactions = transactions;
        this.minSup = minSup;
        this.minConfidence = minConfidence;
    }

    public void run(){
        
        candidates = initCandidates();
        frequentItems = initFrequentItems();
        currItemset = 1;
        /*System.out.println("Freq Items = " + frequentItems.size());
        for(String item : frequentItems.keySet())
            System.out.println(item + " => " + frequentItems.get(item));*/

        candidates = nCandidates();
        frequentItems = nFreqItems();

        candidates = nCandidates();
        frequentItems = nFreqItems();

        System.out.println("Freq Items = " + frequentItems.size());
        //for(String item : frequentItems.keySet())
        //    System.out.println(item + " => " + frequentItems.get(item));

        /*while(frequentItems.size() > 1){
            candidates = nCandidates();
        }*/
    }

    private HashMap<String, Integer> nFreqItems() {

        HashMap<String, Integer> temp = new HashMap<>();

        for(Map.Entry<String, Integer> entry : candidates.entrySet()){
            if(entry.getValue() > minSup)
                temp.put(entry.getKey(), entry.getValue());
        }

        return temp;
    }

    private HashMap<String, Integer> nCandidates() {

        HashMap<String, Integer> temp = new HashMap<>();
        String s1, s2;
        StringTokenizer strToken1, strToken2;

        currItemset++;
        if(currItemset == 2){
            for(Map.Entry<String, Integer> entry1 : frequentItems.entrySet()){
                for(Map.Entry<String, Integer> entry2 : frequentItems.entrySet()){
                    if(!entry1.getKey().equals(entry2.getKey()))
                        temp.put(entry1.getKey() + " " + entry2.getKey(), 0);
                }
            }
        }
        else{
            for(Map.Entry<String, Integer> entry1 : frequentItems.entrySet()) {
                for(Map.Entry<String, Integer> entry2 : frequentItems.entrySet()) {
                    s1 = "";
                    s2 = "";

                    strToken1 = new StringTokenizer(entry1.getKey());
                    strToken2 = new StringTokenizer(entry2.getKey());

                    for(int s = 0; s < currItemset - 2; s++) {
                        s1 = s1 + " " + strToken1.nextToken();
                        s2 = s2 + " " + strToken2.nextToken();
                        //System.out.println(s1);
                        //System.out.println(s2);
                    }

                    if(s2.compareToIgnoreCase(s1)==0) {
                        String addString = (s1 + " " + strToken1.nextToken() + " " + strToken2.nextToken()).trim();
                        temp.put(addString, 0);
                    }
                }
            }
        }

        candidateFreq(temp, currItemset);

        return temp;
    }

    private void candidateFreq(HashMap<String, Integer> temp, int size) {

        for(Map.Entry<String, Integer> entry : temp.entrySet()) {
            //System.out.println(candidate);
            String[] candItems = entry.getKey().split(" ");
            int cpt = 0;
            for (String transaction : transactions) {
                //System.out.println(transaction);
                String[] items = transaction.split(" ");
                    for (String item1 : candItems) {
                        //System.out.println("Token => " + token);
                        for (String item : items) {
                            //System.out.println(item);
                            if (item.equals(item1)){
                                //System.out.println(item);
                                cpt++;
                            }
                        }
                        //System.out.println(cpt);
                        //System.out.println(count);
                    }
                //System.out.println(count);
                if (cpt == size) {
                    //System.out.println(cpt);
                    //System.out.println(tokenizer.countTokens());
                    temp.put(entry.getKey(), entry.getValue() + 1);
                }
            }
        }
    }

    private HashMap<String, Integer> initFrequentItems() {

        HashMap<String, Integer> temp = new HashMap<>();

        for(String candidate : candidates.keySet()){
            if(candidates.get(candidate) > minSup)
                temp.put(candidate, candidates.get(candidate));
        }

        return temp;
    }

    private HashMap<String, Integer> initCandidates() {

        HashMap<String, Integer> temp = new HashMap<>();

        for(String transaction : transactions){
            String[] items = transaction.split(" ");
            for(String item : items)
                if(!temp.containsKey(item))
                    temp.put(item, 1);
                else
                    temp.put(item, temp.get(item) + 1);
        }

        return temp;
    }


}
