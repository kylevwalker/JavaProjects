import java.util.ArrayList;
import java.util.LinkedList;


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

public class HashTable<K,V> 
{
	private final static int TABLE_SIZE = 8;
	ArrayList<LinkedList< Node<K,V>>> hashTable = new ArrayList<>(TABLE_SIZE);

	public HashTable()
	{
		for (int i = 0; i < TABLE_SIZE; i++)
			hashTable.add(new LinkedList<Node<K,V>>());
	}
	
	private int h(K key) {
		int hashCode = key.hashCode();
		int index = hashCode % TABLE_SIZE;
		return Math.abs(index);
	}
	
	public void put(K key, V value)
	{
		int index = h(key); 
		Node<K, V> node = new Node<>(key, value);
		hashTable.get(index).addFirst(node); // assumes keys are unique		
	}
	
	public V get(K key)
	{
		int index = h(key); 
		
		LinkedList<Node<K,V>> list = hashTable.get(index);
		for (Node<K,V> n: list)
			if (n.key.equals(key))
				return n.value;
		
		return null;
	}
	
	public boolean contains(K key) {
		V value = get(key);
		return value == null ? false : true;		
	}
	
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

