package com.personal.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generate all unique possible solutions of an n x n chess board where
 * each row and column have only one possible queen that cannot be attacked
 * horizontally, vertically or diagonally
 *
 * The solutions are unique in the sense that no symmetrical reflection or rotation is respected
 *
 * @author Caleb I. Lucas
 */
public class NQueensAllPossibleSolutions {

    int[][] Q;
    List<int[]> solutions;

    /**
     * Generate possible representations of the board
     * In the returned value, each row is a solution represented as placement of the queen on the board
     * i.e (index -> row; value -> column)
     * @param n - size of the 2D board
     * @return array
     */
    int[][] nQueens(int n) {
        Q = new int[n][n];
        solutions = new ArrayList<>();
        nQueensPermutation(0, new int[n]);
        int[][] res = new int[solutions.size()][];
        for(int i = 0; i < res.length; i++){
            res[i] = solutions.get(i);
        }
        return res;
    }

    /**
     * Generate the possible permutations
     * @param row - row on which we need to find a solution for
     * @param pos - maps each row to the column where queen should be placed
     * @return - boolean if row was solved
     */
    boolean nQueensPermutation(int row, int[] pos)
    {
        if(row >= Q.length) return true;

        for(int i = 0; i < Q.length; i++) {
            Q[row][i] = 1;
            int prevValueAtRow = pos[row];
            pos[row] = i + 1;
            if(row == 0 || insertQueenIntoBoard(row, i)) {
                if(nQueensPermutation(row + 1, pos)) {
                    solutions.add(Arrays.copyOf(pos, pos.length));
                }
            }
            Q[row][i] = 0;
            pos[row] = prevValueAtRow;
        }

        return false;
    }

    /**
     * Check if the cell can be filled
     * @param row - the row on the board
     * @param col - the col on the board
     * @return boolean
     */
    boolean insertQueenIntoBoard(int row, int col) {
        // queen is safe horizontally since other horizontally adjacent cells to the left
        // most definitely failed that's why we are on this cell

        // confirm that the queen is safe vertically
        for(int j = row - 1; j >= 0; j--){
            if(Q[j][col] == 1) {
                return false;
            }
        }

        // confirm that the queen is safe top-left to cell
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if(Q[i][j] == 1) {
                return false;
            }
        }

        // confirm that the queen is safe top-right to cell
        for(int i = row - 1, j = col + 1; i >= 0 && j < Q.length; i--, j++) {
            if(Q[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        NQueensAllPossibleSolutions nQueensAllPossibleSolutions = new NQueensAllPossibleSolutions();
        System.out.println(Arrays.deepToString(nQueensAllPossibleSolutions.nQueens(3)));
        System.out.println(Arrays.deepToString(nQueensAllPossibleSolutions.nQueens(4)));
        System.out.println(Arrays.deepToString(nQueensAllPossibleSolutions.nQueens(5)));
    }
}
