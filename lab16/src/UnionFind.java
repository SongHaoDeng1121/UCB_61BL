public class UnionFind {

    /* TODO: Add instance variables here. */
    private int[] parents;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        if (N < 0) {
            throw new IllegalArgumentException("Number of elements must be non-negative");
        }
        parents = new int[N];
        for (int i = 0; i < N; i++) {
            parents[i] = -1;
        }

    }



    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        int root = find(v);
        return -parents[root];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0 || v >= parents.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return parents[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (v1 < 0 || v1 >= parents.length || v2 < 0 || v2 >= parents.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0 || v >= parents.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        if (parents[v] < 0) {
            return v;
        }
        parents[v] = find(parents[v]);
        return parents[v];
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if (v1 < 0 || v1 >= parents.length || v2 < 0 || v2 >= parents.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2) {
            return;
        }

        int size1 = -parents[root1];
        int size2 = -parents[root2];
        if (size1 < size2) {
            parents[root1] = root2;
            parents[root2] = -(size1 + size2);
        } else if (size1 > size2) {
            parents[root2] = root1;
            parents[root1] = -(size1 + size2);
        } else {
            parents[root1] = root2;
            parents[root2] = -(size1 + size2);
        }

    }
}
