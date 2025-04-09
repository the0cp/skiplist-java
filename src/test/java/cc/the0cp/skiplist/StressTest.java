package cc.the0cp.skiplist;

import java.util.Random;

public class StressTest{
    public static final int INSERT_TIMES = 100000;
    public static final int SEARCH_TIMES = 100000;

    public static String randString(){
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        int len = 10;
        StringBuilder result = new StringBuilder(len);
        Random random = new Random();

        for(int i = 0; i < len; i++){
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    public static void main(String[] args) throws InterruptedException{
        int nThread = 10;
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[nThread];
        SkipList<String, String> skipList = new SkipList<>();

        for(int i = 0; i < nThread; i++){
            threads[i] = new Thread(new InsertTask<>(skipList));
            threads[i].start();
        }

        for(int i = 0; i < nThread; i++){
            threads[i].join();
        }

        long end = System.currentTimeMillis();
        System.out.println("Insert in " + nThread + " Thread " + (nThread * INSERT_TIMES) + " takes " + (end - start) + "ms");

        long start2 = System.currentTimeMillis();

        Thread[] threads2 = new Thread[nThread];
        for(int i = 0; i < nThread; i++){
            threads2[i] = new Thread(new SearchTask<>(skipList));
            threads2[i].start();
        }

        for(int i = 0; i < nThread; i++){
            threads2[i].join();
        }

        long end2 = System.currentTimeMillis();
        System.out.println("Search in " + nThread + " Thread " + (nThread * SEARCH_TIMES) + " takes " + (end2 - start2) + "ms");

    }

    private static class InsertTask<K extends Comparable<K>, V> implements Runnable{
        SkipList<K, V> skipList;
        InsertTask(SkipList<K, V> skipList){
            this.skipList = skipList;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void run(){
            for(int i = 0; i < INSERT_TIMES; i++){
                boolean status = this.skipList.insert((K)randString(), (V)randString());
                if(!status){
                    System.out.println("Failed to insert");
                    break;
                }
            }
        }
    }

    private static class SearchTask<K extends Comparable<K>, V> implements Runnable{
        SkipList<K, V> skipList;
        SearchTask(SkipList<K, V> skipList){
            this.skipList = skipList;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void run(){
            for(int i = 0; i < SEARCH_TIMES; i++){
                this.skipList.get((K)randString());
            }
        }
    }
}
