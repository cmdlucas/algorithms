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

    private class Node {
        Node parent;
        Map<Character, Node> children;
        char charValue;
        int globalKeyWordIndex;
        int suffixConnection;
        String word;
    }

    private class Trie {
        final Node root;
        int size = 0;
        List<Node> nodeIndexTable;

        private Trie() {
            root = new Node();
            root.globalKeyWordIndex = size;
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

                if(currNode.children.get(c) == null) {
                    Node childNode = new Node();
                    childNode.charValue = c;
                    childNode.parent = currNode;
                    childNode.globalKeyWordIndex = ++size;
                    if(i + 1 == keyword.length()) childNode.word = keyword;
                    currNode.children.put(c, childNode);
                    nodeIndexTable.add(childNode);
                }

                currNode = currNode.children.get(c);
            }
        }

        /**
         * Print all the nodes downwards of the Trie starting from the specified root
         * @param root - any random Node signifying the beginning of the tree
         */
        private void print(Node root) {
            if(root.children != null) {

                System.out.println(root.children.keySet());

                for (Map.Entry child : root.children.entrySet()) {
                    print((Node) child.getValue());
                }
            }
        }

        /**
         * Build all the node's suffix connections using BFS and failure function (see published paper.)
         */
        private void buildSuffixConnection() {
            Queue<Node> nodeQueue = new LinkedList<>();
            nodeQueue.add(root);

            while(nodeQueue.peek() != null) {
                Node node = nodeQueue.poll();

                setNodeSuffix(node);

                if(node.children != null) {
                    for (Map.Entry child : node.children.entrySet()) {
                        nodeQueue.add((Node) child.getValue());
                    }
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
                Node parentConnection = nodeIndexTable.get(parent.suffixConnection).children.get(node.charValue);
                if(parentConnection != null) {
                    node.suffixConnection = parentConnection.globalKeyWordIndex;
                    break;
                }

                // next parent will be the suffix connection of this node's parent
                parent = nodeIndexTable.get(parent.suffixConnection);
            }

            if(parent == root) node.suffixConnection = 0;
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

    public static void main(String[] args) {
        AhoCorasick ahoCorasick = new AhoCorasick("ushers");
        ahoCorasick.findValidKeywords(new String[]{"he", "she", "his", "hers"});
    }
}