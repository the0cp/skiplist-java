package cc.the0cp;

import java.io.*;
import java.security.Key;
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
        return Math.min(level, MAX_LEVEL);
    }

    /**
     * Get size of the skip list
     * @return node count
     */
    public int size(){
        return this.nodeCnt;
    }

    /**
     * insert specific node
     * @param key   key of the node
     * @param value value of the node
     * @return  true if node is successfully inserted
     */
    public synchronized boolean insert(K key, V value){
        Node<K, V> cur = this.header;
        ArrayList<Node<K, V>> updateTable = new ArrayList<>(Collections.nCopies(MAX_LEVEL + 1, null));

        for(int i = this.curLevel; i >= 0; i--){    // get the last lower key
            while(cur.forward.get(i) != null && cur.forward.get(i).getKey().compareTo(key) < 0){
                cur = cur.forward.get(i);
            }
            updateTable.set(i, cur);
        }

        cur = cur.forward.getFirst();   // lowest level, get(0)

        if(cur != null && cur.getKey().compareTo(key) == 0){    // same key, replace value
            cur.setValue(value);
            return true;
        }

        int randLevel = randomLevel();

        if(cur == null || cur.getKey().compareTo(key) != 0){    // different key, insert node
            if(randLevel > curLevel){
                for(int i = curLevel + 1; i < randLevel + 1; i++){
                    updateTable.set(i, header);
                }
                curLevel = randLevel;
            }

            Node<K, V> insertNode = createNode(key, value, randLevel);

            for(int i = 0; i <= randLevel; i++){    // insert node and index
                insertNode.forward.set(i, updateTable.get(i).forward.get(i));
                updateTable.get(i).forward.set(i, insertNode);
            }
            nodeCnt++;
            return true;
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