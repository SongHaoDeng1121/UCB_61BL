package main;

import ngrams.NGramMap;
import ngrams.TimeSeries;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import plotting.Plotter;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    private final NGramMap ngm;

    public HistoryHandler(NGramMap map) {
        this.ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        List<TimeSeries> tsList = new ArrayList<>();
        for (String word : words) {
            TimeSeries ts = ngm.weightHistory(word, startYear, endYear);
            tsList.add(ts);
        }

        var chart = Plotter.generateTimeSeriesChart(words, tsList);
        return Plotter.encodeChartAsString(chart);
    }
}
