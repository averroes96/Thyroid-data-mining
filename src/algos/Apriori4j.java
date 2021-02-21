package algos;

import inc.Row;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Apriori4j {

    ArrayList<String> candidates = new ArrayList<>();
    ArrayList<String> itemSet = new ArrayList<>();
    HashMap<String, Integer> frequentItems = new HashMap<>();
    ArrayList<String> finalFrequentItems = new ArrayList<>();
    int minSupport,minConfidence;
    ObservableList<String> transactions = FXCollections.observableArrayList();
    int itemsCount,countItemOccurrence=0,displayFrequentItemSetNumber=2,displayTransactionNumber=1;

    public Apriori4j() {
    }

    // Method to display the algorithm's process
    public void display(int noOfTransactions, ArrayList<String> transactions, int minimumSupport, double minimumConfidence)
    {
        for(int i = 0; i < noOfTransactions; i++)
        {
            String str = transactions.get(i);
            String[] items = str.split(" "); // get the items of the current transaction
            int count = items.length;
            for(int j=0;j<count;j++)
            {
                if(i==0)
                    itemSet.add(items[j]); // If first transaction than automatically add the items
                else
                    if(!(itemSet.contains(items[j]))) // If it doesn't exist in the itemset
                        itemSet.add(items[j]);
            }
        }

        itemsCount = itemSet.size();
        System.out.println("No of Items = "+itemsCount);
        System.out.println("No of Transactions = "+noOfTransactions);
        System.out.println("Minimum Support = "+minimumSupport);
        System.out.println("Minimum Confidence = "+minimumConfidence);

        System.out.println("'Items present in the dataset'");
        for(String i : itemSet)
            System.out.println("------> "+i);

        System.out.println("TRANSACTION ITEMSET");
        for(String i : transactions)
        {
            System.out.println("Transaction " + displayTransactionNumber + " = " + i);
            displayTransactionNumber++;
        }
        firstFrequentItemSet(noOfTransactions,transactions,minimumSupport,minimumConfidence);

    }

    // Method to get the most frequent itemset
    private void firstFrequentItemSet(int noOfTransactions, ArrayList<String> transactions, int minimumSupport, double minimumConfidence) {

        System.out.println("Frequent Itemset 1");
        for(int items = 0; items < itemSet.size() ; items++)
        {
            countItemOccurrence=0;
            String itemStr = itemSet.get(items);
            for(int t=0; t < noOfTransactions; t++)
            {
                String transactionStr = transactions.get(t);
                if(transactionStr.contains(itemStr)) // Check if transaction t has the current item
                {
                    countItemOccurrence++;
                }
            }

            if(countItemOccurrence >= minimumSupport)
            {
                System.out.println(itemStr+" => support = "+countItemOccurrence);
                finalFrequentItems.add(itemStr);
                frequentItems.put(itemStr, countItemOccurrence);
            }
        }

        aprioriStart(noOfTransactions, transactions, minimumSupport, minimumConfidence);
    }

    private void aprioriStart(int noOfTransactions, ArrayList<String> transactions, int minimumSupport, double minimumConfidence) {

        int itemsetNumber=1;

        candidates.addAll(finalFrequentItems); // Add all current frequent items to candidates list
        do
        {
            itemsetNumber++;
            generateCombinations(itemsetNumber); // Generate candidates
            checkFrequentItems(noOfTransactions, transactions, minimumSupport); // Check for current frequent items
        }
        while(candidates.size() > 1); // Until no more candidates

        System.out.println("Association Rules for Frequent Itemset");

        generateAssociationRules(noOfTransactions, transactions, minimumConfidence);
    }

    private void generateAssociationRules(int noOfTransactions, ArrayList<String> transactions, double minimumConfidence) {

        double confidence,confidence1;

        for(int i = 0; i < finalFrequentItems.size(); i++)
        {
            String itemSetStr = finalFrequentItems.get(i);
            double value = frequentItems.get(itemSetStr);
            String str = "", str1 = "";
            String[] item = itemSetStr.split(" ");
            int itemCountInString = item.length;
            if(itemCountInString == 2) /* for items = 2*/
            {
                double s = frequentItems.get(item[0]);
                confidence = value/s;
                if(confidence>=minimumConfidence)
                {
                    System.out.println(item[0]+" -> "+item[1]+" = Confidence = "+ confidence*100 +" and Support = "+(int)value+"");

                }
                double s1 = frequentItems.get(item[1]);
                confidence = value/s1;
                if(confidence>=minimumConfidence)
                {
                    System.out.println(item[1]+" -> "+item[0]+" = Confidence = "+ confidence*100+" and Support = "+(int)value+"");
                }
            }
            else /* for items > 2 */
            {
                for(int a=0; a < itemCountInString - 1; a++)
                {
                    if(a==0)
                        str = str + item[a];
                    else
                        str = str + " " + item[a];
                    for(int j= a+1; j < itemCountInString; j++)
                            str1 = str1 + " " + item[j];

                    double s = frequentItems.get(str);
                    confidence = value/s;
                    String st = str1.trim();
                    double s1 = frequentItems.get(st);
                    confidence1 = value/s1;

                    if(confidence >= minimumConfidence)
                        System.out.println(str+" -> "+str1+" = Confidence = "+confidence*100+" and Support = "+(int)value+"");
                    if(confidence1 >= minimumConfidence)
                    {
                        System.out.println(st+" -> "+str+" = Confidence = "+confidence1*100+" and Support = "+(int)value+"");
                    }
                    str1="";
                }
            }
        }
    }

    private void checkFrequentItems(int noOfTransactions, ArrayList<String> transactions, int minimumSupport) {

        ArrayList<String> combList = new ArrayList<>(candidates);

        System.out.println("Frequent Itemset "+displayFrequentItemSetNumber);

        for (String str : combList) {

            String[] items = str.split(" ");
            int count = items.length;
            int flag = 0, itemSetOccurence = 0;
            for (int t = 0; t < noOfTransactions; t++) {
                String transac = transactions.get(t);
                for (String itemStr : items) {
                    if (transac.contains(itemStr)) {
                        flag++;
                    }
                }

                if (flag == count) {
                    itemSetOccurence++;
                }
                flag = 0;
            }

            if (itemSetOccurence >= minimumSupport) {
                System.out.println(str + " => support = " + itemSetOccurence);
                frequentItems.put(str, itemSetOccurence);
                finalFrequentItems.add(str);
            }
        }
        displayFrequentItemSetNumber++;

        System.out.println(finalFrequentItems.size());
    }

    private void generateCombinations(int itemsetNumber){

        ArrayList<String> candidatesTemp = new ArrayList<>();
        String s1, s2;
        StringTokenizer strToken1, strToken2;

        if(itemsetNumber == 2){ // If items size = 2
            for(int i = 0; i < candidates.size(); i++){
                strToken1 = new StringTokenizer(candidates.get(i));
                s1 = strToken1.nextToken();
                for(int j=i+1; j<candidates.size(); j++){
                    strToken2 = new StringTokenizer(candidates.get(j));
                    s2 = strToken2.nextToken();
                    String addString = s1 + " " + s2;
                    candidatesTemp.add(addString);
                }
            }
        }
        else{ // if items size > 2
            for(int i = 0; i < candidates.size(); i++) {
                for(int j = i+1; j < candidates.size(); j++) {
                    s1 = "";
                    s2 = "";

                    strToken1 = new StringTokenizer(candidates.get(i));
                    strToken2 = new StringTokenizer(candidates.get(j));

                    for(int s = 0; s < itemsetNumber - 2; s++) {
                        s1 = s1 + " " + strToken1.nextToken();
                        s2 = s2 + " " + strToken2.nextToken();
                        //System.out.println(s1);
                        //System.out.println(s2);
                    }

                    if(s2.compareToIgnoreCase(s1)==0) {
                        String addString = (s1 + " " + strToken1.nextToken() + " " + strToken2.nextToken()).trim();
                        candidatesTemp.add(addString);
                    }
                }
            }
        }
        candidates.clear();
        candidates = new ArrayList<>(candidatesTemp);
        candidatesTemp.clear();
    }
}
