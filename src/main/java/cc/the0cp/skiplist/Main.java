package cc.the0cp.skiplist;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        SkipList<String, String> skiplist = new SkipList<>();
        Scanner scanner = new Scanner(System.in);
        skiplist.loadFile();
        boolean loop = true;
        while(loop){
            String cmd = scanner.nextLine();
            String[] cmdList = cmd.split(" ");
            switch (cmdList[0]) {
                case "insert" -> {
                    boolean status = skiplist.insert(cmdList[1], cmdList[2]);
                    if(status){
                        System.out.println("Insert data: [" + cmdList[1] + "," + cmdList[2] + "] successfully!");
                    }else{
                        System.out.println("Failed to insert data: [" + cmdList[1] + "," + cmdList[2] + "]");
                    }
                }
                case "delete" -> {
                    boolean status = skiplist.remove(cmdList[1]);
                    if(status){
                        System.out.println("[" + cmdList[1] + "] deleted successfully!");
                    }else{
                        System.out.println("Failed to delete data!");
                    }
                }
                case "search" -> {
                    String value = skiplist.get(cmdList[1]);
                    if(value != null){
                        System.out.println("Key " + cmdList[1] + " found: " + value);
                    }else{
                        System.out.println("Key " + cmdList[1] + " not exists!");
                    }
                }
                case "exit" -> {
                    Scanner scan = new Scanner(System.in);
                    System.out.print("Save file? [Y/n]: ");
                    String yn = scan.next();
                    if(yn.equals("y") || yn.equals("Y")){
                        skiplist.saveFile();
                    }else if(yn.equals("n") || yn.equals("N")){
                        break;
                    }else{
                        skiplist.saveFile();
                    }
                    loop = false;
                }
                case "list" -> skiplist.list();
                case "save" -> skiplist.saveFile();
                case "load" -> skiplist.loadFile();
                default -> System.out.println("Command not found");
            }
        }
    }
}
