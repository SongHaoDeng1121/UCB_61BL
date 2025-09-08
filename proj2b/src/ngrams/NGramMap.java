package ngrams;

import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private Map<String, TimeSeries> wordMap;
    private TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordMap = new HashMap<>();
        totalCounts = new TimeSeries();
        try (BufferedReader reader = new BufferedReader(new FileReader(wordsFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 3) {
                    continue;
                }

                String word = parts[0];
                int year = Integer.parseInt(parts[1]);
                double count = Double.parseDouble(parts[2]);

                wordMap.putIfAbsent(word, new TimeSeries());
                wordMap.get(word).put(year, count);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read words file", e);
        }

        // Load total counts
        try (BufferedReader reader = new BufferedReader(new FileReader(countsFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                int year = Integer.parseInt(parts[0]);
                double count = Double.parseDouble(parts[1]);

                totalCounts.put(year, count);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read counts file", e);
        }

    }



    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordMap.containsKey(word)) {
            return new TimeSeries();
        }

        startYear = Math.max(TimeSeries.MIN_YEAR, startYear);
        endYear = Math.min(TimeSeries.MAX_YEAR, endYear);

        if (startYear > endYear) {
            return new TimeSeries();
        }

        return new TimeSeries(wordMap.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordMap.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordMap.get(word));
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCounts);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (startYear > endYear) {
            return new TimeSeries();
        }

        startYear = Math.max(TimeSeries.MIN_YEAR, startYear);
        endYear = Math.min(TimeSeries.MAX_YEAR, endYear);

        if (startYear > endYear) {
            return new TimeSeries();
        }

        TimeSeries wordCounts = countHistory(word, startYear, endYear);
        TimeSeries totalCountsInRange = new TimeSeries(totalCounts, startYear, endYear);

        if (wordCounts.isEmpty()) {
            return new TimeSeries();
        }
        return wordCounts.dividedBy(totalCountsInRange);
    }



    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries wordCounts = countHistory(word);
        if (wordCounts.isEmpty()) {
            return new TimeSeries();
        }
        return wordCounts.dividedBy(totalCounts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        if (startYear > endYear) {
            return new TimeSeries();
        }
        startYear = Math.max(TimeSeries.MIN_YEAR, startYear);
        endYear = Math.min(TimeSeries.MAX_YEAR, endYear);

        if (startYear > endYear) {
            return new TimeSeries();
        }

        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeights = weightHistory(word, startYear, endYear);
            result = result.plus(wordWeights);
        }
        return result;
    }


    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeights = weightHistory(word);
            result = result.plus(wordWeights);
        }
        return result;
    }

}
