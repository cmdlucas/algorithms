package com.personal.algorithms;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This code implements connected components labelling as described
 * here: https://en.wikipedia.org/wiki/Connected-component_labeling
 *
 * For a clearer understanding on the algorithm implemented, see:
 * https://docs.google.com/file/d/0B8gQ5d6E54ZDM204VFVxMkNtYjg/edit
 *
 * Our goal is to count the number of connected components in a 2D-array
 *
 * @author Caleb I. Lucas
 */
public class ConnectedComponentsLabelling {

    // 4-connected neighborhood (horizontal and vertical; non-diagonal connection)
    private static int[] dirX = {0, -1, 0, 1};
    private static int[] dirY = {-1, 0, 1, 0};

    private static int countComponents(char[][] A) {
        int count = 0;
        Queue<int[]> queue = new LinkedList<>();

        for(int i = 0; i < A.length; i++) {
            for(int j = 0; j < A[i].length; j++) {
                char cell = A[i][j];
                if(cell == '1') {
                    count++;
                    queue.offer(new int[]{i, j});

                    while(queue.peek() != null) {
                        int[] vertex = queue.poll();

                        // label this vertex as visited
                        A[vertex[0]][vertex[1]] = '0';

                        // Go through the neighbours of this vertex and
                        // add them to the queue of vertices to be visited
                        for(int k = 0; k < 4; k++) {
                            int nY = vertex[0] + dirY[k];
                            int nX = vertex[1] + dirX[k];

                            // only add to neighbour queue if it qualifies
                            if(nY >= 0 && nX >= 0 && nY < A.length && nX < A[i].length && A[nY][nX] == '1') {
                                queue.offer(new int[]{nY, nX});
                            }
                        }
                    }

                }
            }
        }

        return count;
    }

    // Driver Function
    public static void main(String[] args) {
        char[][] binArray = {
                {'0', '1', '1', '0', '1'},
                {'0', '1', '1', '1', '1'},
                {'0', '0', '0', '0', '1'},
                {'1', '0', '0', '1', '1'}
        };

        char[][] binArray2 = {
                {'0', '1', '0', '0', '1'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '1'},
                {'0', '0', '1', '1', '0'},
                {'1', '0', '1', '1', '0'}
        };

        System.out.println(countComponents(binArray));
        System.out.println(countComponents(binArray2));
    }
}
