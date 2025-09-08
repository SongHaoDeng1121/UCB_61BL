package main;

import browser.NgordnetServer;
import demo.DummyHistoryHandler;
import demo.DummyHistoryTextHandler;
import ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        hns.startUp();

        WordNet wn = new WordNet("data/wordnet/synsets-EECS.txt", "data/wordnet/hyponyms-EECS.txt");
        NGramMap ngm = new NGramMap("data/ngrams/frequency-EECS.csv", "data/ngrams/total_counts.csv");

        hns.register("history", new DummyHistoryHandler());
        hns.register("historytext", new DummyHistoryTextHandler());

        hns.register("hyponyms", new HyponymsHandler(wn, ngm));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
