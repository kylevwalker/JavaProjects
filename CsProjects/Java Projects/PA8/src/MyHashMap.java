/*
 * AUTHOR: Kyle Walker
 * FILE: MyHashMap.java
 * ASSIGNMENT: Programming Assignment 8 - MyHashMap
 * COURSE: CSc 210; Fall 2021
 * PURPOSE: This class creates a custom HashMap using a built in HashTable inner class. 
 * The HashTable hashes the keys and fits them into an 8 element array, where all
 * collisions are added to the front of the index linked list. All key/value pairs
 * are stored as custom nodes within the linked lists of each of the HashTable's 
 * indexes. The MyHashMap class has many useful Map methods including: clearing the map,
 * checking if a key or value exists in the map, checking if the map is empty, getting
 * values of certain keys, getting a set of all keys, removing pairs, getting the size
 * of the Map, and printing all contents of the Map as a table with collision data and
 * all keys listed by index. The hashMap accepts generic types for Key and Value, so 
 * any types can be stored in the Map.
 */


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import java.util.HashSet;


public class MyHashMap<K,V>{
	HashTable<K,V> ht;
	
	/*
	 * myHashMap constructor, sets ht contents to new HashTable with generic types for 
	 * key and value. Has a default capacity of 8.
	 */
	public MyHashMap() {
		ht = new HashTable<K,V>();
	}
	
	/*
	 * Clears all data of the Map by replacing ht HashTable with a newly constructed 
	 * HashTable.
	 */
	public void clear() {
		ht = new HashTable<K,V>();
	}
	
	/*
	 * Checks if the given key is contained in the HashTable.
	 * 
	 * @param key, generic type key being searched for.
	 * @return boolean, true if Map contains key.
	 */
	public boolean containsKey(K key) {
		if (ht.contains(key)){
			return true;
		}
		else {return false;}
	}
	
	/*
	 * Checks if the given value is contained in the HashTable. Indexes through HashTable and
	 * searches for node containing given value.
	 * 
	 * @param val, generic type value being searched for.
	 * @return boolean, true if Map contains value.
	 */
	public boolean containsValue(V val) {
		for (int i = 0; i< HashTable.TABLE_SIZE; i++) {
			LinkedList<Node<K,V>> list = ht.hashTable.get(i);
			for (Node<K,V> cur: list) {
				if (cur.value.equals(val)) {
					return true;
					}
				}	
		}
		return false;
	}

	/*
	 * returns corresponding value at the given key.
	 * 
	 * @param key, generic type key being searched for.
	 * @return V, generic type value of given key.
	 */
	public V get(K key) {
		return ht.get(key);
	}
	
	/*
	 * Checks if the current HashMap is empty.
	 * 
	 * @return boolean, true if Map contains no key value pairs.
	 */
	public boolean isEmpty() {
		for (int i = 0; i< HashTable.TABLE_SIZE; i++) {
			LinkedList<Node<K,V>> list = ht.hashTable.get(i);
			if (!list.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Returns a set containing all keys within the HashMap.
	 * 
	 * @return allKeys, java.util.set<K> containing all keys in HashTable.
	 */
	public java.util.Set<K> keySet() {
		Set<K> allKeys = new HashSet<K>();
		for (int i = 0; i< HashTable.TABLE_SIZE; i++) {
			LinkedList<Node<K,V>> list = ht.hashTable.get(i);
			for (Node<K,V> cur: list) {
				allKeys.add(cur.key);
				}
			}
		return allKeys;
		}
	
	/*
	 * Prints out the contents of the HashTable by  order of index, with info on
	 * number of collisions per index and whihc keys exist for each index.
	 */
	public void printTable() {
		String[] contents = ht.toString().split("\n");
		int totalConflicts = 0;
		for (int i = 0; i< HashTable.TABLE_SIZE; i++) {
			String keys = contents[i].substring(3);
			int conflicts = 0;
			if (keys != null) {
				conflicts = keys.split(" ").length - 1;
				if (conflicts > 0) {
					conflicts -= 1;
				}
				totalConflicts += conflicts;
			}
			System.out.println("Index " + i + ": (" + conflicts + " conflicts), " + keys);
		}
		System.out.println("Total # of conflicts: " + totalConflicts);
	}
	
	/*
	 * Adds a new Key Value pair to the HashMap. This pair is created as a new node
	 * for the HashTable and is given a Hashcode to fit in one of the indexes.
	 * 
	 * @param key, generic type key being added in the HashMap
	 * @param val, generic type value being added in the HashMap
	 * @return val, generic type value being added to the Map.
	 */
	public V put(K key, V val) {
		ht.put(key, val);
		return val;
	}
	
	/*
	 * removes a given key and its value from the HashMap.
	 * 
	 * @param key, generic type key for the pair to be removed from the Map.
	 * @return val, generic type value that was removed from the Map.
	 */
	public V remove(K key) {
		//TODO
		if (ht.contains(key)) {
			int index = ht.hash(key);
			LinkedList<Node<K,V>> list = ht.hashTable.get(index);
			for (Node<K,V> cur: list) {
				if (cur.key.equals(key)) {
					V val = ht.get(key);
					list.remove(cur);
					return val;
				}
			}		
		}
		return null;
	}
	
	/*
	 * Returns the number of key value pairs within the HashMap.
	 * 
	 * @return int, integer size of the Map in terms of total pairs.
	 */
	public int size() {
		return this.keySet().size();
	}

	
	// --------------------------------------------------------------------------------
	/*
	 * Node class containing Nodes and Values of generic types to be used in the Linked Lists
	 * of the HashTable class.
	 */
	class Node<K,V>
	{
		K key;
		V value;
		public Node(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
	}

	
	/*
	 * hashTable Class that accepts generic type key and value Nodes and stores them
	 * with linked lists as indexes of a size 8 array. All keys are hashed and stored in
	 * one of the 8 indexes.
	 */
	public class HashTable<K,V> 
	{
		private final static int TABLE_SIZE = 8;
		ArrayList<LinkedList< Node<K,V>>> hashTable = new ArrayList<>(TABLE_SIZE);

		/*
		 * Constructs a new HashTable with 8 empty indexes.
		 */
		public HashTable()
		{
			for (int i = 0; i < TABLE_SIZE; i++)
				hashTable.add(new LinkedList<Node<K,V>>());
		}
		
		/*
		 * Hashes each new key and creates a code for its index within the HashTable. 
		 * Each code will be within the range of 8.
		 */
		private int hash(K key) {
			int hashCode = key.hashCode();
			int index = hashCode % TABLE_SIZE;
			return Math.abs(index);
		}
		
		/*
		 * Creates a new node containing the Key and value and puts it in the front of
		 * its hashed index in the HashTable.
		 * 
		 * @param key, generic type key being added in the HashTable
		 * @param val, generic type value being added in the HashTable
		 */
		public void put(K key, V value)
		{
			int index = hash(key); 
			Node<K, V> node = new Node<>(key, value);
			hashTable.get(index).addFirst(node); // assumes keys are unique		
		}
		
		/*
		 * Gets the value of the given key within the HashTable.
		 * 
		 * @param key, generic type key being searched for.
		 * @return val, generic type value at key.
		 */
		public V get(K key)
		{
			int index = hash(key); 
			
			LinkedList<Node<K,V>> list = hashTable.get(index);
			for (Node<K,V> n: list)
				if (n.key.equals(key))
					return n.value;
			return null;
		}
		
		/*
		 * Checks if the given value is contained in the HashTable. 
		 * 
		 * @param key, generic type key being added in the HashMap
		 * @param boolean, true if key exists in HashTable.
		 */
		public boolean contains(K key) {
			V value = get(key);
			return value == null ? false : true;		
		}
		
		/*
		 *	converts the contents of the HashTable into a readable String.
		 * 
		 * @return str, String with each index of the Table's contents
		 */
		public String toString()
		{
			String str = "";
			for (int i = 0; i < TABLE_SIZE; i++) 
			{
				str += i + ": [";
				LinkedList<Node<K,V>> list = hashTable.get(i);

			    for(int j = 0; j < list.size(); j++)
					str += list.get(j).key + ", ";

				str += "]\n";
			}
			return str;		
		}
	}


}
