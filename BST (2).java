package project4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * An implementation of a binary search tree. 
 * The elements are ordered using their natural ordering.This implementation provides guaranteed O(H)
 * (H is the height of this tree which could be as low as logN for balanced trees, but could be as large as N for unbalanced trees) 
 * time cost for the basic operations (add, remove and contains).
 * This class implements many of the methods provided by the Java framework's TreeSet class.
 * @author Jacob Wang
 * @param <E>
 */
public class BST<E extends Comparable<E>> 
extends Object 
implements Iterable<E>, Cloneable{
	/**
	 * Node class for this BST which stores the height of the node
	 * data and index given by the iterator
	 */
	private class Node { 
		private E e;		//data stored in the node
		private int height;	//height of the node
		private int index;	//index of the node
		private Node left;	//left node
		private Node right;	//right node
		/**
		 * Constructor of the Node in the BST
		 * @param data
		 */
		public Node (E data) {
			this.e = data;
			this.left=null;
			this.height=0;
			this.index=0;
			this.right=null;
		}
	}
	private Node root;	//reference to the root node of the tree 
	private int size;	//size of the tree
	private boolean added;	//if the values are added
	private boolean found;	//if the values are founded
	private int toaa;		//int used to help the to array
	private int tempinorder=0;	//temporary integer stored inorder index
	private int temppreorder=0;	//temporary integer stored perorder index
	private int temppostorder=0;//temporary integer stored postorder index
	private ArrayList<E> inorder = new ArrayList<E>();	//ArrayList stored inorder traversal element of the tree
	private ArrayList<E> preorder = new ArrayList<E>();	//ArrayList stored preorder traversal element of the tree
	private ArrayList<E> postorder = new ArrayList<E>();//ArrayList stored postorder traversal element of the tree	
	/**
	 * Constructs a new, empty tree, 
	 * sorted according to the natural ordering of its elements. 
	 * All elements inserted into the tree must implement the Comparable interface.
	 * size equal to zero
	 * This operation should be O(1).
	 */
	public BST() {
		root = null; //create an empty tree
		size = 0;
	}
	/**
	 * Constructs a new tree containing the elements in the specified collection, 
	 * sorted according to the natural ordering of its elements.
	 * All elements inserted into the tree must implement the Comparable interface.
	 * This operation should be O(N logN) where N is the number of elements in the collection. 
	 * This implies, that the tree that is constructed has to have the high that is approximately logN, not N.
	 * @param collection - collection whose elements will comprise the new tree
	 * @throws NullPointerException - if the specified collection is null
	 */
	public BST(E[] collection) throws NullPointerException{
		if(collection.length<=0) {
			throw new NullPointerException(); // to see if the collection has element or not
		}
		else {
			size=collection.length; // the size of the tree must be equal to the size of the array
			root=quickBST(root,collection,0,collection.length-1); //use recursion method to help construct the tree
		}
		
	}
	/**
	 * Recursive method using quick sort method to construct the tree
	 * during partitioning the left part of the array will be stored in the left 
	 * subtree the right part of the array of the pivot will be stored in the right
	 * subtree
	 * @param a - the current node
	 * @param collection - the array need to be construct to the tree
	 * @param low - the lower index
	 * @param high - the higher index
	 * @return BST Node or null if all of the element added
	 */
	private Node quickBST(Node a,E[] collection,int low, int high) {
		if (low > high) //base case to stop the recursion method
			return null;
		else {
			int pivot = partition(collection, low, high); // get the index of the pivot from partitioning
			a = new Node(collection[pivot]); //set the current node's data equal to the pivot
			a.left = quickBST(a.left,collection, low, pivot - 1); //left subtree
			a.right = quickBST(a.right,collection, pivot + 1, high); // right subtree
			updateHeight(a); // update the height of each node
			return a; //returning the node
		}
	}
	/**
	 * private partition method to partition the array
	 * use the last index of the given array as the pivot
	 * and partitioning then put the pivot in the right places
	 * @param arr - array need to be partitioned
	 * @param left - left most index to processed
	 * @param right - right most index to processed
	 * @return pivot integer
	 */
	private int partition(E[] arr, int first, int last){
		E pivot = arr[last]; // set the last element of the array to pivot
		int i = (first - 1); // the left part of the partitioning 
		for(int j = first; j <= last - 1; j++){ //for loop used to do the partitioning
			if (arr[j].compareTo(pivot)<0){//if the element is in the wrong position swap it
				i++;
				swap(arr, i, j);
			}
			}
		swap(arr, i + 1, last); //swap the two element make the pivot in the right position of the array
		return (i + 1); //return the index of pivot
	}
	/**
	 * private swap method using to help 
	 * swap the two element in array
	 * @param arr - array to swap the element
	 * @param i - the index of element need to be swapped
	 * @param j - the index of element need to be swapped
	 */
	private void swap(E[] arr, int i, int j)
	{
	    E temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;
	}
	/**
	 * private method used to update the height
	 * the leaf node should be height 1
	 * and the parent node height update according to its child
	 * @param n - current node being processed
	 * @throws NullPointerException - if n is null
	 */
	private void updateHeight(Node n){
		if(n==null) { // check if n exist
			return;
		}
		if(n.left==null&n.right==null) // the leaf of the tree
			n.height=1; //height equal to 1
		else if(n.left==null) // if the left of the node is null
			n.height=n.right.height+1; // the height is just the right subtree plus one
		else if(n.right==null)// if the right of the node is null
			n.height=n.left.height+1; // the height is just the left subtree plus one
		else //if both the left subtree and right subtree are not null
			n.height = 1+ Math.max(n.left.height, n.right.height);// the height equal to the max height 
		//plus one of either left or right subtree
	}
	/**
	 * Adds the specified element to this set if it is not already present.
	 * More formally, adds the specified element e to this tree 
	 * if the set contains no element e2 such that Objects.equals(e, e2). 
	 * If this set already contains the element, 
	 * the call leaves the set unchanged and returns false.
	 * This operation should be O(H).
	 * @param e - element to be added to this set
	 * @return true if this set did not already contain the specified element
	 * @throws NullPointerException - 
	 * if the specified element is null and this set uses natural ordering, 
	 * or its comparator does not permit null elements
	 */
	public boolean add​(E e) throws NullPointerException{
		added = false;
		if(e==null) { // if e equals to null then throw exception
			throw new NullPointerException();
		}
		if(root==null) { // if root equals null
			root = new Node(e); // add element to the root
			updateHeight(root); //update the height
			size++;	//size enlarge by one
			return true; 
		}
		else {
			addbst(e,root); //use recursive method to add the element
			return added;
		}
	}
	/**
	 * private recursive method for helping to add element in specified location
	 * in a BST it changes the value of added to true if it is been added to the tree
	 * false if the element if the element is already in the tree
	 * @param x - element need to be added
	 * @param r - current element need to be added
	 */
	private void addbst (E x,Node r) { 
		if(r.e.compareTo(x)<0) { // current element stored in the node smaller than element need to be added
			if(r.right==null) { // if there is no element in the right of the current node
				size++;
				r.right=new Node(x);//add the element
				updateHeight(r.right);
				updateHeight(r);
				added = true; 
				return;
			}
			else
				addbst(x,r.right);//Returning the right node of the current node
		}
		if(r.e.compareTo(x)>0) { // current element stored in the node bigger than element need to be added
			if(r.left==null) { // if there is no element in the left of the current node
				size++;
				Node temp=new Node(x); //add the element
				updateHeight(temp);
				r.left=temp;
				updateHeight(r);
				added = true; 
				return;
			}
			else
				addbst(x,r.left); //Returning the left node of the current node
		}
		if(r.e.compareTo(x)==0) { //repeat element
			added=false;
			return;
		}
		updateHeight(r);
	}
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * This operation should be O(MH) where M is the size of the collection 
	 * and H is the height of the current tree.
	 * @param collection - collection containing elements to be added to this set
	 * @return true if this set changed as a result of the call
	 * @throws NullPointerException - if the specified collection is null or if any element of the collection is null
	 */
	public boolean addAll​(Collection<? extends E> collection) throws NullPointerException{
		if(collection==null||collection.size()==0) // if the collection is null or size is zero throw exception
			throw new NullPointerException();
		Iterator<? extends E> it = collection.iterator();// use iterator to access the collection
		int i=this.size();
		while(it.hasNext()) {
			add​(it.next());
		}
		
		return (i!=this.size());
	}
	/**
	 * Removes the specified element from this tree if it is present. 
	 * More formally, removes an element e such that Objects.equals(o, e), 
	 * if this tree contains such an element. 
	 * Returns true if this tree contained the element (or equivalently, 
	 * if this tree changed as a result of the call). 
	 * (This tree will not contain the element once the call returns.)
	 * This operation should be O(H).
	 * @param o - object to be removed from this set, if present
	 * @return true if this set contained the specified element
	 * @throws ClassCastException - if the specified object 
	 * cannot be compared with the elements currently in this tree
	 * @throws NullPointerException - if the specified element is null
	 */
	@SuppressWarnings("unchecked")
	public boolean remove​(Object o) throws NullPointerException,ClassCastException {
		found = false;
		if(o==null) {
			throw new NullPointerException();
		}
		try {
			this.root.e.compareTo((E) o); //try to catch the ClassCastException
		}
		catch(ClassCastException e){
			System.err.print("The type removing cannot be changed to the type already existed in tree");
		}
		E other = (E) o;
		if(root.e==o) { //see if the removing element is stored in the root
			found=true;
			if(root.left==null) { // if the left subtree of the root is null
				root=root.right; //the new tree will be set to it right sub tree
				updateHeight(root);
			}
			else if(root.right==null) { // if the right subtree of the root is null
				root=root.left; //the new tree will be set to it left sub tree
				updateHeight(root); //update height of the new tree
			}
			else if(root.left.right==null) { // if the left right subtree of the root is null
				root.left.right=root.right; //move the right subtree of the root to root.left.right
				root.right=null;
				root=root.left; //the new root
				updateHeight(root); //update height
			}
			else { // if the left right subtree of the root is not null
				Node temp=root.left.right; 
				Node curr=root.right;
				removeroot(temp,curr); //using recursive method to change the new root
				root.left.right=root.right; 
				root.right=null;
				root=root.left;// set the new root
				updateHeight(root);//update the height
			}	
			size--;//decrease the size
		}
		else {//the remove element is not the root
			removebst(other,root);//use recursive method to remove the element
		}
		return found;//return if the element is in the tree or not
		}
	/**
	 * recursive method used to found the given element and remove it
	 * and update the height of the tree after the given element has been
	 * removed if there is no element to removed found will be set to false
	 * update the height during the process of removing these element
	 * @param x - the element to be removed
	 * @param r - current node being processed
	 */
	private void removebst(E x,Node r) {
		if(r.e.compareTo(x)<0) { //current element smaller than given element
			if(r.right==null) { //go to the right subtree if it is null
				found = false; //return false
				return;
			}
			if(r.right.e.compareTo(x)==0) {// if it is equal to the element
				found=true; //return true
				size--;//decrease the size
				//to connect the child node after removing the node
				if(r.right.left==null&r.right.right==null) { 
					r.right=null;
					updateHeight(r.right);
				}	
				else if(r.right.left!=null&r.right.right==null) {
					r.right=r.right.left;
					updateHeight(r.right);
				}
				else if(r.right.left==null&r.right.right!=null) {
					r.right=r.right.right;
					updateHeight(r.right);
				}
				else if(r.right.left!=null&r.right.right!=null) {
					Node s=r.right.left;
					Node curr=r.right.right;
					change(s,curr);
					r.right=r.right.right;
					updateHeight(r.right);	
				}
			}
			else
				removebst(x,r.right);//doing recursion if the element is not equal to the given element
		}
		if(r.e.compareTo(x)>0) { //current element bigger than given element
			if(r.left==null) { //go to the left subtree if it is null
				found = false; //return false
				return;
			}
			if(r.left.e.compareTo(x)==0) {// if it is equal to the element
				found=true; //return true
				size--; //decrease the size
				//to connect the child node after removing the node
				if(r.left.left==null&r.left.right==null) {
					r.left=null;
					updateHeight(r.left);
				}	
				else if(r.left.right==null&r.left.left!=null) {
					r.left=r.left.left;
					updateHeight(r.left);
				}
				else if(r.left.left==null&r.left.right!=null) {
					r.left=r.left.right;
					updateHeight(r.left);
				}	
				else if(r.left.left!=null&r.left.right!=null) {
					Node s=r.left.left;
					Node curr=r.left.right;
					change(s,curr);
					r.left=r.left.right;
					updateHeight(r.left);	
				}
			}
			else
				removebst(x,r.left); //doing recursion if the element is not equal to the given element
		}   
		updateHeight(r);
	}
	/**
	 * private method used to hel
	 * @param s
	 * @param curr
	 */
	private void change(Node s,Node curr) {
		if(curr.left!=null)
			change(s,curr.left);
		if(curr.left==null) {
			curr.left=s;
		    updateHeight(curr);
		}
		updateHeight(curr);
	}
	/**
	 * private remove method if the removing method located at the root of the tree
	 * @param temp - root.left.right
	 * @param curr - current node being processed
	 */
	private void removeroot(Node temp,Node curr) {
		if(curr.left!=null) 
			removeroot(temp,curr.left);
		if(curr.left==null) {
			curr.left=temp;
			updateHeight(curr.left);
		}
		updateHeight(curr);
	}
	/**
	 * Removes all of the elements from this set. The set will be empty after this call returns.
	 * This operation should be O(1).
	 */
	public void clear() {
		this.root=null; //making the root to null
		this.size=0;
	}
	/**
	 * Returns true if this set contains the specified element. 
	 * More formally, returns true if and only 
	 * if this set contains an element e such that Objects.equals(o, e).
	 * This operation should be O(H).
	 * @param o - object to be checked for containment in this set
	 * @return true if this set contains the specified element
	 * @throws NullPointerException - if the specified element is null and this set uses natural ordering, 
	 * or its comparator does not permit null elements
	 * @throws ClassCastException - if the specified object cannot be compared with the elements currently in the set
	 */
	public boolean contains​(Object o) throws NullPointerException,ClassCastException {
		if(o==null) { //to see o is null or not 
			throw new NullPointerException();
		}
		@SuppressWarnings("unchecked")
		E other = (E) o;
		Node current = this.root;
		while(current != null) {
			if (current.e.compareTo(other)==0){ //compare to equal to zero
				return true; 
			} 
			else if(current.e.compareTo(other)>0) {//go left if the element is smaller than given element
				current = current.left; 
			}
			else if (current.e.compareTo(other)<0) {//go right if the element is bigger than given element
				current = current.right; 
			}	
		}
		return false;
	}
	/**
	 * Returns true if this collection contains all of the elements in the specified collection. 
	 * This implementation iterates over the specified collection, 
	 * checking each element returned by the iterator in turn to see if it's contained in this tree. 
	 * If all elements are so contained true is returned, otherwise false.
	 * This operation should be O(MH) where M is the size of the collection and H is the height of the current tree.
	 * @param c - collection to be checked for containment in this tree
	 * @return true if this tree contains all of the elements in the specified collection
	 * @throws NullPointerException - if the specified collection contains one or more null elements 
	 * and this collection does not permit null elements,
	 * or if the specified collection is null.
	 */
	public boolean containsAll​(Collection<?> c) throws NullPointerException{
		if(c==null)
			throw new NullPointerException();	
		boolean a=false;
		loop:for(Object o:c) {
			@SuppressWarnings("unchecked")
			E other = (E) o;
			a=contains​(other);//call on contains method
			if(!a) 
				break loop;//if one of the element is repeated return false and stop the loop
		}
		return a;
	}
	/**
	 * Returns the number of elements in this tree.
	 * This operation should be O(1).
	 * @return the number of elements in this tree
	 */
	public int size() {
		return size;
	}
	/**
	 * Returns true if this set contains no elements.
	 * This operation should be O(1).
	 * @return true if this set contains no elements
	 */
	public boolean isEmpty() {
		return(root==null);
	}
	/**
	 * Returns true if this tree is a full tree 
	 * (i.e., a binary tree in which each node has either two children or is a leaf).
	 * This operation should be O(N).
	 * @return true if this tree is full
	 */
	public boolean isFull() {
		return isFull(root);
	}
	/**
	 * recursive method to find if the tree is full or not
	 * @param - current node being processing
	 * @return true if this tree is full
	 */
	private boolean isFull(Node n) {
		if((n.left==null&n.right!=null)|(n.left!=null&n.right==null)) { //if the node has only one child then it is not full
			return false;
		}
		if(n.left==null&n.right==null) { //if it is leaf
			return true;
		}
		if(n.left!=null&n.right!=null) { //doing recursion until reaching the leaf
			return(isFull(n.left)&isFull(n.left));
		}
		return false;
	}
	/**
	 * Returns true if this tree is balanced based on the AVL tree balancing requirements 
	 * (i.e., for every node, the difference in height between its two sub-trees is at most 1).
	 * This operation should be O(N).
	 * @return true if this tree is balanced
	 */
	public boolean isBalanced() {
		if(this.root==null)
			return true;
		return IsB(root);
	}
	/**
	 * Recursive method to find the tree is balanced or not
	 * @param n - current node being processing
	 * @return true if this tree is balanced
	 */
	private boolean IsB(Node n) {
		if(n.left==null&n.right==null) { // if it is leaf
			return true;
		}
		else if(n.left==null&n.right!=null) { //if left subtree is null and right is not
			return(n.right.height<=1); //check if the right subtree's height is bigger than 1 or not
		}
		else if(n.left!=null&n.right==null) {//if right subtree is null and right is not
			return(n.left.height<=1);//check if the left subtree's height is bigger than 1 or not
		}
		else if(n.left!=null&n.right!=null) {//if the node has two child
			if(Math.abs(n.right.height-n.left.height)>1) // see the height of the two child difference is bigger than one or not
				return false;
			else //if not doing recursion
				return(IsB(n.left)&IsB(n.right));
		}
		return false; 
	}
	/**
	 * Returns the height of this tree. 
	 * The height of a leaf is 1. 
	 * The height of the tree is the height of its root node.This operation should be O(1).
	 * @return the height of this tree or zero if the tree is empty
	 */
	public int height() {
		if(root==null)
			return 0;
		else
			return root.height;
	}
	/**
	 * Returns an iterator over the elements in this tree in ascending order.
	 * This operation should be O(N).
	 * @return an iterator over the elements in this set in ascending order
	 */
	@Override
	public Iterator<E> iterator() {
		tempinorder=0;
		inorder = new ArrayList<E>();
		inorder(inorder,root);
		Iterator<E> itr1 = inorder.iterator();
		return itr1;
	}
	/**
	 * private iterator method used to iterate all the elements
	 * in the tree and stored it in the ArrayList with ascending order
	 * and give them index according to the order when they being processed
	 * @param i - the arrayList need to be modified
	 * @param n - current node being processing
	 */
	private void inorder(ArrayList<E> i, Node n){
		if(n!=null) {
			inorder(i,n.left);//go left
			n.index=tempinorder;//process the node
			tempinorder++;
			i.add(n.e);
			inorder(i,n.right);//go right
		}
	}
	/**
	 * Returns an iterator over the elements in this tree in order of the preorder traversal.
	 * This operation should be O(N).
	 * @return an iterator over the elements in this tree in order of the preorder traversal
	 */
	public Iterator<E> preorderIterator(){
		temppreorder=0;
		preorder = new ArrayList<E>();
		preorder(preorder,root);
		Iterator<E> itr2= preorder.iterator();
		return itr2;
	}
	/**
	 * private iterator method used to iterate all the elements
	 * in the tree and stored it in the ArrayList with preorder traversal
	 * and give them index according to the order when they being processed
	 * @param i - the arrayList need to be modified
	 * @param n - current node being processing
	 */
	private void preorder(ArrayList<E> i, Node n){
		if(n!=null) {
			n.index=temppreorder;//process the node
			temppreorder++;
			i.add(n.e);
			preorder( i,n.left );//go left
			preorder( i,n.right );//go right
		}
	}
	/**
	 * Returns an iterator over the elements in this tree in order of the preorder traversal.
	 * This operation should be O(N).
	 * @return an iterator over the elements in this tree in order of the preorder traversal
	 */
	public Iterator<E> postorderIterator(){
		temppostorder=0;
		postorder = new ArrayList<E>();
		postorder(postorder,root);
		Iterator<E> itr3= this.iterator();
		return itr3;
	}
	/**
	 * private iterator method used to iterate all the elements
	 * in the tree and stored it in the ArrayList with postorder traversal
	 * and give them index according to the order when they being processed
	 * @param i - the arrayList need to be modified
	 * @param n - current node being processing
	 */
	private void postorder(ArrayList<E> i, Node n){
		if(n!=null) {
			postorder(i,n.left );//go left
			postorder(i,n.right );//go right
			n.index=temppostorder;//process the node
			temppostorder++;
			i.add(n.e);	
		}
	}
	/**
	 * Returns the element at the specified position in this tree. 
	 * The order of the indexed elements is the same as provided by this tree's iterator. 
	 * The indexing is zero based (i.e., the smallest element in this tree is at index 0 and the largest one is at index size()-1).
	 * This operation should be O(H).
	 * @param index - index of the element to return
	 * @return the element at the specified position in this tree
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public E get​(int index) throws IndexOutOfBoundsException {
		if(index<0||index>size-1) {
			throw new IndexOutOfBoundsException();
		}
		ArrayList<E> a = new ArrayList<E>();
		findin(index,root,a);
		return a.get(index);
	}
	/**
	 * Private recursive method to help find the element with the index in the tree
	 * and return.
	 * @param i - the index to find
	 * @param n - current node being processing 
	 * @return the element at the specified position in this tree
	 */
	private E findin(int i,Node n,ArrayList<E> gl) {
		if(n!=null) {
			findin(i,n.left,gl);//go left
			gl.add(n.e);
			findin(i,n.right,gl);//go right
		}
		return null;
	}
	/**
	 * Returns a collection whose elements range from fromElement, inclusive, to toElement, inclusive. 
	 * The returned collection/list is backed by this tree,
	 * so changes in the returned list are reflected in this tree, and vice-versa 
	 * (i.e., the two structures share elements. The returned collection should be organized according to the natural ordering of the elements
	 * (i.e., it should be sorted).
	 * This operation should be O(M) where M is the number of elements in the returned list.
	 * @param fromElement - low end point (inclusive) of the returned collection
	 * @param toElement - high end point (inclusive) of the returned collection
	 * @return a collection containing a portion of this tree whose elements range from fromElement, inclusive, to toElement, inclusive
	 * @throws NullPointerException - if fromElement or toElement is null
	 * @throws IllegalArgumentException - if fromElement is greater than toElement
	 */
	public ArrayList<E> getRange​(E fromElement, E toElement) throws NullPointerException,IllegalArgumentException{
		if(fromElement==null||toElement==null) {
			throw new NullPointerException();
		}
		if(fromElement.compareTo(toElement) >0) {
			throw new IllegalArgumentException();
		}	
		ArrayList<E> a = new ArrayList<E>();   
		getarr(a, root, fromElement, toElement);
		return a;
	}
	/**
	 * Private recursion method to help the getRange method
	 * put the element in the ArrayList if the element is in the range of the
	 * given range
	 * @param arr - the ArrayList is returning
	 * @param n - current node being processed
	 * @param l - lower bond element
	 * @param h - higher bond element
	 */
	private void getarr(ArrayList<E> arr, Node n, E l, E h ) {
		if (n == null)
			return;
		else {
			if (l.compareTo(n.e)<=0){//if the lower bond is bigger than current element go left
				getarr(arr,n.left, l, h);
			}
			if (l.compareTo(n.e) <= 0 && h.compareTo(n.e) >= 0){//if it is in the range add the element
				arr.add(n.e);
			}    
			if (h.compareTo(n.e)>=0){//if the higher bond is smaller than current element go right
				getarr(arr,n.right, l, h);
			}
		}
	}
	/**
	 * Returns the least element in this tree greater than or equal to the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * @param e - the value to match
	 * @return the least element greater than or equal to e, or null if there is no such element
	 * @throws NullPointerException - if the specified element is null
	 * @throws ClassCastException - if the specified element cannot be compared with the elements currently in the set
	 */
	public E ceiling​(E e) throws NullPointerException,ClassCastException{
		if(e==null) {
			throw new NullPointerException();
		}
		return c(e,root);
	}
	/**
	 * Private method help the ceiling method to return the right element 
	 * @param e - the given element
	 * @param n - given node
	 * @return - the least element greater than or equal to e, or null if there is no such element
	 * @throws NullPointerException - if the specified element is null
	 */
	private E c(E e, Node n) throws NullPointerException{
		E ceil;
		if(e==null)  {
			return null;
		}
		if(n.e.compareTo(e)==0) {
			return n.e;
		}
		if (n.e.compareTo(e)>0) {
			if(n.left==null)
				return n.e;
			else if (n.left.e.compareTo(e)<0&n.left.right==null) 
				return n.e;
			else if(n.left!=null) {
				ceil = c(e,n.left);
				if(ceil == null)
					return n.e;
				else if(ceil.compareTo(e)<0)
					return n.e;
				else 
					return ceil;
    			}
		}
		if (n.e.compareTo(e)<0) {
			if(n.right==null)
				return null;
			else if(n.right!=null)
				return c(e,n.right);
		}
		return null;
	}
	/**
	 * Returns the greatest element in this set less than or equal to the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * @param e - the value to match
	 * @return the greatest element less than or equal to e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	public E floor​(E e) throws NullPointerException,ClassCastException{
		if(e==null) {
			throw new NullPointerException();
		}
		return f(e,root);
	}
	/**
	 * Private method used to help the floor method to return the right element
	 * @param e - the value to match
	 * @param n - current node being processed
	 * @return the greatest element less than or equal to e, or null if there is no such element
	 */
	private E f(E e, Node n) {
		E floor = null;
		if(e==null)  {
			return null;
		}
		if(n.e.compareTo(e)==0) {
			return n.e;
		}
		if (n.e.compareTo(e)>0) {
			if(n.left==null)
				return null;
			else if(n.left!=null)
				return f(e,n.left);
		}
		if (n.e.compareTo(e)<0) {
			if(n.right==null)
				return n.e;
			else if(n.right.e.compareTo(e)>0&n.right.left==null) {
				return n.e;
			}
			else if(n.right!=null)
				floor=f(e,n.right);
        	   	if(floor == null)
        	   		return n.e;
        	   	else if(floor.compareTo(e)>0)
        	   		return n.e;
        	   	else 
        	   		return floor;
		}
		return null;
	}
	/**
	 * Returns the first (lowest) element currently in this tree. 
	 * This operation should be O(H).
	 * @return the first (lowest) element currently in this tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	public E first() throws NoSuchElementException{
		if(this.root==null)
			throw new NoSuchElementException();
		Node curr = root;
		while(curr.left!=null) {//continue go left until reaching the smallest element
			curr=curr.left;
		}
		return curr.e;
	}
	/**
	 * Returns the last (highest) element currently in this tree.
	 * This operation should be O(H).
	 * @return the last (highest) element currently in this tree
	 * @throws NoSuchElementException - if this set is empty
	 */
	public E last() throws NoSuchElementException{
		if(this.root==null)
			throw new NoSuchElementException();
		Node curr = root;
		while(curr.right !=null) {//continue go right until reaching the biggest element
			curr=curr.right;
		}
		return curr.e;
	}
	/**
	 * Returns the greatest element in this set strictly less than the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * @param e - the value to match
	 * @return the greatest element less than e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	public E lower​(E e) throws NullPointerException,ClassCastException{
		if(e==null) {
			throw new NullPointerException();
		}
		return l(e,root);
	}
	/**
	 * Private recursive method for helping lower method
	 * to find the element strictly smaller than e
	 * @param e - the value to match
	 * @param n - current node
	 * @return the greatest element less than e, or null if there is no such element
	 */
	private E l ( E e, Node n) {
		if(n.e.compareTo(e)>0) {//if current element bigger than given element
			if(n.left==null) { //if cannot go left any more return current element
				return n.e;
			}
			if(n.left!=null) { //go left 
				return l(e,n.left);
			}	
		}
		if(n.e.compareTo(e)<0) {//if current element smaller than given element
			if(n.right==null) {//if cannot go right any more return current element
				return n.e;
			}
			if (n.right.e.compareTo(e)==0) {//if it is equal to the given element
				if(n.right.left==null)//if it cannot go left of this element return the current node
					return n.e;
				else 
					return l(e,n.right);//go right
			}
			if(n.right!=null) {
				return l(e,n.right);//go right
			}		
		}
		if(n.e.compareTo(e)==0) {//if it is equal to the given element
			if(n.left!=null) {//if it cannot go left of this element return the current node
				return l(e,n.left);
			}
			else//return null if there is no such element
				return null;
		}
		return null;
	}
	/**
	 * Returns the least element in this tree strictly greater than the given element, 
	 * or null if there is no such element.
	 * This operation should be O(H).
	 * @param e - the value to match
	 * @return the least element greater than e, or null if there is no such element
	 * @throws ClassCastException - if the specified element cannot be compared with the elements currently in the set
	 * @throws NullPointerException - if the specified element is null
	 */
	public E higher​(E e) throws NullPointerException,ClassCastException{
		if(e==null) {
			throw new NullPointerException();
		}
		return h(e , root);
	}
	/**
	 * Private recursive method for helping higher method
	 * to find the element strictly smaller than e
	 * @param e - the value to match
	 * @param n - current node
	 * @return the greatest element less than e, or null if there is no such element
	 */
	private E h( E e, Node n) {
		if(n.e.compareTo(e)>0) {//if current element bigger than given element
			if(n.left==null||n.left.e.compareTo(e)<0) {//if the left of current node is null or the left element is smaller than the given element 
				return n.e;
			}
			if(n.left.e.compareTo(e)==0) {//if the left element is equal to the given element
				if(n.left.right==null) {//return the current element if the current.right is null
					return n.e;
				}
				else
					return h(e,n.left.right);//go  right
			}
			if(n.left!=null) {//if left is not null
				return h(e,n.left);//go left
			}	
		}
		if(n.e.compareTo(e)<0) {//if current element smaller than given element
			if(n.right==null) {//if the right of current node is null or the right node's element is smaller than the given element 
				return n.e;
			}
			if(n.right!=null) {
				return h(e,n.right);//go right
			}		
		}
		if(n.e.compareTo(e)==0)
			if(n.right!=null) {
				return h(e,n.right);//go right
			}
			else
				return null;
		return null;
	}
	/**
	 * Compares the specified object with this tree for equality. 
	 * Returns true if the given object is also a tree, the two trees have the same size, 
	 * and every member of the given tree is contained in this tree.This operation should be O(N).
	 * @param obj - object to be compared for equality with this tree
	 * @return true if the specified object is equal to this tree
	 * @Override equals in class Object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals (Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(obj instanceof BST) {
			BST a = (BST) obj;
			if(a.size!=this.size) {// see the size is equal or not
				return false;
			}
			else
				return reequal (a.root,this.root);
		}
		else
			return false;
	}
	/**
	 * Private method used to help the equals method
	 * @param root2 the current node of obj
	 * @param n2 current node of the BST 
	 * @return true if the specified object is equal to this tree
	 */
	private boolean reequal(Node n1, Node n2) {
		if(n1==null&n2==null) {
			return true;
		}
		else if(n1==null&n2!=null||n1!=null&n2==null) {
			return false;
		}
		else if(n1.e.compareTo(n2.e)==0) {
			if((n1.left==null&n2.left!=null)||n1.right==null&n2.right!=null
					||(n1.left!=null&n2.left==null)||n1.right!=null&n2.right==null){//see the shape is equal or not
				return false;
			}
			else if(n1.left==null&n2.left==null&n1.right!=null&n2.right!=null){
				return reequal(n1.right, n2.right);
			}
			else if(n1.left!=null&n2.left!=null&n1.right==null&n2.right==null){
				return reequal(n1.left, n2.left);
			}
			else if(n1.left!=null&n2.left!=null&n1.right!=null&n2.right!=null){
				return( reequal(n1.left, n2.left)&reequal(n1.right, n2.right));
			}
			else
				return true;
		}
		else
			return false;
	}
	/**
	 * Returns a string representation of this tree.
	 * The string representation consists of a list of the tree's elements in the order they are returned 
	 * by its iterator (inorder traversal), enclosed in square brackets ("[]"). 
	 * Adjacent elements are separated by the characters ", " (comma and space). 
	 * Elements are converted to strings as by String.valueOf(Object).This operation should be O(N).
	 * @return a string representation of this collection
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		to(root,sb);
		sb.append("]");
		return sb.toString();
	}
	private void to(Node n, StringBuffer s) {
		if(n!=null) {
    		to(n.left,s);//go left
    		s.append(n.e.toString()+", ");
    		to(n.right,s);//go right
    	}
	}
	/**
	 * This function returns an array containing all the elements returned by this tree's iterator, 
	 * in the same order, stored in consecutive elements of the array, starting with index 0. 
	 * The length of the returned array is equal to the number of elements returned by the iterator.
	 * This operation should be O(N).
	 * @return an array, whose runtime component type is Object, containing all of the elements in this tree
	 */
	public Object[] toArray() {
		Object[] temp= new Object[size];
		toaa=0;
		toa(root,temp,toaa);
		return temp;
	}
	/**
	 * Private helper method to help the to array method
	 * @param n - the current node being processed
	 * @param temp - the array to return
	 * @param i - index for storing the element
	 */
	private void toa(Node n, Object[] temp,int i) {
		if(n!=null) {
    		toa(n.left,temp,i);//go left
    		temp[toaa++]=n.e;
    		toa(n.right,temp,i);//go right
    	}	
	}
	/**
	 * Produces a string representation of this tree that contains, one per line, 
	 * every path from the root of this tree to a leaf node in the tree. 
	 * The order of the paths should be from left to right. 
	 * In each path, the values in the nodes should be separated by a comma and a single space. 
	 * This operation should be O(N).
	 * @return string containing all root-leaf paths of this tree.
	 */
	public String toStringAllPaths() {
		String Allpath ="";
		if(root==null) {
			return Allpath;
		}
		else {
		Allpath+=root.e.toString();
		return all(Allpath,root);
		}
	}
	/**
	 * Private recursive method help toStringAllpath method
	 * @param s - the return string of toStringAllPaths method
	 * @param n - current node being processing
	 * @return string containing all root-leaf paths of this tree.
	 */
	private String all(String s, Node n) {
		if(n==null)
			return s;
		else if(n.left==null&n.right==null) {//the end of a route
			return s;
		}
		else if(n.left==null&n.right!=null) {//left is null go right
			s+=", "+n.right.e.toString();
			return all(s,n.right);
		}
		else if(n.left!=null&n.right==null) {//right is null go left
			s+=", "+n.left.e.toString();
			return all(s,n.left);
		}
		else if(n.left!=null&n.right!=null) {//have two sub tree then separate it into two line
			String a2 = s+", "+n.left.e.toString();
			s+=", "+n.right.e.toString();
			return (all(a2,n.left)+"\n"+all(s,n.right));
		}
		return null;
	}
	/**
	 * Produces a string representation of this tree that contains, one per line, 
	 * every path from the root of this tree to a leaf node in the tree whose length is maximal 
	 * (i.e., whose length matches the height of the tree). 
	 * The order of the paths should be from left to right. 
	 * In each path, the values in the nodes should be separated by a comma and a single space. 
	 * This operation should be O(N).
	 * @return string containing all root-leaf paths of this tree.
	 */
	public String toStringAllMaxPaths​() {
		String temp ="";
		temp+=root.e.toString();
		return max(temp,root,root.height);
	}
	/**
	 * Private method helping the toStringAllMaxPaths method
	 * @param a - the string to return
	 * @param n - current node to process
	 * @param h - the height of its parent node
	 * @return string containing all root-leaf paths of this tree.
	 */
	private String max(String a, Node n,int h) {
		if(n==null)
			return a;
		if(n.left==null&n.right==null) {//the end of a route
			return a;
		}
		if(n.left==null&n.right!=null) {//left is null go right
			a+=", "+n.right.e.toString();
			return max(a,n.right,n.right.height);
		}
		if(n.left!=null&n.right==null) {//right is null go right
			a+=", "+n.left.e.toString();
			return max(a,n.left,n.left.height);
		}
		if(n.left!=null&n.right!=null) {//have two sub tree then see the height
			if(h-n.right.height==1&h-n.left.height==1) {//if they are equal
				String a2 = a+", "+n.left.e.toString();
				a+=", "+n.right.e.toString();
				return (max(a2,n.left,n.left.height)+"\n"+max(a,n.right,n.right.height));
			}
			if(h-n.left.height==1) {//left tree satisfied the max
				a+=", "+n.left.e.toString();
				return max(a,n.left,n.left.height);}
			if(h-n.right.height==1) {//right tree satisfied the max
				a+=", "+n.right.e.toString();
				return max(a,n.right,n.right.height);}
		}		
		return null;
	}
	/**
	 * Produces tree like string representation of this tree.
	 * Returns a string representation of this tree in a tree-like format. 
	 * The string representation consists of a tree-like representation of this tree. 
	 * Each node is shown in its own line with the indentation showing the depth of the node in this tree. 
	 * The root is printed on the first line, followed by its left subtree, followed by its right subtree.
	 * This operation should be O(N).
	 * Helping from the BST-with Comparator from ed
	 * @return string containing tree-like representation of this tree.
	 */
	public String toStringTreeFormat( ) {
		StringBuffer sb = new StringBuffer(); 
		toStringTree(sb, root, 0);
		return sb.toString();
	}
	/**
	 * private recursive helper method to help the toStringTreeFormat method
	 * @param sb - StringBuffer
	 * @param node - current node being processing 
	 * @param level - number to record the level 
	 */
	private void toStringTree( StringBuffer sb, Node node, int level ) {
		if (level > 0 ) {//to see how many space need to be printed
			for (int i = 0; i < level-1; i++) {
				sb.append("   ");
			}
			sb.append("|--");
		}
		if (node == null) {
			sb.append( "null\n"); 
			return;
		}
		else {
			sb.append( node.e + "\n"); 
		}
		toStringTree(sb, node.left, level+1); //go left 
		toStringTree(sb, node.right, level+1);//go right
	}
}
