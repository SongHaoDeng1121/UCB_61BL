import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

/* A mutable and finite Graph object. Edge labels are stored via a HashMap
   where labels are mapped to a key calculated by the following. The graph is
   undirected (whenever an Edge is added, the dual Edge is also added). Vertices
   are numbered starting from 0. */
public class Graph {

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
    /* Maps vertices to a list of its connected edges. */
    private HashMap<Integer, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();

    /* Returns the vertices that neighbor V. */
    public TreeSet<Integer> getNeighbors(int v) {
        return new TreeSet<Integer>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    public TreeSet<Edge> getEdges(int v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    public TreeSet<Integer> getAllVertices() {
        return new TreeSet<Integer>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(Integer v) {
        if (neighbors.get(v) == null) {
            neighbors.put(v, new HashSet<Integer>());
            edges.put(v, new HashSet<Edge>());
        }
    }

    /* Adds Edge E to the graph. */
    public void addEdge(Edge e) {
        addEdgeHelper(e.getSource(), e.getDest(), e.getWeight());
    }

    /* Creates an Edge between V1 and V2 with no weight. */
    public void addEdge(int v1, int v2) {
        addEdgeHelper(v1, v2, 0);
    }

    /* Creates an Edge between V1 and V2 with weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        addEdgeHelper(v1, v2, weight);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(int v1, int v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(int v) {
        return neighbors.get(v) != null;
    }

    /* Returns true if the graph contains the edge E. */
    public boolean containsEdge(Edge e) {
        return allEdges.contains(e);
    }

    /* Returns if this graph spans G. */
    public boolean spans(Graph g) {
        TreeSet<Integer> all = getAllVertices();
        if (all.size() != g.getAllVertices().size()) {
            return false;
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> vertices = new ArrayDeque<>();
        Integer curr;

        vertices.add(all.first());
        while ((curr = vertices.poll()) != null) {
            if (!visited.contains(curr)) {
                visited.add(curr);
                for (int n : getNeighbors(curr)) {
                    vertices.add(n);
                }
            }
        }
        return visited.size() == g.getAllVertices().size();
    }

    /* Overrides objects equals method. */
    public boolean equals(Object o) {
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        return neighbors.equals(other.neighbors) && edges.equals(other.edges);
    }

    /* A helper function that adds a new edge from V1 to V2 with WEIGHT as the
       label. */
    private void addEdgeHelper(int v1, int v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2, weight);
        Edge e2 = new Edge(v2, v1, weight);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }

    public Graph prims(int start) {
        // If start vertex doesn't exist, return null
        if (!containsVertex(start)) {
            return null;
        }

        Graph mst = new Graph(); // Resulting MST graph
        HashSet<Integer> inTree = new HashSet<>(); // Tracks vertices already in MST
        HashMap<Integer, Edge> distFromTree = new HashMap<>(); // Minimum connecting edge for each vertex
        PriorityQueue<Integer> fringe = new PriorityQueue<>(new PrimVertexComparator(distFromTree));

        // Initialize all vertices
        for (int v : getAllVertices()) {
            if (v == start) continue;
            distFromTree.put(v, new Edge(-1, v, Integer.MAX_VALUE)); // placeholder edge
        }

        inTree.add(start);
        for (Edge e : getEdges(start)) {
            int neighbor = e.getDest();
            distFromTree.put(neighbor, e);
            fringe.add(neighbor);
        }

        // Add the start vertex to MST
        mst.addVertex(start);

        while (!fringe.isEmpty()) {
            int curr = fringe.poll();
            Edge edge = distFromTree.get(curr);

            // If the destination vertex is already in MST, skip
            if (inTree.contains(curr)) continue;

            // Add vertex and edge to MST
            mst.addVertex(curr);
            mst.addEdge(edge);
            inTree.add(curr);

            // For each neighbor, update if we found a smaller connecting edge
            for (Edge e : getEdges(curr)) {
                int neighbor = e.getDest();
                if (!inTree.contains(neighbor)) {
                    Edge currentMin = distFromTree.get(neighbor);
                    if (currentMin == null || e.compareTo(currentMin) < 0) {
                        distFromTree.put(neighbor, e);
                        fringe.remove(neighbor); // re-insert to update priority
                        fringe.add(neighbor);
                    }
                }
            }
        }

        // Check if all vertices are in MST (i.e., graph is connected)
        if (mst.getAllVertices().size() != getAllVertices().size()) {
            return null; // not connected
        }

        return mst;
    }


    public Graph kruskals() {
        Graph mst = new Graph();
        WeightedQuickUnionUF dsu = new WeightedQuickUnionUF(getAllVertices().size());

        // Add all vertices to MST graph
        for (int v : getAllVertices()) {
            mst.addVertex(v);
        }

        int mstEdgeCount = 0;

        for (Edge e : allEdges) {
            int u = e.getSource();
            int v = e.getDest();

            // If u and v are not connected (in different sets), add the edge
            if (dsu.find(u) != dsu.find(v)) {
                dsu.union(u, v);
                mst.addEdge(e);
                mstEdgeCount++;

                // Once we have V - 1 edges, MST is complete
                if (mstEdgeCount == getAllVertices().size() - 1) {
                    return mst;
                }
            }
        }

        // Not all vertices are connected
        return null;
    }


    /* A comparator to help you compare vertices in terms of
     * how close they are to the current MST.
     * Feel free to uncomment the below code if you'd like to use it;
     * otherwise, you may implement your own comparator.
     */
    private class PrimVertexComparator implements Comparator<Integer> {
        private HashMap<Integer, Edge> distFromTree;

        public PrimVertexComparator(HashMap<Integer, Edge> distFromTree) {
            this.distFromTree = distFromTree;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            int edgeCompRes = distFromTree.get(o1).compareTo(distFromTree.get(o2));
            if (edgeCompRes == 0) {
                return o1 - o2;
            }
            return edgeCompRes;
        }
    }
}
