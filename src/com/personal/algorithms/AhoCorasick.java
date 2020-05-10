package com.personal.algorithms;

import java.util.*;

/**
 * Although non-tested across widely varying inputs, this code
 * serves as an implementation of the Aho-Corasick algorithm
 * See the paper: http://cr.yp.to/bib/1975/aho.pdf
 *
 * @author Caleb I. Lucas
 */
public class AhoCorasick {
    private Trie trie;

    private static class Node {
        Node parent;
        Map<Character, Node> children;
        char charValue;
        int indexInTable;
        int suffixConnection;
        String word;
    }

    private static class Trie {
        int size = 0;
        final Node root;
        final List<Node> nodeIndexTable;

        Trie() {
            root = new Node();
            root.indexInTable = size;
            nodeIndexTable = new ArrayList<>();
            nodeIndexTable.add(root);
        }

        /**
         * Insert a keyword into the Trie
         * @param keyword - a sequence of characters indicating what we are searching for in a given text
         */
        private void insertKeyword(String keyword) {
            Node currNode = root;

            for(int i = 0; i < keyword.length(); i++) {
                char c = keyword.charAt(i);

                if(currNode.children == null) {
                    currNode.children = new HashMap<>();
                }

                Node childNode;

                if(currNode.children.get(c) == null) {
                    childNode = new Node();
                    childNode.charValue = c;
                    childNode.parent = currNode;
                    childNode.indexInTable = ++size;
                    currNode.children.put(c, childNode);
                    nodeIndexTable.add(childNode);
                } else {
                    childNode = currNode.children.get(c);
                }

                if(i + 1 == keyword.length()) childNode.word = keyword;

                currNode = childNode;
            }
        }

        /**
         * Build all the node's suffix connections using BFS and failure function (see published paper)
         */
        private void buildSuffixConnection() {
            Queue<Node> nodeQueue = new LinkedList<>();
            nodeQueue.add(root);

            while(nodeQueue.peek() != null) {
                Node node = nodeQueue.poll();

                setNodeSuffix(node);

                if(node.children != null) {
                    node.children.forEach((key, value) -> nodeQueue.add(value));
                }
            }
        }

        /**
         * The concept of this suffix-prefix connection calculator is similar to that of KMP.
         * The goal here, though, is to link a node to another node higher up the trie that signifies
         * the largest sequence of characters that is both a suffix (in source node) and a prefix in
         * the destination node
         * @param node - any Node in the Trie
         */
        private void setNodeSuffix(Node node) {
            // handle nodes at level 0 && level 1
            if(node == root || node.parent == root) {
                node.suffixConnection = 0;
                return;
            }

            // handle nodes at level 2 and above
            Node parent = node.parent;

            while(parent != root) {

                // does any of the children of the resulting node of the
                // parent's suffix connection contain our node's character
                Map<Character, Node> parentConnectionChildren = nodeIndexTable.get(parent.suffixConnection).children;
                if(parentConnectionChildren != null && parentConnectionChildren.get(node.charValue) != null) {
                    node.suffixConnection = parentConnectionChildren.get(node.charValue).indexInTable;
                    break;
                }

                // next parent will be the suffix connection of this node's parent
                parent = nodeIndexTable.get(parent.suffixConnection);
            }

            if(parent == root) node.suffixConnection = 0;
        }

        /**
         * Print all the nodes downwards of the Trie starting from the specified root
         * @param root - any random Node signifying the beginning of the tree
         */
        private void print(Node root) {
            if(root.children != null) {

                System.out.println(root.children.keySet());

                root.children.forEach((key, value) -> print(value));
            }
        }
    }

    private String text;

    private AhoCorasick(String text) {
        trie = new Trie();
        this.text = text;
    }

    private void insertKeywords(String[] keywords) {
        for (String keyword: keywords) {
            trie.insertKeyword(keyword);
        }
        trie.buildSuffixConnection();
    }

    private void findValidKeywords(String[] keywords) {
        this.insertKeywords(keywords);

        Node current = trie.root;

        for(int i = 0; i < text.length();) {
            char currChar = text.charAt(i);
            if(current.children != null && current.children.get(currChar) != null) {
                current = current.children.get(currChar);
                i++;
            } else if(current != trie.root) {
                current = trie.nodeIndexTable.get(current.suffixConnection);
            } else {
                i++;
            }
            if(current.word != null) {
                System.out.println(current.word);
            }
        }
    }

    private void printTrie() {
        trie.print(trie.root);
    }

    // Driver Function
    public static void main(String[] args) {
        AhoCorasick ahoCorasick = new AhoCorasick("ushers");
        ahoCorasick.findValidKeywords(new String[]{"he", "she", "his", "hers"});
    }
}