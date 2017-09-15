/* Starter code for NgordnetUI (part 7 of the project). Rename this file and 
   remove this comment. */

package ngordnet;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;

/**
 * Provides a simple user interface for exploring WordNet and NGram data.
 * 
 * @author Liren
 */
public class NgordnetUI {
    public static void main(String[] args) {
        // use ngordnetui_mini.config for a smaller dataset
        String config = "./src/ngordnet/ngordnetui_full.config";
        In in = new In(config);
        System.out.println("Reading " + config + " ...");

        String wordFile = in.readString();
        String countFile = in.readString(); 
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + "."
        + "\ntype help for commands.");
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        //System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");
        
        int startYear = 1000, endYear = 2050;
        while (true) {
        System.out.print("> ");
        String line = StdIn.readLine();
        String[] rawTokens = line.split(" ");
        String command = rawTokens[0];
        String[] tokens = new String[rawTokens.length - 1];
        System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            try {
                switch (command) {
                case "quit":
                    if (tokens.length != 0) throw new Exception();
                    return;
                case "help":
                    if (tokens.length != 0) throw new Exception();
                    In help = new In("./src/ngordnet/help.txt");
                    String str = help.readAll();
                    System.out.println(str);
                    break;
                case "range":
                    if (tokens.length != 2) throw new Exception();
                    startYear = Integer.parseInt(tokens[0]);
                    endYear = Integer.parseInt(tokens[1]);
                    System.out.println("Start year set to " + startYear + ", end year set to " + endYear);
                    break;
                case "count":
                    if (tokens.length != 2) throw new Exception();
                    System.out.println(ngm.countInYear(tokens[0], Integer.parseInt(tokens[1])));
                    break;
                case "hyponyms":
                    if (tokens.length != 1) throw new Exception();
                    System.out.println(wn.hyponyms(tokens[0]));
                    break;
                case "history":
                        Plotter.plotAllWords(ngm, tokens, startYear, endYear);
                        break;
                case "hypohist":
                    Plotter.plotCategoryWeights(ngm, wn, tokens, startYear, endYear);
                    break;
                case "wordlength":
                    if (tokens.length != 0) throw new Exception();
                    WordLengthProcessor wlp = new WordLengthProcessor();
                    Plotter.plotProcessedHistory(ngm, startYear, endYear, wlp);
                    break;
                case "zipf":
                    if (tokens.length != 1) throw new Exception();
                    Plotter.plotZipfsLaw(ngm, Integer.parseInt(tokens[0]));
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid command.");
            }
        }
    }
}
