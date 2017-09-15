package ngordnet;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;

/** this is Delegation. TimeSeries is extension. 
 * YearlyRecord doesn't know which year it comes from, it's someone else's job. */
public class YearlyRecord {
    // package protected: used for defensive copy
    HashMap<String, Integer> countMap;
    private TreeMap<String, Integer> sortedCountMap; 
    private HashMap<String, Integer> rankMap;
    private boolean cached;
    
    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        countMap = new HashMap<>();
        sortedCountMap = new TreeMap<>(new WordComparator());
        rankMap = new HashMap<>();
        cached = false;
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        this();
        countMap.putAll(otherCountMap);
        sortedCountMap.putAll(otherCountMap);
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        if (countMap.containsKey(word)) {
            return countMap.get(word);
        }
        return 0;
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        countMap.put(word, count);
        sortedCountMap.put(word, count);
        cached = false;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return countMap.size();
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        return sortedCountMap.keySet();
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        return new ArrayList<Number>(sortedCountMap.values());
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        if (!countMap.containsKey(word)) {
            return -1;
        }
        if (!cached) {
            buildRank();
        }
        return rankMap.get(word);
    }

    private void buildRank() {
        rankMap = new HashMap<>();
        int i = countMap.size();
        for (String s : sortedCountMap.keySet()) {
            rankMap.put(s, i);
            i--;
        }
        cached = true;
    }
    
    private class WordComparator implements Comparator<String> {
        public int compare(String a, String b) {
            // same rank not allowed
            if (countMap.get(a).compareTo(countMap.get(b)) > 0) {
                return 1;
            }
            return -1;
        }
    }
} 