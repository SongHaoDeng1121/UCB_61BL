package main;

import java.util.*;

public class GraphHelper {

    public static Set<Integer> descendants(Map<Integer, Set<Integer>> graph, Set<Integer> startNodes) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>(startNodes);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited.contains(node)) {
                visited.add(node);
                Set<Integer> neighbors = graph.getOrDefault(node, new HashSet<>());
                for (int neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return visited;
    }
}
