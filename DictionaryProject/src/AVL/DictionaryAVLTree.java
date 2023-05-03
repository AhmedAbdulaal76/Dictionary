package AVL;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DictionaryAVLTree<T extends Comparable<? super T>> extends BST<T> {
	
   protected int height; //The height of a specific node in a tree
   protected int wordsCounter = 0; //Hold the number of words in the dictionary
   protected int repeatedWordsCounter = 0;
	
	public DictionaryAVLTree() { //initializing the AVL Tree with empty node
		super();
		height = -1;
	}
	
	public DictionaryAVLTree(BSTNode<T> root) { //initializing the AVL Tree with a single node
		super(root);
		height = -1;
	}

   public DictionaryAVLTree(File file){ // adding every word in the file to the AVL-tree
      try {
         Scanner scan = new Scanner(file);
         while(scan.hasNextLine()){
           String word = scan.nextLine();
             addWord(word);}
         scan.close();
     } catch (Exception e) {
         System.out.println(e);
     }
   }
	
	public int getHeight() {
		return getHeight(root);
	} // return the height if the tree
	
	private int getHeight(BSTNode<T> node) { // return the height of specific node
      if(node == null) // if the node does node exist, return -1
         return -1;
      else       // the maximum height between the right or left subtree
         return 1 + Math.max(getHeight(node.left), getHeight(node.right));
   }
	
   private DictionaryAVLTree<T> getLeftAVL() { //return the left node
      DictionaryAVLTree<T> leftsubtree = new DictionaryAVLTree<T>(root.left);
      return leftsubtree;
   }

   private DictionaryAVLTree<T> getRightAVL() { // return the right node
      DictionaryAVLTree<T> rightsubtree = new DictionaryAVLTree<T>(root.right);
      return rightsubtree;
    }
    
	protected int getBalanceFactor() { // return the balance factor of the tree
      if(isEmpty()) // return zero if it is empty
         return 0;
      else       // return Right subtree height - Left subtree height
         return getRightAVL().getHeight() - getLeftAVL().getHeight();
    }
    
    public void addWord(String el)  {
      try {
         if(findWord(el)){ // if the key already exists in the AVL-tree
           throw new WordAlreadyExistsException();}
       } catch (Exception e) {
         repeatedWordsCounter++;
         System.out.printf("%10s\talready exists, it will not be added again\n", el);
         return; // return to continue adding other words 
       }
      super.insert(el);     //add the word
      this.balance();       //update the balance of the tree
      wordsCounter++; // increase the words counter by 1
    }
    
    public void deleteWord(String el) throws WordNotFoundException { //Removes a word from the dictionary
      if (!findWord(el)) {
         throw new WordNotFoundException();
       }
      super.deleteByCopying(el);    // delete the word
      balance();        //update the balance of the tree
      System.out.println("word deleted successfully");
    }
    
    protected void balance() // balance the AVL-tree
    {
      if(!isEmpty())
      {
         getLeftAVL().balance();
    	   getRightAVL().balance();

         adjustHeight();
        
         int balanceFactor = getBalanceFactor();
        
         if(balanceFactor == -2) {
			   //System.out.println("Balancing node with el: "+root.el);
            if(getLeftAVL().getBalanceFactor() < 0)
			      rotateRight();
            else
               rotateLeftRight();
         }
		
         else if(balanceFactor == 2) {
            //System.out.println("Balancing node with el: "+root.el);
            if(getRightAVL().getBalanceFactor() > 0)
               rotateLeft();
            else
               rotateRightLeft();
         }
      }
   }
    
   protected void adjustHeight() // update the height value
   {
      if(isEmpty())
         height = -1;
      else
         height = 1 + Math.max(getLeftAVL().getHeight(), getRightAVL().getHeight());   
   }
    
   protected void rotateRight() {
		//System.out.println("RIGHT ROTATION");

      BSTNode tempNode = root.right; // saving the right side on a variable temporarily
      // exchanging nodes to rotate
      root.right = root.left; 
      root.left = root.right.left;
      root.right.left = root.right.right;
      root.right.right = tempNode;

      // replacing the root with the new one
      String val = root.el;
      root.el = root.right.el;
      root.right.el = val;

      getRightAVL().adjustHeight();
      adjustHeight();
   }
    
   protected void rotateLeft() {
 	   //System.out.println("LEFT ROTATION");
      BSTNode<T> tempNode = root.left;
      root.left = root.right;
      root.right = root.left.right;
      root.left.right = root.left.left;
      root.left.left = tempNode;
            
      String val =  root.el;
      root.el = root.left.el;
      root.left.el = val;
            
      getLeftAVL().adjustHeight();
      adjustHeight();
	}
	
	protected void rotateLeftRight()
   {
      //System.out.println("Double Rotation...");

      getLeftAVL().rotateLeft(); // rotating the left subtree to the left
      getLeftAVL().adjustHeight();
      this.rotateRight(); // rotating the tree to the right 
      this.adjustHeight();
  }

   protected void rotateRightLeft()
   {
		//System.out.println("Double Rotation...");
      getRightAVL().rotateRight();
      getRightAVL().adjustHeight();
      this.rotateLeft();
      this.adjustHeight();
   }

   public boolean findWord(String s){
      return super.isInTree(s);
   } // Search for a word in the dictionary

   public String[] findSimilar(String string){ //Search for words that are similar to a given word
      String s = SearchSimilar(root, string, "");
      //System.out.println(s);
      return s.split(" ");
    }


    //A helper method for findSimilar to search for the in each node similar keys
    private String SearchSimilar(BSTNode x, String key, String sum) {
      int i = 0;
      int difference = 0; // for letters Order Difference
      int difference2 = 0; // for letters Difference
      if (x == null)
        return sum;

        Queue<BSTNode<T>> queue = new Queue<BSTNode<T>>();
        if (x != null) {
             queue.enqueue(x);
             while (!queue.isEmpty()) { //looping until the queue is empty
                 x = queue.dequeue();
                 if(Math.abs(key.length()-x.el.length()) <= 1){ // compare the length of each node to the key
                  if(key.length() < x.el.length()){ // key length < node length
                     difference = lettersOrderDifference(key, x.el);
                     difference2 = lettersDifference(key, x.el);
                  
                  }
                  else{        // key length >= node length
                    difference = lettersOrderDifference(x.el, key);
                    difference2 = lettersDifference(key, x.el);
                  }
                  if(difference <= 1 || difference2 == -1)
                      if(!sum.contains(x.el))
                        sum += x.el + " ";
                }
                 if (x.left != null)
                      queue.enqueue(x.left); //adding the left child in the queue
                 if (x.right != null)
                      queue.enqueue(x.right); //adding the right child in the queue
             }
        }
      return sum;

      
    }


    //Calculates the letters’ order difference between the two words to check for similarity
    private int lettersOrderDifference(String word, String matchWord) {
      int difference = 0;
      

      for (int i = 0; i < word.length(); i++) {
        if(matchWord.charAt(i) != word.charAt(i))
          difference++;
      }

      
      return difference;
  }
    //Calculates the letters’ difference between the two words to check for similarity without caring about their order
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

  public void saveToFile(String path) throws IOException{ // save the dictionary to a specific file
   WriteFile file = new WriteFile(path, true);
   file.clearFile(); // if the file already exists, it will clear then rewrite it
   saveToFile(root, file); //Saving every element one by one
 }

 private void saveToFile(BSTNode x, WriteFile file) throws IOException {
   if(x == null)
     return;
     Queue<BSTNode<T>> queue = new Queue<BSTNode<T>>();
        if (x != null) {
             queue.enqueue(x);
             while (!queue.isEmpty()) {
                 x = queue.dequeue();
                 file.writeToFile(x.el);
                 if (x.left != null)
                      queue.enqueue(x.left);
                 if (x.right != null)
                      queue.enqueue(x.right);
             }
        }
 }
 
 public void counter(){ // print the number of words
   System.out.println("Number of repeated words: " + repeatedWordsCounter);
   System.out.println("Number of words added to the dictionary: " + wordsCounter);
 }
    
}