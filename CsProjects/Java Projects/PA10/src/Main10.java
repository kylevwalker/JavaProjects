
public class Main10 {

	public static void main(String[] args) {
		MyTreeMap<Integer, String> tm = new MyTreeMap<Integer, String>();
		System.out.println(tm.containsValue("Cran"));
		System.out.println(tm.put(284, "Sam"));
		System.out.println(tm.put(353, "Jan"));
		System.out.println(tm.put(768, "Stan"));
		System.out.println(tm.put(599, "Dan"));
		System.out.println(tm.put(593, "Ann"));
		System.out.println(tm.isEmpty());
		tm.clear();
		
		System.out.println(tm.put(284, "Sam"));
		System.out.println(tm.put(353, "Jan"));
		System.out.println(tm.put(768, "Stan"));
		System.out.println(tm.put(599, "Cran"));
		System.out.println(tm.containsValue("Cran"));
		System.out.println(tm.put(593, "Ann"));
		System.out.println(tm.get(599));
		System.out.println(tm.put(599, "Dan"));
		tm.printTree();
		System.out.println(tm.keySet().toString());
		System.out.println(tm.size());
		System.out.println(tm.remove(599));
		tm.printTree();
		System.out.println(tm.keySet().toString());
		System.out.println(tm.size());
	}

}
