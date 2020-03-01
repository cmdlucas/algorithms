package com.personal.algorithms;

import java.util.Arrays;

/**
 * Knuth-Morris-Pratt substring search algorithm
 *
 * @author Caleb I. Lucas
 */
public class KMPMatch {
    public static void main(String[] args) {
        System.out.println(matches("abxabcabcaby", "abcaby"));
        System.out.println(matches("awesomeness", "some"));
    }

    private static boolean matches(String S, String P) {
        if(P.length() > S.length()) return false;

        int[] look = buildPrefixSuffixLookup(P);

        System.out.println(Arrays.toString(look));

        boolean f = false;

        for(int i = 0, j = 0; i < S.length() && j < P.length();)
        {
            if(S.charAt(i) == P.charAt(j)) {
                i++; j++; f = true;
            } else {
                if(j > 0) {
                    j =  look[j-1];
                } else {
                    i++;
                }
                f = false;
            }
        }

        return f;
    }

    private static int[] buildPrefixSuffixLookup(String P) {
        int[]  L = new int[P.length()];

        for(int i = 1, j = 0; i < P.length();) {
            if(P.charAt(i) == P.charAt(j)) {
                // set i to the value of j + 1, which indicates the last known prefix-suffix match
                L[i++] = ++j;
            } else {
                if(j > 0) {
                    // return j to the value stored at the previous cell
                    // (whose value could (not) have been dictated by a match)
                    // leave i as is so that the loop can do a rematch to get the value for this cell
                    j = L[j - 1];
                } else {
                    L[i++] = 0;
                }
            }
        }

        return L;
    }

    private static boolean naiveSearch(String S, String P) {
        if (P.length() > S.length()) return false;

        boolean f = false;
        for(int i = 0, j = 0; i < S.length() && j < P.length(); i++) {
            if(P.charAt(j) == S.charAt(i)) {
                j++; f = true;
            }
            else {
                j = 0; f = false;
            }
        }

        return f;
    }
}
