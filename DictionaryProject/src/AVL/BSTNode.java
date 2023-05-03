/************************  BSTNode.java  **************************
 *             node of a generic binary search tree
 */
package AVL;
public class BSTNode<T extends Comparable<? super T>> {
    protected String el;
    protected BSTNode<T> left, right;
    public BSTNode() {
        left = right = null;
    }
    public BSTNode(String el) {
        this(el,null,null);
    }
    public BSTNode(String el, BSTNode<T> lt, BSTNode<T> rt) {
        this.el = el; left = lt; right = rt;
    }
}