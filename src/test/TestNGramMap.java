package test;

import ngordnet.NGramMap;
import org.junit.Test;

public class TestNGramMap {

    @Test
    public void test() {
        // Loading should take less than 60 seconds
        NGramMap ngm = new NGramMap("./ngrams/all_words.csv",
                "./ngrams/total_counts.csv");
    }
    
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestTimeSeries.class);
    }
}
