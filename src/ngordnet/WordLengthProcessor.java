package ngordnet;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        long totalChar = 0, totalWords = 0;
        for (String word : yearlyRecord.words()) {
            totalChar += word.length() * yearlyRecord.count(word);
        }
        for (Number i : yearlyRecord.counts()) {
            totalWords += i.longValue();
        }
        return (double) totalChar / totalWords;
    }
}
