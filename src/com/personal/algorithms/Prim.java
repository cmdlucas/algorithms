package com.personal.algorithms;

import java.util.*;

public class Prim {

    private static class Vertex {
        private final int parent; // the vertex's parent's value
        private final int value; // the vertex itself
        private final int distanceFromParent;

        public Vertex(int value, int parent, int distanceFromParent) {
            this.value = value;
            this.parent = parent;
            this.distanceFromParent = distanceFromParent;
        }
    }

    private static int[] minimumSpanningForest(int[][] G) {

        // map each vertex to the closest parent's edge weight
        int[] parentsDistance = new int[G.length];

        // initialize the distance to show that no distance is known
        Arrays.fill(parentsDistance, Integer.MAX_VALUE);

        // maps each vertex to the closest parent
        int[] parents = new int[G.length];

        // tell us which vertex has been visited
        boolean[] visited = new boolean[G.length];

        // This helps to construct a Forest rather than a single tree
        // i.e we're assured that disconnected components will be visited
        for(int i = 0; i < G.length; i++) {
            if(!visited[i]) {
                // store the vertices for selection in order of the closest parents
                Queue<Vertex> nextVertices = new PriorityQueue<>(Comparator.comparingInt(v -> v.distanceFromParent));
                nextVertices.add(new Vertex(i, i, 0));
                parents[i] = 0;

                while(nextVertices.peek() != null) {
                    Vertex vertex = nextVertices.poll();
                    visited[vertex.value] = true;

                    // update parent connection only on first update
                    // or when we have discovered a much closer parent
                    if(vertex.distanceFromParent <= parentsDistance[vertex.value]) {
                        parents[vertex.value] = vertex.parent;
                    }

                    for(int j = 0; j < G.length; j++) {
                        // proceed if there's a connection
                        if(G[vertex.value][j] != -1) {
                            // cache the closer parent's value so that if we ever visit this
                            // vertex more than once, we get to register it's closest parent
                            parentsDistance[j] = Math.min(parentsDistance[j], G[vertex.value][j]);
                            // prevent any cycles
                            if(!visited[j]) {
                                nextVertices.add(new Vertex(j, vertex.value, parentsDistance[j]));
                            }
                        }
                    }
                }
            }
        }

        System.out.println(Arrays.toString(parentsDistance));

        return parents;
    }

    // Driver Function
    public static void main(String[] args) {
        int[][] g = {
                { -1, 2, -1, 6, -1 },
                { 2, -1, 3, 8, 5 },
                { -1, 3, -1, -1, 7 },
                { 6, 8, -1, -1, 9 },
                { -1, 5, 7, 9, -1 }
        };

        System.out.println(Arrays.toString(minimumSpanningForest(g)));
    }
}
