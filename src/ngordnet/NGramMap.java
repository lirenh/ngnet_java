package ngordnet;

import java.util.Collection;
import java.util.TreeMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;

public class NGramMap {
    
    private TreeMap<Integer, YearlyRecord> recordMap = new TreeMap<>();
    private TimeSeries<Long> countHist = new TimeSeries<>();
    
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        Path wPath = Paths.get(wordsFilename);
        Path cPath = Paths.get(countsFilename);
        try (BufferedReader wReader = Files.newBufferedReader(wPath);
                BufferedReader cReader = Files.newBufferedReader(cPath)) {
            String wLine = null;
            while ((wLine = wReader.readLine()) != null) {
                String[] info = wLine.split("\t");
                String word = info[0];
                int year = Integer.parseInt(info[1]);
                int cnt = Integer.parseInt(info[2]);
                if (recordMap.containsKey(year)) {
                    recordMap.get(year).put(word, cnt);
                } else {
                    YearlyRecord yr = new YearlyRecord();
                    yr.put(word, cnt);
                    recordMap.put(year, yr);
                }
            }
            String sLine = null;
            while ((sLine = cReader.readLine()) != null) {
                String[] info = sLine.split(",");
                int year = Integer.parseInt(info[0]);
                long totalCnt = Long.parseLong(info[1]);
                countHist.put(year, totalCnt);
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (!recordMap.containsKey(year)) {
            return 0;
        }
        return recordMap.get(year).count(word);
    }

    /** Returns a defensive copy of the YearlyRecord of YEAR. */
    public YearlyRecord getRecord(int year) {
        if (!recordMap.containsKey(year)) {
            return null;
        }
        YearlyRecord yr = recordMap.get(year);
        return new YearlyRecord(yr.countMap);
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return countHist;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        return new TimeSeries<Integer>(countHistory(word), startYear, endYear);
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries<Integer> ts = new TimeSeries<>();
        for (int year : recordMap.keySet()) {
            ts.put(year, countInYear(word, year));
        }
        return ts;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        return new TimeSeries<Double>(weightHistory(word), startYear, endYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        return countHistory(word).dividedBy(countHist);
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        return new TimeSeries<Double>(summedWeightHistory(words), startYear, endYear);
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> ts = new TimeSeries<>();
        for (String w : words) {
            ts = ts.plus(weightHistory(w));
        }
        return ts;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp) {
        return new TimeSeries<Double>(processedHistory(yrp), startYear, endYear);
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> ts = new TimeSeries<>();
        for (int year : recordMap.keySet()) {
            ts.put(year, yrp.process(recordMap.get(year)));
        }
        return ts;
    }
}