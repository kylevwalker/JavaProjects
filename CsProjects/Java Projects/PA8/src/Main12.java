public class Main12 {
	public static void main(String[] args) {
		MyHashMap<String, Integer> ht = new MyHashMap<>();
		//HashTable<String, Integer> ht = new HashTable<>();
		System.out.println("Putting 9 key value pairs in Hashmap");
		ht.put("Jack", 4237);
		ht.put("Jill", 4257);
		ht.put("Pat", 3232);
		ht.put("Tom", 5784);
		ht.put("Jan", 2934);
		ht.put("Maurice", 8735);
		ht.put("Monica", 8735);
		ht.put("Mandrake", 8735);
		ht.put("Mandy", 8735);

		System.out.println("Contains Key Jack? yes");
		System.out.println(ht.containsKey("Jack"));
		System.out.println("Contains Key Marie? no");
		System.out.println(ht.containsKey("Marie"));
		System.out.println("Get value of key Jack 4237");
		System.out.println(ht.get("Jack"));
		System.out.println("Remove Key Jack");
		System.out.println(ht.remove("Jack"));
		System.out.println("Contains Key Jack? no");
		System.out.println(ht.containsKey("Jack"));
		System.out.println("Contains Value 3232? yes");
		System.out.println(ht.containsValue(3232));
		System.out.println("Contains Value 8735? no");
		System.out.println(ht.containsValue(8735));
		System.out.println("Size of Map? 9");
		System.out.println(ht.size());
		System.out.println("Returning KeySet");
		System.out.println(ht.keySet());
		System.out.println("is Map empty? no");
		System.out.println(ht.isEmpty());
		ht.printTable();
		System.out.println("Clearing Map");
		
		ht.clear();
		System.out.println("IsEmpty? yes");
		System.out.println(ht.isEmpty());
		System.out.println("Get value of key Jack Null");
		System.out.println(ht.get("Jack"));
		System.out.println("Remove Key Jack Error");
		System.out.println(ht.remove("Jack"));
		System.out.println("Size of Map? 0");
		System.out.println(ht.size());
		System.out.println("Returning KeySet Empty");
		System.out.println(ht.keySet());
		ht.printTable();
	}
}
