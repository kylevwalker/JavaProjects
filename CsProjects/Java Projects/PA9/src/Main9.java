
public class Main9 {

	public static void main(String[] args) throws Exception {
		PatientQueue pq = new PatientQueue();
		System.out.println(pq.isEmpty());
        pq.enqueue("Anat", 4);
        System.out.println(pq.toString());
        pq.enqueue("Ben", 9);
        pq.enqueue("Sam", 8);
        pq.enqueue("Wu", 7);
        pq.enqueue("Rein", 6);
        pq.enqueue("Eve", 10);    
        System.out.println(pq);
        System.out.println(pq.dequeue());
        System.out.println(pq);
        System.out.println(pq.dequeue());
        System.out.println(pq.isEmpty());
        pq.enqueue("Brass ass", 1);
        pq.enqueue("Brass ass", 9);
        System.out.println(pq);
        System.out.println(pq.size());
        pq.changePriority("Brass ass", 8);
        System.out.println(pq.peek());
        System.out.println(pq.peekPriority());
        System.out.println(pq);
        
        

//        while (!pq.isEmpty())
//            System.out.println(pq.dequeue());  

	}

}
