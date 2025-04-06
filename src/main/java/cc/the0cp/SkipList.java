package cc.the0cp;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
        /**
         * forward list to store next node in different levels
         */
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

    private Node<K, V> createNode(K key, V value, int level){
        return new Node<>(key, value, level);
    }

    /**
     * Generate random level for nodes
     * @return level
     */
    private static int randomLevel(){
        int level = 1;
        Random rand = new Random();
        while(rand.nextBoolean()){
            level++;
        }
        return level;
    }

    /**
     * Get size of the skip list
     * @return node count
     */
    public int size(){
        return this.nodeCnt;
    }

    public synchronized boolean insert(K key, V value){
        Node<K, V> cur = this.header;
        ArrayList<Node<K, V>> update = new ArrayList<>(Collections.nCopies(MAX_LEVEL + 1, null));

        for(int i = this.curLevel; i >= 0; i--){
            while(cur.forward.get(i) != null && cur.forward.get(i).getKey().compareTo(key) < 0){
                cur = cur.forward.get(i);
            }
        }
        return false;
    }

    public static void main(String[] args){
        //System.out.printf("Hello and welcome!");

        for(int i = 1; i <= 5; i++){
            System.out.println("i = " + i);
        }
    }
}