package algos;

import inc.Row;
import org.apache.commons.collections4.ListUtils;

import java.lang.reflect.Array;
import java.util.*;

public class AprioriAlgorithm {

    public ArrayList<String> transactions;
    public HashMap<String, Integer> candidates, freqItems, associationRules, currFreqItems,prevFreqItems;
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

        candidates = initCandidates();
        initFrequentItems();
        currItemset = 1;
        /*System.out.println("Freq Items = " + frequentItems.size());
        for(String item : frequentItems.keySet())
            System.out.println(item + " => " + frequentItems.get(item));*/

        System.out.println("Candidates = " + candidates.size());

        candidates = nCandidates();
        nFreqItems();

        System.out.println("Candidates = " + candidates.size());

        System.out.println("Prev Items = " + prevFreqItems.size());
        for(String item : prevFreqItems.keySet())
            System.out.println(item + " => " + prevFreqItems.get(item));

        System.out.println("============================================================================================");

        System.out.println("Curr Items = " + currFreqItems.size());
        for(String item : currFreqItems.keySet())
            System.out.println(item + " => " + currFreqItems.get(item));

        candidates = nCandidates();
        nFreqItems();

        System.out.println("Candidates = " + candidates.size());
        for(String item : candidates.keySet())
            System.out.println(item + " => " + candidates.get(item));

        System.out.println("Prev Items = " + prevFreqItems.size());
        for(String item : prevFreqItems.keySet())
            System.out.println(item + " => " + prevFreqItems.get(item));

        System.out.println("============================================================================================");

        System.out.println("Curr Items = " + currFreqItems.size());
        for(String item : currFreqItems.keySet())
            System.out.println(item + " => " + currFreqItems.get(item));


        /*System.out.println("Freq Items = " + freqItems.size());
        for(String item : freqItems.keySet())
            System.out.println(item + " => " + freqItems.get(item));*/

    }

    public void getAssociationRules(){

        for(Map.Entry<String, Integer> entry : freqItems.entrySet()){
            //ArrayList<String> subsets = getItemSubsets(entry.getKey().split(" "), 0, 0, subsets);
        }
    }

    private void nFreqItems() {

        prevFreqItems = new HashMap<>();

        for(Map.Entry<String, Integer> entry : currFreqItems.entrySet()){
            prevFreqItems.put(entry.getKey(), entry.getValue());
        }

        currFreqItems = new HashMap<>();
        for(Map.Entry<String, Integer> entry : candidates.entrySet()){
            if(entry.getValue() >= minSup) {
                freqItems.put(entry.getKey(), entry.getValue());
                currFreqItems.put(entry.getKey(), entry.getValue());
            }
        }

    }

    private HashMap<String, Integer> nCandidates() {

        HashMap<String, Integer> temp = new HashMap<>();

        currItemset++;
        if(currItemset == 2){
            for(Map.Entry<String, Integer> entry1 : currFreqItems.entrySet()){
                for(Map.Entry<String, Integer> entry2 : currFreqItems.entrySet()){
                    if(!entry1.getKey().equals(entry2.getKey()))
                        temp.put(entry1.getKey() + " " + entry2.getKey(), 0);
                }
            }
        }
        else{
            for(Map.Entry<String, Integer> entry1 : currFreqItems.entrySet()) {
                for(Map.Entry<String, Integer> entry2 : currFreqItems.entrySet()) {

                    if(!entry1.equals(entry2)) {

                        String[] elements = entry2.getKey().split(" ");
                        String newCandidate = entry1.getKey();

                        for (String element : elements) {
                            String tempCandidate = newCandidate + " " + element;
                            if (checkSubElements(tempCandidate) && checkRedundance(tempCandidate) && checkExistence(temp, tempCandidate)) {
                                temp.put(tempCandidate, 0);
                            }
                        }
                    }
                }
            }
        }

        candidateFreq(temp, currItemset);

        return temp;
    }

    private boolean checkExistence(HashMap<String, Integer> temp, String tempCandidate) {

        for(Map.Entry<String, Integer> entry1 : temp.entrySet()){
            if(strEqual(tempCandidate.split(" "), entry1.getKey().split(" ")))
                return false;
        }

        return true;
    }

    private boolean checkRedundance(String addString) {

        String[] list = addString.split(" ");

        for (int i = 0; i < list.length - 1 ; i++){
            for (int j = i + 1; j < list.length; j++){
                if(list[i].equals(list[j])) {

                    return false;
                }
            }
        }

        //System.out.println(addString + " is not redundant");
        return true;
    }

    private boolean checkSubElements(String addString) {

        String arr[] = addString.split(" ");
        int r = currItemset - 1;
        int n = arr.length;
        String[] data = new String[r];
        ArrayList<String> tempList = new ArrayList<>();
        tempList = combinationUtil(arr, data, 0, n - 1, 0, r, tempList);

        for(String sub : tempList){
            //System.out.println(sub + " >>> " + tempList);
            if(!checkSubItem(sub))
                return false;
        }

        return true;
    }

    private boolean checkSubItem(String first){

        for(Map.Entry<String, Integer> entry : currFreqItems.entrySet()){

            String[] secondList = entry.getKey().split(" ");
            String[] firstList = first.split(" ");

            if(strEqual(firstList, secondList)) {
                return true;
            }
        }

        return false;

    }

    private boolean strEqual(String[] firstList, String[] secondList) {

        int len = firstList.length;
        int cpt = 0;

        for(String second : secondList){
            for(String first : firstList){
                if(second.equals(first))
                    cpt++;
            }
        }
        return cpt == len;
    }

    private ArrayList<String> combinationUtil(String arr[], String data[], int start, int end, int index, int r, ArrayList<String> temp){

        if (index == r)
        {
            String str = "";
            for (int j=0; j<r; j++)
                str += data[j] + " ";

            temp.add(str.trim());
            return temp;
        }

        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r, temp);
        }

        return temp;
    }

    private void candidateFreq(HashMap<String, Integer> temp, int size) {

        for(Map.Entry<String, Integer> entry : temp.entrySet()) {
            String[] candItems = entry.getKey().split(" ");
            int cpt;
            for (String transaction : transactions) {
                cpt = 0;
                String[] items = transaction.split(" ");
                    for (String item1 : items) {
                        for (String item : candItems) {
                            if (item.equals(item1)){
                                cpt++;
                            }
                        }
                    }
                if (cpt == size) {
                    temp.put(entry.getKey(), entry.getValue() + 1);
                }
            }
        }
    }

    private void initFrequentItems() {

        currFreqItems = new HashMap<>();

        for(String candidate : candidates.keySet()){
            if(candidates.get(candidate) >= minSup) {
                freqItems.put(candidate, candidates.get(candidate));
                currFreqItems.put(candidate, candidates.get(candidate));
            }
        }

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
