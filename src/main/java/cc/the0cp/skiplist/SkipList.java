package cc.the0cp.skiplist;

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
    private final Node<K, V> header;
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

    public int size(){
        return nodeCnt;
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
     * search and get node value
     * @param key node key to search
     * @return the value of key node
     */
    public V get(K key){
        Node<K, V> cur = this.header;

        for(int i = this.curLevel; i >= 0; i--){
            while(cur.forward.get(i) != null && cur.forward.get(i).getKey().compareTo(key) < 0){
                cur = cur.forward.get(i);
            }
        }

        cur = cur.forward.getFirst();

        if(cur != null && cur.getKey().compareTo(key) == 0){
            return cur.getValue();
        }

        return null;
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
            this.nodeCnt++;
            return true;
        }
        return false;
    }

    /**
     * delete node from skip list
     * @param key target node's key
     * @return true if successfully deleted
     */
    public synchronized boolean remove(K key){
        Node<K, V> cur = this.header;
        ArrayList<Node<K, V>> updateTable = new ArrayList<>(Collections.nCopies(MAX_LEVEL, null));

        for(int i = this.curLevel; i >= 0; i--){
            while(cur.forward.get(i) != null && cur.forward.get(i).getKey().compareTo(key) < 0){
                cur = cur.forward.get(i);
            }
            updateTable.set(i, cur);
        }

        cur = cur.forward.getFirst();

        if(cur != null && cur.getKey().compareTo(key) == 0){    // node found
            for(int i = 0; i < this.curLevel; i++){
                if(updateTable.get(i).forward.get(i) != cur){
                    break;
                }
                updateTable.get(i).forward.set(i, cur.forward.get(i));
            }
        }

        while(this.curLevel > 0 && this.header.forward.get(this.curLevel) == null){
            this.curLevel--;    // update the list level
        }

        this.nodeCnt--;
        return true;
    }

    /**
     * list current skip list
     */
    public void list(){
        System.out.println("Listing " + this.size() + " nodes...");
        for(int i = this.curLevel; i >= 0; i--){
            Node<K, V> node = this.header.forward.get(i);
            System.out.print("Level " + i + ": ");
            while(node != null){
                System.out.print(node.getKey() + " : " + node.getValue() + " ;");
                node = node.forward.get(i);
            }
            System.out.println();
        }
    }

    public void saveFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DATA_STORE))){
            Node<K, V> node = this.header.forward.getFirst();
            while(node != null){
                bufferedWriter.write(String.valueOf(node.getKey()) + ':' + node.getValue() + ';');
                bufferedWriter.newLine();
                node = node.forward.getFirst();
            }
        }catch(IOException error){
            throw new RuntimeException("Failed to save file: ", error);
        }
        System.out.println("Data saved!");
    }

    public void loadFile(){
        try(BufferedReader bufferedReader = new BufferedReader((new FileReader(DATA_STORE)))){
            String data;
            while((data = bufferedReader.readLine()) != null){
                Node<K, V> node = stringToNode(data);
                if(node != null) {
                    insert(node.getKey(), node.getValue());
                }
            }
        }catch(IOException error){
            throw new RuntimeException("Failed to load file: ", error);
        }
        System.out.println("Data loaded: " + this.size());
    }

    @SuppressWarnings("unchecked")
    public Node<K, V> stringToNode(String data){
        if(data == null || data.isEmpty()){
            return null;
        }
        if(data.contains(":") && data.contains(";")){
            String keyString = data.substring(0, data.indexOf(":"));
            K key = (K)keyString;
            String valueString = data.substring(data.indexOf(":") + 1, data.length() - 1);
            V value = (V)valueString;
            return new Node<>(key, value, 0);
        }
        return null;
    }
}