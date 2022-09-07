/*
 * AUTHOR: Kyle Walker
 * FILE: PatientQueue.java
 * ASSIGNMENT: Programming Assignment 9 - PatientQueue
 * COURSE: CSc 210; Fall 2021
 * PURPOSE: This class is an implementation of a Priority Queue representing patients in a hospital
 * with varying priorities of urgency. Patients will be automatically sorted by priority in the heap
 * so that the patients with the most urgent needs will be served first despite the order they were
 * enqueued. The class contains the following methods: enqueue a new Patient to the queue, dequeue
 * the highest priority Patient, peek the name of the highest priority patient, peek the priority
 * value of the highest priority patient, change the priority value of a patient, check if the 
 * Queue is empty, return the size of the queue, clear the queue, and return a string representation
 * of the queue. These methods depend on the sorted "Binary Tree" structure of the inner Heap class,
 * which automatically bubbles up or down when new Patients are added or removed.
 */

import java.util.Arrays;

public class PatientQueue {
	private Heap queue;
	private int size;

	/*
	 * Constructs a new PatientQueue object with an empty Heap queue and size of 0.
	 */
	public PatientQueue() {
		queue = new Heap();
		size = 0;
	}

	/*
	 * Creates a new patient with the given name and priority and adds it to the
	 * Queue in proper sorted order.
	 * 
	 * @param name, String for the name of the new Patient object.
	 * 
	 * @param priority, Integer for the priority value of the new Patient object.
	 */
	public void enqueue(String name, int priority) {
		Patient newPatient = new Patient(name, priority);
		enqueue(newPatient);
	}

	/*
	 * Enqueues the given Patient to a sorted position in the Queue.
	 * 
	 * @param patient, Existing Patient object to be enqueued into the Queue.
	 */
	public void enqueue(Patient patient) {
		queue.add(patient);
		size++;
	}

	/*
	 * Removes the Patient of the highest priority from the Queue and returns the
	 * removed patient's name String.
	 * 
	 * @return name, String name of removed patient.
	 */
	public String dequeue() throws Exception {
		if (queue.isEmpty()) {
			throw new Exception("Error: Cannot dequeue an empty PatientQueue");
		}
		size--;
		return queue.remove().name;
	}

	/*
	 * Peeks the highest priority patient in the queue and returns their name.
	 * Throws an exception if the queue is empty
	 * 
	 * @return name, String name of highest priority patient.
	 */
	public String peek() throws Exception {
		if (queue.isEmpty()) {
			throw new Exception("Error: Cannot peek on an empty PatientQueue");
		}
		return queue.heap[1].name;
	}

	/*
	 * Peeks the highest priority patient in the queue and returns their priority
	 * value. Throws an exception if the queue is empty
	 * 
	 * @return priority, Integer priority value of highest priority patient.
	 */
	public int peekPriority() throws Exception {
		if (queue.isEmpty()) {
			throw new Exception("Error: Cannot peekPriority on an empty PatientQueue");
		}
		return queue.heap[1].priority;
	}

	/*
	 * Finds first occurrence of an existing Patient by name and replaces their
	 * priority value with the new given priority. Their position in the queue will
	 * automatically be changed.
	 * 
	 * @param name, String for the name of the patient to be changed.
	 * 
	 * @param newPriority, Integer for the priority value of the new Patient object.
	 */
	public void changePriority(String name, int newPriority) {
		for (int i = 0; i < size; i++) {
			Patient cur = queue.heap[i];
			if (cur != null && cur.name == name) {
				queue.remove(i);
				enqueue(name, newPriority);
				i = size;
			}
		}
	}

	/*
	 * Checks if the current PriorityQueue is empty and returns true if it is.
	 * 
	 * @return boolean, boolean representing if Queue is empty or not.
	 */
	public boolean isEmpty() {
		if (queue.isEmpty()) {
			return true;
		}
		return false;
	}

	/*
	 * Returns the size of the current PriorityQueue by number of patients.
	 * 
	 * @return size, int value of number of Patients in the PriorityQueue
	 */
	public int size() {
		return size;
	}

	/*
	 * Clears the entire Queue and creates a new empty Queue heap.
	 */
	public void clear() {
		this.queue = new Heap();
	}

	/*
	 * Returns a String representation of the Queue heap in the format: {String name
	 * (int priority), ...}
	 * 
	 * @return toString, String representation of Queue called in heap class.
	 */
	public String toString() {
		return queue.toString();
	}

// ------------------------------------------------------------------
	public class Heap {

		private static final int DEFAULT_CAPACITY = 10;
		private Patient[] heap;
		private int size;

		/*
		 * Constructs a new Heap object with an empty Array and default capacity of 10
		 * and size 0.
		 */
		public Heap() {
			heap = (Patient[]) new Patient[DEFAULT_CAPACITY];
			size = 0;
		}

		/*
		 * Finds parent of the given Patient of index i.
		 * 
		 * @param i, integer index of current Patient.
		 * 
		 * @return i/2, Integer for index of parent in terms of the Array.
		 */
		private int parent(int i) {
			return i / 2;
		}

		/*
		 * Finds left child of the given Patient of index i.
		 * 
		 * @param i, integer index of current Patient.
		 * 
		 * @return 2*i, Integer for index of left child in terms of the Array.
		 */
		private int left(int i) {
			return 2 * i;
		}

		/*
		 * Finds right child of the given Patient of index i.
		 * 
		 * @param i, integer index of current Patient.
		 * 
		 * @return 2*i + 1, Integer for index of right child in terms of the Array.
		 */
		private int right(int i) {
			return 2 * i + 1;
		}

		/*
		 * Compares two patients a and b to see if a is of higher priority than b. If
		 * both patients have the same priority, the one with the alphabetically lesser
		 * name will be of higher priority.
		 * 
		 * @param a, Patient being compared for higher priority.
		 * 
		 * @param b, Patient being compared for lower priority.
		 * 
		 * @return boolean, boolean showing if a is of higher priority than b or not.
		 */
		private boolean compare(Patient a, Patient b) {
			if (a.priority < b.priority) {
				return true;
			}
			if (a.priority == b.priority) {
				if (a.name.compareTo(b.name) < 0) {
					return true;
				}
			}
			return false;

		}

		/*
		 * Rearranges given index to proper order by recursively comparing to its
		 * parent. The current index moves its way up the "Tree" until it is in proper
		 * order.
		 * 
		 * @param i, integer index of current Patient.
		 */
		private void bubbleUp(int i) {
			if (i > 1 && compare(heap[i], heap[parent(i)])) {
				Patient p = heap[parent(i)];
				heap[parent(i)] = heap[i];
				heap[i] = p;
				bubbleUp(parent(i));
			}
		}

		/*
		 * Rearranges given index to proper order by recursively comparing to its
		 * children. The current index moves its way down the "Tree" until it is in
		 * proper order.
		 * 
		 * @param i, integer index of current Patient.
		 */
		private void bubbleDown(int i) {
			if (left(i) <= size) {
				int priority = left(i);
				if (right(i) <= size && compare(heap[right(i)], heap[left(i)])) {
					priority = right(i);
				}
				if (compare(heap[priority], heap[i])) {
					Patient p = heap[priority];
					heap[priority] = heap[i];
					heap[i] = p;
					bubbleDown(priority);
				}
			}
		}

		/*
		 * Adds a new Patient to the heap and bubbles up until it is in proper order. If
		 * the heap becomes bigger than the default size, it will be resized by double
		 * to accommodate more Patients. Records increase in size.
		 * 
		 * @param p, Patient which is being added to the Heap.
		 */
		public void add(Patient p) {
			if (size == heap.length - 1)
				resize();

			size++;
			heap[size] = p;
			bubbleUp(size);
		}

		/*
		 * Removes the patient of the highest priority from the heap and bubblesDown
		 * until the new "Root" is in proper order with its children. Returns the
		 * patient which was removed or returns null if empty.
		 */
		public Patient remove() {
			if (isEmpty())
				return null;

			Patient p = heap[1];
			heap[1] = heap[size];
			size--;
			bubbleDown(1);
			return p;
		}

		/*
		 * Removes the patient of the given index from the heap and bubblesDown until
		 * the new "Root" is in proper order with its children. Returns the patient
		 * which was removed or returns null if empty.
		 * 
		 * @param index, integer index position of the Patient to be removed.
		 */
		public Patient remove(int index) {
			if (isEmpty())
				return null;

			Patient p = heap[index];
			heap[index] = heap[size];
			size--;
			bubbleDown(1);
			return p;
		}

		/*
		 * Checks if the current Heap is empty and returns true if it is.
		 * 
		 * @return boolean, boolean representing if heap is empty or not.
		 */
		public boolean isEmpty() {
			return size == 0;
		}

		/*
		 * Returns size of the Heap in terms of number of Patients.
		 * 
		 * @return size, integer of number of patients in heap.
		 */
		public int size() {
			return size;
		}

		/*
		 * Resizes the heap dynamically so that when more room is needed, the size will
		 * double.
		 */
		public void resize() {
			Patient[] original = heap;
			Patient[] resized = Arrays.copyOf(original, heap.length * 2);
			this.heap = resized;
		}

		/*
		 * Returns a String representation of the heap in the format: {String name (int
		 * priority), ...}
		 * 
		 * @return toString, String representation of Patients in heap.
		 */
		public String toString() {
			String s = "{";

			for (int i = 1; i <= size; i++)
				s += (i == 1 ? "" : ", ") + heap[i];

			return s + "}";
		}

	}
}
