package algos;

import java.util.*;

public class TDSApriori {

    public HashMap<Integer,ArrayList<Set>> globalItemSet,globalFreqSet;
    public HashMap<Set, Integer> itemSetWithSup = new HashMap<>();
    public ArrayList<Set> L1ItemSet,currItemSet;
    public ArrayList<Set> C1ItemSet;
    public ArrayList<String> transactions;
    public int minSup;
    public double confidence;

    public TDSApriori(ArrayList<String> transactions, int support, double confidence) {

        this.transactions = transactions;
        this.minSup = support;
        this.confidence = confidence;
        initCandidates();
        L1ItemSet = getAboveMinSup(C1ItemSet, transactions, minSup, itemSetWithSup);
    }

    public void run(){

        currItemSet = L1ItemSet;
        int k = 2;

        while(!currItemSet.isEmpty()){

            globalFreqSet.put(k-1, currItemSet);
            ArrayList<Set> candidateSet = getUnion(currItemSet, k);
        }
    }

    private ArrayList<Set> getUnion(ArrayList<Set> currItemSet, int k) {

        ArrayList<Set> temp = new ArrayList<>();

        for(Set i : currItemSet){
            for(Set j : currItemSet){
                i.addAll(j);
                if(i.size() == k){
                    temp.add(i);
                }
            }
        }

        return temp;
    }

    private void initCandidates() {

        C1ItemSet = new ArrayList<>();

        for(String transaction : transactions){
            String[] items = transaction.split(" ");
            for(String item : items){
                Set itemSet = new HashSet(Collections.singleton(item));
                if(!C1ItemSet.contains(itemSet))
                    C1ItemSet.add(itemSet);
            }
        }

        for(Set set : C1ItemSet){
            System.out.println(set);
        }

    }

    private ArrayList<Set> getAboveMinSup(ArrayList<Set> candidates, ArrayList<String> transactions, int minSup, HashMap<Set, Integer> globalItemSet) {

        ArrayList<Set> freqItemSet = new ArrayList<>();
        HashMap<Set, Integer> localItemSet = new HashMap<>();

        for(Set candidate : candidates) {
            for(String transaction : transactions){
                Set<String> tranSet = new HashSet<>(Arrays.asList(transaction.split(" ")));
                System.out.println(tranSet);
                System.out.println(candidate);
                if(isSubset(candidate, tranSet)){
                    if (localItemSet.containsKey(candidate)) {
                        localItemSet.put(candidate, localItemSet.get(candidate) + 1);
                        globalItemSet.put(candidate, globalItemSet.get(candidate) + 1);
                    }
                }
                else{
                    localItemSet.put(candidate, 0);
                    globalItemSet.put(candidate, 0);
                }
            }
        }

        for(Map.Entry<Set, Integer> entry : localItemSet.entrySet()){
            double support = (float)(entry.getValue() / transactions.size());
            if(support >= minSup)
                freqItemSet.add(entry.getKey());
        }
        System.out.println("==================== Freq Items =====================");
        for(Set set : freqItemSet){
            System.out.println(set);
        }

        return freqItemSet;

    }

    public static <String> boolean isSubset(Set<String> setA, Set<String> setB) {
        return setB.containsAll(setA);
    }
}
