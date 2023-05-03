package BTree;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;

public class DictionaryBTree {

    private int M; // minimum degree
    private int wordsCounter = 0;
    private int repeatedWordsCounter = 0;
  
    // Node creation
    public class Node {
      int n; // current number of keys
      String key[] = new String[2 * M - 1];
      Node child[] = new Node[2 * M];
      boolean leaf = true; // will be false when not
  
      public int Find(String k) {
        for (int i = 0; i < this.n; i++) {
          if (this.key[i].equals(k)) {
            return i;
          }
        }
        return -1;
      };
    }
  
    public DictionaryBTree(int t) {
      M = t;
      root = new Node();
      root.n = 0;
      root.leaf = true;
    }

    public DictionaryBTree(){
      this(7);
  }

  public DictionaryBTree(String string) throws WordAlreadyExistsException{
    this(7);
      addWord(string);
  }

  public DictionaryBTree(File f){
    this(7);
      try {
          Scanner scan = new Scanner(f);
          while(scan.hasNextLine()){ // adding every word in the file to the b-tree
            String word = scan.nextLine();
              addWord(word);}
          scan.close();
      } catch (Exception e) {
          System.out.println(e);
      }
  }
      
  
    private Node root;
  
    // Search key
    private Node Search(Node x, String key) {
      int i = 0;
      if (x == null)
        return x;
      for (i = 0; i < x.n; i++) {
        if (x.key[i].compareTo(key) > 0) {
          break;
        }
        if (key.equals(x.key[i])) {
          return x; // return the matching key
        }
      }
      if (x.leaf) { // if there exists more children it will continue the search otherwise return null
        return null;
      } else {
        return Search(x.child[i], key);
      }
    }
  
    // Splitting the node
    private void Split(Node x, int pos, Node y) {
      Node z = new Node();
      z.leaf = y.leaf;
      z.n = M - 1;
      for (int j = 0; j < M - 1; j++) {
        z.key[j] = y.key[j + M];
      }
      if (!y.leaf) {
        for (int j = 0; j < M; j++) {
          z.child[j] = y.child[j + M];
        }
      }
      y.n = M - 1;
      for (int j = x.n; j >= pos + 1; j--) {
        x.child[j + 1] = x.child[j];
      }
      x.child[pos + 1] = z;
  
      for (int j = x.n - 1; j >= pos; j--) {
        x.key[j + 1] = x.key[j];
      }
      x.key[pos] = y.key[M - 1];
      x.n = x.n + 1;
    }
  
    // Inserting a value
    public void addWord(final String key) throws WordAlreadyExistsException{
      try {
        if(findWord(key)){ // if the key already exists in the b-tree
          throw new WordAlreadyExistsException();}
      } catch (Exception e) {
        repeatedWordsCounter++;
        System.out.printf("%10s\talready exists, it will not be added again\n", key);
        return; // return to continue adding other words 
      }
      
      Node r = root;
      if (r.n == 2 * M - 1) {
        Node s = new Node();
        root = s;
        s.leaf = false;
        s.n = 0;
        s.child[0] = r;
        Split(s, 0, r);
        insertValue(s, key);
      } else {
        insertValue(r, key);
      }
      wordsCounter++;
    }
  
    // addWord to the node
    final private void insertValue(Node x, String k) {
  
      if (x.leaf) {
        int i = 0;
        for (i = x.n - 1; i >= 0 && k.compareTo(x.key[i]) < 0; i--) {
          x.key[i + 1] = x.key[i];
        }
        x.key[i + 1] = k;
        x.n = x.n + 1;
      } else {
        int i = 0;
        for (i = x.n - 1; i >= 0 && k.compareTo(x.key[i]) < 0; i--) {
        }
        ;
        i++;
        Node tmp = x.child[i];
        if (tmp.n == 2 * M - 1) {
          Split(x, i, tmp);
          if (k.compareTo(x.key[i]) > 0) {
            i++;
          }
        }
        insertValue(x.child[i], k);
      }
  
    }
  
    public void print() {
      print(root);
    }
  
    // print all the words in the b-tree
    private void print(Node x) {
      if(x == null)
        return;
      for (int i = 0; i < x.n; i++) {
        System.out.print(x.key[i] + " ");
      }
      if (!x.leaf) {
        for (int i = 0; i < x.n + 1; i++) {
          print(x.child[i]);
        }
      }
    }
  
    // Check if present
    public boolean findWord(String k) {
      if (this.Search(root, k) != null) {
        return true;
      } else {
        return false;
      }
    }


    private void Remove(Node x, String key) {
      int pos = x.Find(key);
      if (pos != -1) {
        if (x.leaf) {
          int i = 0;
          for (i = 0; i < x.n && !x.key[i].equals(key); i++) {
          }
          ;
          for (; i < x.n; i++) {
            if (i != 2 * M - 2) {
              x.key[i] = x.key[i + 1];
            }
          }
          x.n--;
          return;
        }
        if (!x.leaf) {
  
          Node pred = x.child[pos];
          String predKey = "";
          if (pred.n >= M) {
            for (;;) {
              if (pred.leaf) {
                System.out.println(pred.n);
                predKey = pred.key[pred.n - 1];
                break;
              } else {
                pred = pred.child[pred.n];
              }
            }
            Remove(pred, predKey);
            x.key[pos] = predKey;
            return;
          }
  
          Node nextNode = x.child[pos + 1];
          if (nextNode.n >= M) {
            String nextKey = nextNode.key[0];
            if (!nextNode.leaf) {
              nextNode = nextNode.child[0];
              for (;;) {
                if (nextNode.leaf) {
                  nextKey = nextNode.key[nextNode.n - 1];
                  break;
                } else {
                  nextNode = nextNode.child[nextNode.n];
                }
              }
            }
            Remove(nextNode, nextKey);
            x.key[pos] = nextKey;
            return;
          }
  
          int temp = pred.n + 1;
          pred.key[pred.n++] = x.key[pos];
          for (int i = 0, j = pred.n; i < nextNode.n; i++) {
            pred.key[j++] = nextNode.key[i];
            pred.n++;
          }
          for (int i = 0; i < nextNode.n + 1; i++) {
            pred.child[temp++] = nextNode.child[i];
          }
  
          x.child[pos] = pred;
          for (int i = pos; i < x.n; i++) {
            if (i != 2 * M - 2) {
              x.key[i] = x.key[i + 1];
            }
          }
          for (int i = pos + 1; i < x.n + 1; i++) {
            if (i != 2 * M - 1) {
              x.child[i] = x.child[i + 1];
            }
          }
          x.n--;
          if (x.n == 0) {
            if (x == root) {
              root = x.child[0];
            }
            x = x.child[0];
          }
          Remove(pred, key);
          return;
        }
      } else {
        for (pos = 0; pos < x.n; pos++) {
          if (x.key[pos].compareTo(key) > 0) {
            break;
          }
        }
        Node tmp = x.child[pos];
        if (tmp.n >= M) {
          Remove(tmp, key);
          return;
        }
        if (true) {
          Node nb = null;
          String devider = "-";
  
          if (pos != x.n && x.child[pos + 1].n >= M) {
            devider = x.key[pos];
            nb = x.child[pos + 1];
            x.key[pos] = nb.key[0];
            tmp.key[tmp.n++] = devider;
            tmp.child[tmp.n] = nb.child[0];
            for (int i = 1; i < nb.n; i++) {
              nb.key[i - 1] = nb.key[i];
            }
            for (int i = 1; i <= nb.n; i++) {
              nb.child[i - 1] = nb.child[i];
            }
            nb.n--;
            Remove(tmp, key);
            return;
          } else if (pos != 0 && x.child[pos - 1].n >= M) {
  
            devider = x.key[pos - 1];
            nb = x.child[pos - 1];
            x.key[pos - 1] = nb.key[nb.n - 1];
            Node child = nb.child[nb.n];
            nb.n--;
  
            for (int i = tmp.n; i > 0; i--) {
              tmp.key[i] = tmp.key[i - 1];
            }
            tmp.key[0] = devider;
            for (int i = tmp.n + 1; i > 0; i--) {
              tmp.child[i] = tmp.child[i - 1];
            }
            tmp.child[0] = child;
            tmp.n++;
            Remove(tmp, key);
            return;
          } else {
            Node lt = null;
            Node rt = null;
            boolean last = false;
            if (pos != x.n) {
              devider = x.key[pos];
              lt = x.child[pos];
              rt = x.child[pos + 1];
            } else {
              devider = x.key[pos - 1];
              rt = x.child[pos];
              lt = x.child[pos - 1];
              last = true;
              pos--;
            }
            for (int i = pos; i < x.n - 1; i++) {
              x.key[i] = x.key[i + 1];
            }
            for (int i = pos + 1; i < x.n; i++) {
              x.child[i] = x.child[i + 1];
            }
            x.n--;
            lt.key[lt.n++] = devider;
  
            for (int i = 0, j = lt.n; i < rt.n + 1; i++, j++) {
              if (i < rt.n) {
                lt.key[j] = rt.key[i];
              }
              lt.child[j] = rt.child[i];
            }
            lt.n += rt.n;
            if (x.n == 0) {
              if (x == root) {
                root = x.child[0];
              }
              x = x.child[0];
            }
            Remove(lt, key);
            return;
          }
        }
      }
    }
  
    public void deleteWord(String key) throws WordNotFoundException{
      Node x = Search(root, key);
      if (x == null) {
        throw new WordNotFoundException();
      }
      Remove(root, key);
      System.out.println("word deleted successfully");
    }
  

    public String[] findSimilar(String string){
      String s = SearchSimilar(root, string, "");
      //System.out.println(s);
      return s.split(" ");
    }
    

    private String SearchSimilar(Node x, String key, String sum) {
      int i = 0;
      int difference = 0;
      int difference2 = 0;
      if (x == null)
        return sum;

      for (i = 0; i < x.n; i++) {
        if(Math.abs(key.length()-x.key[i].length()) <= 1){
          if(key.length() < x.key[i].length()){
             difference = lettersOrderDifference(key, x.key[i]);
             difference2 = lettersDifference(key, x.key[i]);
            
          }
          else{ 
            difference = lettersOrderDifference(x.key[i], key);
            difference2 = lettersDifference(key, x.key[i]);
          }
          if(difference <= 1 || difference2 == -1)
              if(!sum.contains(x.key[i]))
                sum += x.key[i] + " ";
        }
      }
      //System.out.println(sum);
      if (x.leaf) {
        return sum;
      } else {
        for (int j = 0; j < x.child.length; j++) {
          sum = SearchSimilar(x.child[j], key, sum);
        }
        return sum;
      }
    }

    private int lettersOrderDifference(String word, String matchWord) {
      int difference = 0;
      

      for (int i = 0; i < word.length(); i++) {
        if(matchWord.charAt(i) != word.charAt(i))
          difference++;
      }

      
      return difference;
  }

  private int lettersDifference(String word, String matchWord){
    int difference = 0;
    String tmp = "";

    for (int i = 0; i < word.length(); i++) {
      if(!matchWord.contains(word.charAt(i)+"")){
        difference++;
        tmp += word.charAt(i);
      }
    }
    if(tmp.length() == 1 && matchWord.equals(word.replaceAll(tmp, "")))
      difference = -1; // this works as returning a flag of a special case



    return difference;
  }

  public void saveToFile(String path) throws IOException{
    WriteFile file = new WriteFile(path, true);
    file.clearFile(); // if the file already exists, it will clear then rewrite it
    saveToFile(root, file);
  }

  private void saveToFile(Node x, WriteFile file) throws IOException {
    if(x == null)
      return;
    for (int i = 0; i < x.n; i++) {
      file.writeToFile(x.key[i]);
    }
    if (!x.leaf) {
      for (int i = 0; i < x.n + 1; i++) {
        saveToFile(x.child[i], file);
      }
    }
  }
  
  public void counter(){
    System.out.println("Number of repeated words: " + repeatedWordsCounter);
    System.out.println("number of words: " + wordsCounter);
  }
}