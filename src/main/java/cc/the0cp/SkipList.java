package cc.the0cp;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class SkipList<K extends Comparable<K>, V>{
    /**
     * Node to store data
     * @param <K>
     * @param <V>
     */
    public static class Node<K extends Comparable<K>, V>{
        K key;
        V value;
        int level;
        ArrayList<Node<K, V>> forward;

        Node(K key, V value, int level){
            this.key = key;
            this.value = value;
            this.level = level;
            this.forward = new ArrayList<>(Collections.nCopies(level + 1, null));
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public int getLevel() {
            return this.level;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * Max level of the skip list
     */
    private static final int MAX_LEVEL = 32;
    /**
     * Dir for data persistence
     */
    private static final String DATA_STORE = "./data";
    /**
     * header of the skip list
     */
    private Node<K, V> header;
    /**
     * current level of the skip list
     */
    private int curLevel;
    /**
     * Count of node in current skip list
     */
    private int nodeCnt;

    SkipList(){
        this.header = new Node<>(null, null, MAX_LEVEL);
        this.curLevel = 0;
        this.nodeCnt = 0;
    }

    public static void main(String[] args){
        //System.out.printf("Hello and welcome!");

        for(int i = 1; i <= 5; i++){
            System.out.println("i = " + i);
        }
    }
}