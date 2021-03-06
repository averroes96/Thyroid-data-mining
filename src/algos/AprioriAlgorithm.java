package algos;

import inc.Row;

import java.lang.reflect.Array;
import java.util.*;

public class AprioriAlgorithm {

    public ArrayList<String> transactions;
    public HashMap<String, Integer> candidates, freqItems, associationRules, tempFreqItems;
    public int minSup;
    public double minConfidence;
    int currItemset;

    public AprioriAlgorithm() {
        transactions = new ArrayList<>();
        freqItems = new HashMap<>();
        candidates = new HashMap<>();
        minSup = 0;
        minConfidence = 0;
    }

    public AprioriAlgorithm(ArrayList<String> transactions) {
        this.transactions = transactions;
        freqItems = new HashMap<>();
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
  /*
        candidates = initCandidates();
        initFrequentItems();
        currItemset = 1;
        /*System.out.println("Freq Items = " + frequentItems.size());
        for(String item : frequentItems.keySet())
            System.out.println(item + " => " + frequentItems.get(item));*/
/*
        candidates = nCandidates();
        nFreqItems();

        candidates = nCandidates();
        nFreqItems();

        candidates = nCandidates();
        nFreqItems();
        /*
        candidates = nCandidates();
        frequentItems = nFreqItems();

        /*candidates = nCandidates();
        nFreqItems();

        candidates = nCandidates();
        nFreqItems();

        candidates = nCandidates();
        nFreqItems();

        candidates = nCandidates();
        nFreqItems();*/

        /*System.out.println("Freq Items = " + freqItems.size());
        for(String item : freqItems.keySet())
            System.out.println(item + " => " + freqItems.get(item));*/

        String temp = "A B C D E F";
        String arr[] = temp.split(" ");
        int r = 5;
        int n = arr.length;
        String[] data = new String[r];
        ArrayList<String> tempList = new ArrayList<>();
        tempList = combinationUtil(arr, data, 0, n-1, 0, r, tempList) ;
        //checkSubElements(temp);

        for(String str : tempList)
            System.out.println(str);

    }

    public void getAssociationRules(){

        for(Map.Entry<String, Integer> entry : freqItems.entrySet()){
            //ArrayList<String> subsets = getItemSubsets(entry.getKey().split(" "), 0, 0, subsets);
        }
    }

    private void getItemSubsets(String[] key, int start, int end, ArrayList<String> temp) {

        for(int i = 0; i < key.length; i++){

        }

    }

    private void nFreqItems() {

        tempFreqItems = new HashMap<>();
        for(Map.Entry<String, Integer> entry : candidates.entrySet()){
            if(entry.getValue() > minSup) {
                freqItems.put(entry.getKey(), entry.getValue());
                tempFreqItems.put(entry.getKey(), entry.getValue());
            }
        }

        System.out.println("Temp Items = " + tempFreqItems.size());

    }

    private HashMap<String, Integer> nCandidates() {

        HashMap<String, Integer> temp = new HashMap<>();
        String s1, s2;
        StringTokenizer strToken1, strToken2;

        currItemset++;
        if(currItemset == 2){
            for(Map.Entry<String, Integer> entry1 : tempFreqItems.entrySet()){
                for(Map.Entry<String, Integer> entry2 : tempFreqItems.entrySet()){
                    if(!entry1.getKey().equals(entry2.getKey()))
                        temp.put(entry1.getKey() + " " + entry2.getKey(), 0);
                }
            }
        }
        else{
            for(Map.Entry<String, Integer> entry1 : tempFreqItems.entrySet()) {
                for(Map.Entry<String, Integer> entry2 : tempFreqItems.entrySet()) {
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
                        if (checkSubElements(addString))
                            temp.put(addString, 0);
                    }
                }
            }
        }

        candidateFreq(temp, currItemset);

        return temp;
    }

    private boolean checkSubElements(String addString) {

        return true;
    }

    private ArrayList<String> combinationUtil(String arr[], String data[], int start, int end, int index, int r, ArrayList<String> temp){

        // Current combination is ready to be printed, print it
        if (index == r)
        {
            String str = "";
            for (int j=0; j<r; j++)
                str += data[j] + " ";

            temp.add(str.trim());
            return temp;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r, temp);
        }

        return temp;
    }

    private void candidateFreq(HashMap<String, Integer> temp, int size) {

        for(Map.Entry<String, Integer> entry : temp.entrySet()) {
            //System.out.println(candidate);
            String[] candItems = entry.getKey().split(" ");
            int cpt;
            for (String transaction : transactions) {
                //System.out.println(transaction);
                cpt = 0;
                String[] items = transaction.split(" ");
                    for (String item1 : items) {
                        //System.out.println("Token => " + token);
                        for (String item : candItems) {
                            //System.out.println(item);
                            if (item.equals(item1)){
                                //System.out.println(item + " == " + item1);
                                cpt++;
                            }
                        }
                        //System.out.println(cpt);
                        //System.out.println(count);
                    }
                //System.out.println(count);
                if (cpt == size) {
                    //System.out.println(transaction);
                    //System.out.println(entry.getKey());
                    //System.out.println(cpt);
                    //System.out.println(size);
                    temp.put(entry.getKey(), entry.getValue() + 1);
                }
            }
        }
    }

    private void initFrequentItems() {

        tempFreqItems = new HashMap<>();

        for(String candidate : candidates.keySet()){
            if(candidates.get(candidate) > minSup) {
                freqItems.put(candidate, candidates.get(candidate));
                tempFreqItems.put(candidate, candidates.get(candidate));
            }
        }

        System.out.println("Temp Items = " + tempFreqItems.size());

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

    private boolean isMoreThanMin(){

        for(Map.Entry<String, Integer> entry : candidates.entrySet()){
            if(entry.getValue() > minSup)
                return true;
        }
        return false;
    }


}
