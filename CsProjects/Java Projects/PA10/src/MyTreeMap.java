/*
 * AUTHOR: Kyle Walker
 * FILE: MyTreeMap.java
 * ASSIGNMENT: Programming Assignment 8 - MyTreeMap
 * COURSE: CSc 210; Fall 2021
 * PURPOSE: This class creates a custom TreeMap using a BinarySearchTree inner class. 
 * The TreeMap stores all key value mappings in a binary search tree in order of keys.
 * The TreeMap has many methods including: clearing the map,
 * checking if a key or value exists in the map, checking if the map is empty, getting
 * values of certain keys, getting a set of all keys, removing mappings, getting the size
 * of the Map, and printing all contents of the Map as a table of sorted key value pairs. 
 * The Tree Map automatically sorts the tree during any putting or removal.
 */



import java.util.HashSet;
import java.util.Set;

public class MyTreeMap<K extends Comparable<K>,V> {
	BinarySearchTree bst;
	

	/*
	 * MyTreeMap constructor, sets bst contents to new BinarySearchTree with generic types for 
	 * key and value. Default root node is set to Null.
	 */
	public MyTreeMap(){
		this.bst = new BinarySearchTree();
	}
	
	/*
	 * Clears all data of the Map by replacing bst BinarySearchTree with a newly constructed 
	 * BinarySearchTree.
	 */
	public void clear() {
		this.bst = new BinarySearchTree();
	}
	
	/*
	 * Checks if the given key exists in the TreeMap.
	 * 
	 * @param key, generic type key being searched for.
	 * @return boolean, true if Map contains key.
	 */
	public boolean containsKey(K key) {
		return bst.contains(key);
	}
	
	/*
	 * Checks if the given value is contained in the TreeMap.
	 * 
	 * @param val, generic type value being searched for.
	 * @return boolean, true if Map contains value.
	 */
	public boolean containsValue(V val) {
		return bst.containsVal(val);
	}
	
	/*
	 * returns corresponding value at the given key.
	 * 
	 * @param key, generic type key being searched for.
	 * @return V, generic type value of given key.
	 */
	public V get(K key) {
		return bst.get(key);
	}
	
	/*
	 * Checks if the current TreeMap is empty.
	 * 
	 * @return boolean, true if Map contains no key value pairs.
	 */
	public boolean isEmpty() {
		if (bst.size() == 0)
			return true;
		else
			return false;
	}
	
	/*
	 * Returns a set containing all keys within the TreeMap.
	 * 
	 * @return allKeys, java.util.set<K> containing all keys in TreeMap.
	 */
	public java.util.Set<K> keySet(){
		return bst.getKeySet();
	}
	
	/*
	 * Prints out the contents of the TreeMap by sorted order of Keys with 
	 * each corresponding Value.
	 */
	public void printTree() {
		bst.print();
	}
	
	/*
	 * Adds a new Key Value pair to the TreeMap. This pair is created as a new node
	 * for the BinarySearchTree and is recursively sorted into the tree.
	 * 
	 * @param key, generic type key being added in the TreeMap.
	 * @param val, generic type value being added in the TreeMap.
	 * @return val, generic type value previously stored in the node with the same Key,
	 * 				returns null if Key did not exist previously.
	 */
	public V put(K key, V val) {
		V prev = get(key);
		bst.add(key, val);
		return prev;
		
	}
	
	/*
	 * removes a given key and its value from the TreeMap.
	 * 
	 * @param key, generic type key for the pair to be removed from the Map.
	 * @return val, generic type value that was removed from the Map if Key existed before.
	 * 				otherwise, returns null.
	 */
	public V remove(K key) {
		V prev = bst.get(key);
		bst.delete(key);
		return prev;
	}
	
	/*
	 * Returns the number of key value mappings within the TreeMap.
	 * 
	 * @return int, integer size of the Map in terms of total mappings.
	 */
	public int size() {
		return bst.size();
	}
	
	
	//--------------------------------------------------------------------------------------------------
	

	/*
	 * BinarySearchTree inner class which organizes Key Value mappings into a tree sorted
	 * by Keys. The Tree recursively adjusts the order of nodes every time a new node is
	 * added or an existing node is removed.
	 */
public class BinarySearchTree
{
	/*
	 * BinarySearchTree Node inner class which stores the Key and Value of one mapping, and
	 * contains pointers to possible child nodes.
	 */
    private class Node
    {    
    	private K key;
        private V value;    
        private Node left;  
        private Node right;  
            
        /*
    	 * Node constructor, Sets the Key and Value to the given key and value data. Children
    	 * pointers are set to null by default.
    	 */
        public Node(K key, V value) 
        {    
    		this.key = key;
    		this.value = value;
    		this.left = null;    
            this.right = null;    
        }    
    }    
     
    public Node root = null;       

    
    //-------------------------------------------------------------
	/*
	 * Adds a new Key Value pair to the Tree. Calls upon the addRecur method to recursively 
	 * sort the new node into the Tree.
	 * 
	 * @param key, generic type key being added in the Tree.
	 * @param val, generic type value being added in the Tree.
	 */
    public void add(K key, V value) {
        root = addRecur(root, key, value);
    }
    
	/*
	 * Recursively sorts the new node into the Tree.
	 * 
	 * @param curr, Node pointer to current position in tree.
	 * @param key, generic type key being added in the Tree.
	 * @param val, generic type value being added in the Tree.
	 * @return curr, Node pointer to current position in tree.
	 */
    private Node addRecur(Node curr, K key, V value) {
        if (curr == null) 
            return new Node(key, value);
        else
        	if (key.compareTo(curr.key) < 0)
        		curr.left = addRecur(curr.left, key, value);
        	else if (key.compareTo(curr.key) > 0) 
        		curr.right = addRecur(curr.right, key, value);
        	else
        		curr.value = value;
        return curr;
    }    

    //-------------------------------------------------------------
	/*
	 * Gets the corresponding value of a given Key. Calls getRecur.
	 *
	 * @param key, generic type key being searched for in the Tree.
	 * @return getRecur(root, key), generic type value from getter.
	 */
    public V get(K key) {
        return getRecur(root, key);
    }

	/*
	 * Recursively finds the corresponding value of a given Key.
	 *
	 * @param key, generic type key being searched for in the Tree.
	 * @return value, generic type value found from search.
	 */
    private V getRecur(Node curr, K key) {
        if (curr == null)
            return null;
        if (key.equals(curr.key))
            return curr.value;
        return (key.compareTo(curr.key) < 0) ? getRecur(curr.left, key) : getRecur(curr.right, key);
    } 
    
    //-------------------------------------------------------------
	/*
	 * Checks if the given Key is contained in the Tree. Calls containsRecur(root)
	 * 
	 * @param key, generic type key being searched for.
	 * @return boolean, true if Tree contains key.
	 */
    public boolean contains(K key) {
        return containsRecur(root, key);
    }
	/*
	 * Checks if the given value is contained in the Tree. Calls containsValRecur(root)
	 * 
	 * @param val, generic type value being searched for.
	 * @return boolean, true if Tree contains value.
	 */
    public boolean containsVal(V val) {
        return containsValRecur(root, val);
    }

	/*
	 * Recursively checks if the given Key is contained in the Tree.
	 * 
	 * @param curr, Node pointer to current position in tree.
	 * @param key, generic type key being searched for.
	 * @return boolean, true if Tree contains key.
	 */
    private boolean containsRecur(Node curr, K key) {
        if (curr == null)
            return false;
        if (key.equals(curr.key))
            return true;
        return (key.compareTo(curr.key) < 0) ? containsRecur(curr.left, key) : containsRecur(curr.right, key);
    } 
       
	/*
	 * Recursively checks if the given Value is contained in the Tree.
	 * 
	 * @param curr, Node pointer to current position in tree.
	 * @param val, generic type value being searched for.
	 * @return boolean, true if Tree contains value.
	 */
    private boolean containsValRecur(Node curr, V val) {
        if (curr == null)
            return false;
        if (val.equals(curr.value))
            return true;
        return containsValRecur(curr.left, val) || containsValRecur(curr.right, val);
    } 

    //-------------------------------------------------------------
	/*
	 * Calls deleteRecur on current Key to delete it from the Tree.
	 * 
	 * @param key, generic type key being searched for.
	 */
    public void delete(K key) {
        root = deleteRecur(root, key);
    }   
    
	/*
	 * Recursively deletes a given node and adjusts order of Tree.
	 * 
	 * @param curr, Node pointer to current position in tree.
	 * @param key, generic type key being searched for deletion.
	 * @return curr, Node pointer to current position in tree.
	 */
    private Node deleteRecur(Node curr, K key) {
        if (curr == null)  // key not in tree
            return null;

        if (key.equals(curr.key))  // remove this node
        {
        	if (curr.left == null && curr.right == null)
        	    return null;
        	else if (curr.right == null) // replace this node with left child
        	    return curr.left;
        	else if (curr.left == null) // replace this node with right child
        	    return curr.right;
        	else
        	{
        		Node n = maxNode(curr.left);
        		curr.key = n.key;
        		curr.value = n.value;
        		curr.left = deleteRecur(curr.left, n.key);
        		return curr;
        	}
        }
        else
        {
        	if (key.compareTo(curr.key) < 0) 
        		curr.left = deleteRecur(curr.left, key);
        	else
        		curr.right = deleteRecur(curr.right, key);

        	return curr;
        }
    }    

    
    //-------------------------------------------------------------
	/*
	 * Recursively searches for the farthest right node in the branch, then returns it.
	 * 
	 * @param root, Node pointer to current "root" of the subtree
	 * @return root, Node pointer to maximum right leaf node.
	 */
    private Node maxNode(Node root) {
        return root.right == null ? root : maxNode(root.right);
    } 
    
    //-------------------------------------------------------------   
	/*
	 * Prints the contents of the Tree with inorder traversal.
	 * 
	 * @param node, node pointer to root of current subtree.
	 */
    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            System.out.println(node.key + ", " + node.value);
            traverseInOrder(node.right);
        }
    }    
       
    //-------------------------------------------------------------
	/*
	 * Calls traverseInOrder from the root to help print the entire tree.
	 */
    public void print() 
    {    
    	traverseInOrder(root);
    }    
    
    // ------------------------------------------------------------
	/*
	 * Helper function to call countNodes from the root.
	 * 
	 * @return countNodes(root), resulting integer size of tree from function.
	 */
    public int size() {
    	return countNodes(root);
    }
    
	/*
	 * Recursively adds the number of nodes in the tree to the size count.
	 * 
	 * @param curr, Node pointer to current "root" of the subtree
	 * @return size, total number of mappings in Tree.
	 */
    public int countNodes(Node curr) {
    	if (curr == null)
    		return 0;
    	else {
    		return 1 + countNodes(curr.left) + countNodes(curr.right);
    	}
    }
    // -------------------------------------------------------------
    
	/*
	 * Adds all Key values to a set to return. Calls addToSet to fill set.
	 * 
	 * @return keySet, Set containing all keys from the Tree.
	 */
    public java.util.Set<K> getKeySet(){
    	Set<K> keySet = new HashSet<K>();
    	addToSet(root, keySet);
    	return keySet;
    		
        }
    
	/*
	 * Adds all Key values to a set to return.
	 * 
	 * @param curr, Node pointer to current position in Tree.
	 * @param keySet, Set containing all keys from the Tree.
	 */
    public void addToSet(Node curr, Set<K> keySet) {
        if (curr != null) {
        	keySet.add(curr.key);
        	addToSet(curr.left, keySet);
            addToSet(curr.right, keySet);
        }
            
    }
}


}
