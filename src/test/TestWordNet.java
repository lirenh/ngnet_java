package test;
import static org.junit.Assert.*;

import ngordnet.WordNet;
import org.junit.Test;
import org.junit.Before;

public class TestWordNet {
    WordNet wn;
    @Before
    public void setUp() {
        wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
//        wn = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");
//wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
    }
    
    //@Test
    public void testN() {
    }
  
    
    @Test
    public void testIsNoun() {
        assertTrue(wn.isNoun("jump"));
        assertTrue(wn.isNoun("leap"));
        assertTrue(wn.isNoun("nasal_decongestant"));
    }
    
    @Test
    public void TestAllNoun() {
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }
    }
    
    @Test
    public void testHypnoyms() {
        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }
        
        System.out.println("Hypnoyms of jump:");
        for (String noun : wn.hyponyms("jump")) {
            System.out.println(noun);
        }  
        
        System.out.println("Hypnoyms of change:");

        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        for (String noun : wn2.hyponyms("change")) {
            System.out.println(noun);
        }
    }
    
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestWordNet.class);        
    }
}
