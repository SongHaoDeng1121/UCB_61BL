import java.util.*;

public class Graph {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        // Remove existing edge if it exists
        adjLists[v1].removeIf(edge -> edge.to == v2);
        // Add new edge with specified weight
        adjLists[v1].add(new Edge(v1, v2, weight));
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        // Add edge in both directions for undirected graph
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        // TODO: YOUR CODE HERE
        // Check if any edge in the adjacency list of 'from' goes to 'to'
        for (Edge edge : adjLists[from]) {
            if (edge.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        // TODO: YOUR CODE HERE
        List<Integer> neighborsList = new ArrayList<>();
        // Collect all destination vertices from adjacency list of v
        for (Edge edge : adjLists[v]) {
            neighborsList.add(edge.to);
        }
        return neighborsList;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        // TODO: YOUR CODE HERE
        int count = 0;
        // Count edges from all vertices that point to v
        for (int i = 0; i < vertexCount; i++) {
            for (Edge edge : adjLists[i]) {
                if (edge.to == v) {
                    count++;
                }
            }
        }
        return count;
    }

    /* Returns a list of the vertices that lie on the shortest path from start to stop. 
    If no such path exists, you should return an empty list. If START == STOP, returns a List with START. */
    public ArrayList<Integer> shortestPath(int start, int stop) {
        // TODO: YOUR CODE HERE
        // Handle edge case: start equals stop
        if (start == stop) {
            ArrayList<Integer> result = new ArrayList<>();
            result.add(start);
            return result;
        }

        // Use VertexDistance objects in priority queue for cleaner implementation
        PriorityQueue<VertexDistance> fringe = new PriorityQueue<>();

        // Track distances from start vertex to each vertex
        Map<Integer, Integer> distances = new HashMap<>();

        // Track predecessors for path reconstruction
        Map<Integer, Integer> predecessors = new HashMap<>();

        // Track visited vertices to avoid revisiting
        Set<Integer> visited = new HashSet<>();

        // Initialize: start vertex has distance 0
        distances.put(start, 0);
        fringe.add(new VertexDistance(start, 0));

        // Dijkstra's main loop
        while (!fringe.isEmpty()) {
            // Get vertex with minimum distance from start
            VertexDistance current = fringe.poll();
            int currentVertex = current.vertex;

            // Skip if already visited (optimization)
            if (visited.contains(currentVertex)) {
                continue;
            }

            // Mark current vertex as visited
            visited.add(currentVertex);

            // If we reached the destination, we can terminate early
            if (currentVertex == stop) {
                break;
            }

            // Examine all neighbors of current vertex
            for (Edge edge : adjLists[currentVertex]) {
                int neighbor = edge.to;

                // Skip already visited neighbors
                if (visited.contains(neighbor)) {
                    continue;
                }

                // Calculate new distance through current vertex
                int currentDistance = distances.get(currentVertex);
                int newDistance = currentDistance + edge.weight;

                // If this is the first time we've seen this neighbor, or we found a shorter path
                if (!distances.containsKey(neighbor) || newDistance < distances.get(neighbor)) {
                    // Update distance and predecessor
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, currentVertex);

                    // Add to fringe with updated distance
                    fringe.add(new VertexDistance(neighbor, newDistance));
                }
            }
        }

        // Reconstruct path from stop to start using predecessors
        ArrayList<Integer> path = new ArrayList<>();

        // If stop vertex was never reached, no path exists
        if (!predecessors.containsKey(stop) && stop != start) {
            return path; // Return empty list
        }

        // Build path backwards from stop to start
        int current = stop;
        while (current != start) {
            path.add(current);
            current = predecessors.get(current);
        }
        path.add(start);

        // Reverse to get path from start to stop
        Collections.reverse(path);

        return path;
    }

    private Edge getEdge(int v1, int v2) {
        // TODO: YOUR CODE HERE
        // Search through adjacency list of v1 for edge to v2
        for (Edge edge : adjLists[v1]) {
            if (edge.to == v2) {
                return edge;
            }
        }
        return null; // No edge found
    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

        public int to() {
            return this.to;
        }

    }

    /* Custom vertex wrapper class for the priority queue in Dijkstra's algorithm
     * This approach is cleaner than using a separate comparator */
    private class VertexDistance implements Comparable<VertexDistance> {
        int vertex;
        int distance;

        VertexDistance(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    
}