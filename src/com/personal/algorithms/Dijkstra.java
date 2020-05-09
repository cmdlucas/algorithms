package com.personal.algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This is an implementation of Dijkstra's algorithm using a min-PriorityQueue
 * See: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 *
 * @author Caleb I. Lucas
 */
public class Dijkstra {

    public static class Vertex {
        int value;
        int distanceFromSource;

        public Vertex(int value, int distanceFromSource) {
            this.value = value;
            this.distanceFromSource = distanceFromSource;
        }
    }

    private static int[] graphDistances(int[][] g, int s) {
        int[] res = new int[g.length];

        // track visited nodes
        boolean[] visited = new boolean[g.length];

        // initialize array to a maximum value
        Arrays.fill(res, Integer.MAX_VALUE);

        /*
         * This Queue is especially helpful to determine the next min vertex to visit.
         * It helps us achieve some greedy optimization
         */
        Queue<Vertex> nextMinVertex = new PriorityQueue<>(Comparator.comparingInt(v -> v.distanceFromSource));
        nextMinVertex.add(new Vertex(s, 0));
        res[s] = 0;
        
        while(nextMinVertex.peek() != null) {
            Vertex vertex = nextMinVertex.poll();
            int[] adjVertices = g[vertex.value];
            visited[vertex.value] = true;

            // Explore the vertices that are connected to this vertex
            for(int i = 0; i < adjVertices.length; i++) {
                // i is a vertex
                if(adjVertices[i] != -1) {
                    // since this is a candidate vertex, store the smallest value seen thus far
                    res[i] = Math.min(res[i], vertex.distanceFromSource + adjVertices[i]);
                    // prevent cycles
                    if(!visited[i]) {
                        nextMinVertex.add(new Vertex(i, res[i]));
                    }
                }
            }
        }

        return Arrays.stream(res).map(r -> r == Integer.MAX_VALUE ? -1 : r).toArray();
    }

    // Driver Function
    public static void main(String[] args) {
        int[][] g = {
                {-1, 3, 2},
                {2, -1, 0},
                {-1, 0, -1}
        };
        int[] res = graphDistances(g, 0);

        System.out.println(Arrays.toString(res));
    }

}
