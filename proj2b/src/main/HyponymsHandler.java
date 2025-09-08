package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wordNet;
    private final NGramMap ngm;

    public HyponymsHandler(WordNet wordNet, NGramMap ngm) {
        this.wordNet = wordNet;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        Set<String> queryWords = new HashSet<>(q.words());
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();

        Set<String> hyponyms = wordNet.hyponyms(queryWords);

        if (k == 0) {
            List<String> sortedHyponyms = new ArrayList<>(hyponyms);
            Collections.sort(sortedHyponyms);
            return sortedHyponyms.toString();
        } else {
            Map<String, Double> wordCounts = new HashMap<>();
            for (String word : hyponyms) {
                TimeSeries ts = ngm.countHistory(word, startYear, endYear);
                double totalCount = 0.0;
                for (int year = startYear; year <= endYear; year++) {
                    totalCount += ts.getOrDefault(year, 0.0);
                }
                if (totalCount > 0) {
                    wordCounts.put(word, totalCount);
                }
            }

            List<String> sortedByCount = new ArrayList<>(wordCounts.keySet());
            sortedByCount.sort((a, b) -> {
                int cmp = Double.compare(wordCounts.get(b), wordCounts.get(a));
                if (cmp != 0) {
                    return cmp;
                }
                return a.compareTo(b);
            });

            if (k < sortedByCount.size()) {
                sortedByCount = sortedByCount.subList(0, k);
            }

            return sortedByCount.toString();
        }
    }
}
