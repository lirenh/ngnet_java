package ngordnet;

import edu.princeton.cs.algs4.Digraph;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

public class WordNet {
    private Digraph graph;
    private Map<Integer, Set<String>> map; //vertex num to string set
    private Map<String, Set<Integer>> pam; //string to vertex set
    private Set<String> allNouns;

    /** Creates a WordNet using files form SYNSETFILENAME and HYPONYMFILENAME */
    public WordNet(String synsetFilename, String hyponymFilename) {
        Path sfPath = Paths.get(synsetFilename);
        Path hfPath = Paths.get(hyponymFilename);

        map = new HashMap<>(); // Instantiate
        pam = new HashMap<>(); // Instantiate
        allNouns = new HashSet<>(); // Instantiate
        int total = 0;
        try (BufferedReader s = Files.newBufferedReader(sfPath); 
                BufferedReader h = Files.newBufferedReader(hfPath)) {
            String sLine = null;
            while ((sLine = s.readLine()) != null) {
                String[] strs = sLine.split(",");
                int key = Integer.parseInt(strs[0]);
                String[] nouns = strs[1].split(" ");
                Set<String> values = new HashSet<>(Arrays.asList(nouns));
                map.put(key, values);
                allNouns.addAll(values);               
                // pam
                for (String str : nouns) {
                    if (!pam.containsKey(str)) {
                        Set<Integer> set = new HashSet<>();
                        set.add(key);
                        pam.put(str, set);
                    } else {
                        pam.get(str).add(key);
                    }                   
                }                
                total++;
            }

            graph = new Digraph(total); // Instantiate
            String hLine = null;
            while ((hLine = h.readLine()) != null) {
                String[] strs = hLine.split(",");
                int edge0 = Integer.parseInt(strs[0]);
                for (int i = 1; i < strs.length; i++) {
                    int edge = Integer.parseInt(strs[i]);
                    graph.addEdge(edge0, edge);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /* Returns true if NOUN is a word in some synset. */
    public boolean isNoun(String noun) {
        return allNouns.contains(noun);
    }

    /* Returns the set of all nouns. */
    public Set<String> nouns() {
        return allNouns;
    }

    /**
     * Returns the set of all hyponyms of WORD as well as all synonyms of WORD.
     * If WORD belongs to multiple synsets, return all hyponyms of all of these
     * synsets. See http://goo.gl/EGLoys for an example. Do not include hyponyms
     * of synonyms.
     */
    public Set<String> hyponyms(String word) {
        Set<String> hy = new HashSet<>();
        Set<Integer> intSet = GraphHelper.descendants(graph, pam.get(word));
        for (int i : intSet) {
            hy.addAll(map.get(i));
        }
        return hy;
    }
}





