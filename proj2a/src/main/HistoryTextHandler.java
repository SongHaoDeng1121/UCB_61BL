package main;

import com.google.gson.Gson;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap ngm;

    public HistoryTextHandler(NGramMap ngm) {
        this.ngm = ngm;
    }


    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        boolean weighted = q.k() == -1;

        if (words.isEmpty()) {
            return "[]";
        }

        TimeSeries result;

        if (words.size() == 1) {
            String word = words.get(0);
            result = weighted
                    ? ngm.weightHistory(word, startYear, endYear)
                    : ngm.countHistory(word, startYear, endYear);
        } else {
            if (weighted) {
                result = ngm.summedWeightHistory(words, startYear, endYear);
            } else {
                result = new TimeSeries();
                for (String word : words) {
                    TimeSeries single = ngm.countHistory(word, startYear, endYear);
                    result = result.plus(single);
                }
            }
        }

        List<List<Number>> data = new ArrayList<>();
        for (Integer year : result.years()) {
            List<Number> pair = new ArrayList<>();
            pair.add(year);
            pair.add(result.get(year));
            data.add(pair);
        }
        Gson gson = new Gson();
        return gson.toJson(data);


    }
}


