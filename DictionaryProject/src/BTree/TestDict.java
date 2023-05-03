package BTree;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestDict {
    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();
        DictionaryBTree dictionary = selectLoading();
        dictionary.counter();
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time: " + elapsedTime/1000000);
        Thread.sleep(500);
        int choice = selectMethod();
        initiate_Methods(dictionary, choice);

    }

    private static void initiate_Methods(DictionaryBTree dictionary, int choice) throws WordNotFoundException, InterruptedException, IOException, WordAlreadyExistsException {
        Scanner scan = new Scanner(System.in);
        if(choice == 1){
            System.out.print("add new word> ");
            String word = scan.nextLine();
            dictionary.addWord(word);
            System.out.printf("%s added successfully\n", word);
            Thread.sleep(1000);
            choice = selectMethod();
            initiate_Methods(dictionary, choice);
        }
        else if(choice == 2){
            System.out.print("remove word> ");
            String word = scan.nextLine();
            dictionary.deleteWord(word);
            Thread.sleep(1000);
            choice = selectMethod();
            initiate_Methods(dictionary, choice);
        }
        else if(choice == 3){
            System.out.print("search for similar words> ");
            String word = scan.nextLine();
            System.out.println(java.util.Arrays.toString(dictionary.findSimilar(word)));
            Thread.sleep(1000);
            choice = selectMethod(); 
            initiate_Methods(dictionary, choice);
        }
        else if(choice == 4){
            System.out.print("check word> ");
            String word = scan.nextLine();
            if(dictionary.findWord(word))
                System.out.println("word found in the dictionary.");
            else
            System.out.println("word not found");
            Thread.sleep(1000); 
            choice = selectMethod();
            initiate_Methods(dictionary, choice);
        }
        if(choice == 5){
            System.out.print("Enter filename (with the desired path)> ");
            String name = scan.nextLine();
            dictionary.saveToFile(name.replace("\\", "\\\\"));
            System.out.println("File saved succeffully in the path: " + name);
            Thread.sleep(1000);
            choice = selectMethod();
            initiate_Methods(dictionary, choice);
        }

    }

    public static DictionaryBTree selectLoading(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\nChoose 1 or 2 from below: ");
        System.out.println("\t1- Load dictionary from a file.");
        System.out.println("\t2- Load an empty dictionary.");
        System.out.print(">> ");
        int choice = scan.nextInt();
     
        if(choice == 1){
            System.out.print("\nEnter file path> ");
            scan = new Scanner(System.in);
            String path = scan.nextLine();
            try {   
                File file = new File(path.replace("\\", "\\\\"));
                DictionaryBTree dictionary = new DictionaryBTree(file);
                return dictionary;
            } catch (Exception e) {
                System.out.println(e);
            }
            
        }
        else if(choice == 2){
            DictionaryBTree dictionary = new DictionaryBTree();
            return dictionary;
        }
        else{
            System.out.println("Invalid selection, try again.");
            selectLoading();
        }

        return null;
        

        
    }

    public static int selectMethod() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nChoose one of the methods below: ");
        System.out.println("\t1- add new word.");
        System.out.println("\t2- remove word.");
        System.out.println("\t3- search for similar word.");
        System.out.println("\t4- find word.");
        System.out.println("\t5- save updated Dictionary");
        System.out.print(">> ");
        int choice = scan.nextInt();
        if(choice > 5 || choice < 0){
            System.out.println("invalid, try again");
            selectMethod();
        }
        

        return choice;
    }
}
