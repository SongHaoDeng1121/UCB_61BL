package main;

import edu.princeton.cs.algs4.In;




import java.util.*;

public class WordNet {
    private Map<Integer, String[]> idToSynset;
    private Map<String, Set<Integer>> wordToIds;
    private Map<Integer, Set<Integer>> graph;

    public WordNet(String synsetsFile, String hyponymsFile) {
        idToSynset = new HashMap<>();
        wordToIds = new HashMap<>();
        graph = new HashMap<>();

        parseSynsets(synsetsFile);
        parseHyponyms(hyponymsFile);
    }

    private void parseSynsets(String synsetsFile) {
        In in = new In(synsetsFile);
        while (in.hasNextLine()) {
            String[] parts = in.readLine().split(",");
            int id = Integer.parseInt(parts[0]);
            String[] synsetWords = parts[1].split(" ");

            idToSynset.put(id, synsetWords);
            for (String word : synsetWords) {
                wordToIds.computeIfAbsent(word, k -> new HashSet<>()).add(id);
            }
        }
    }

    private void parseHyponyms(String hyponymsFile) {
        In in = new In(hyponymsFile);
        while (in.hasNextLine()) {
            String[] parts = in.readLine().split(",");
            int source = Integer.parseInt(parts[0]);
            graph.putIfAbsent(source, new HashSet<>());

            for (int i = 1; i < parts.length; i++) {
                int target = Integer.parseInt(parts[i]);
                graph.get(source).add(target);
            }
        }
    }

    public Set<String> hyponyms(String word) {
        Set<Integer> synsetIds = wordToIds.getOrDefault(word, new HashSet<>());
        Set<Integer> descendants = GraphHelper.descendants(graph, synsetIds);

        Set<String> result = new HashSet<>();
        for (int id : descendants) {
            String[] synset = idToSynset.get(id);
            result.addAll(Arrays.asList(synset));
        }
        return result;
    }

    public Set<String> hyponyms(Set<String> words) {
        if (words.isEmpty()) {
            return Collections.emptySet();
        }

        Iterator<String> iter = words.iterator();
        Set<String> result = new HashSet<>(hyponyms(iter.next()));

        while (iter.hasNext()) {
            Set<String> nextHyponyms = hyponyms(iter.next());
            result.retainAll(nextHyponyms);
        }
        return result;
    }



}
