
import java.util.*;
 
class Main {   
    public static void main(String args[])
    {
        PriorityQueue<Integer> pQueue = new PriorityQueue<Integer>();
 
        pQueue.add(10);
        pQueue.add(20);
        pQueue.add(15);
 
        // Print the top element of PriorityQueue
        System.out.println(pQueue.peek()); 
        // Print the top element and removing it
        System.out.println(pQueue.poll()); 
        // Print the top element again
        System.out.println(pQueue.peek());
                
        PriorityQueue<String> namePriorityQueue = new PriorityQueue<>();

        // Add items to a Priority Queue (ENQUEUE)
        namePriorityQueue.add("Lisa");
        namePriorityQueue.add("Robert");
        namePriorityQueue.add("Joseph");

        // Remove items from the Priority Queue (DEQUEUE)
        while (!namePriorityQueue.isEmpty())
            System.out.println(namePriorityQueue.remove());
        
        //-----------------------------------------------------------------------
        
        Comparator<String> strlenComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        };

        // Create a Priority Queue with a custom Comparator
        PriorityQueue<String> cnamePriorityQueue = new PriorityQueue<>(strlenComparator);

        // Add items to a Priority Queue (ENQUEUE)
        cnamePriorityQueue.add("Lisa");
        cnamePriorityQueue.add("Robert");
        cnamePriorityQueue.add("Joe");

        // Remove items from the Priority Queue (DEQUEUE)
        while (!cnamePriorityQueue.isEmpty())
            System.out.println(cnamePriorityQueue.remove());

        //-----------------------------------------------------------------------
        /* For a PriorityQueue of user defined objects:
        1. the class should implement the Comparable interface and provide compareTo(), or
        2. a custom Comparator should be provided when creating the PriorityQueue. */
        
        PriorityQueue<Employee> employeePriorityQueue = new PriorityQueue<>();

        // Add items to the Priority Queue
        employeePriorityQueue.add(new Employee("Jan", 100000.00));
        employeePriorityQueue.add(new Employee("Dan", 145000.00));
        employeePriorityQueue.add(new Employee("Lisa", 115000.00));

        // compareTo() in Employee determines the order the objects are dequeued
        while (!employeePriorityQueue.isEmpty())
            System.out.println(employeePriorityQueue.remove());        
    }
}



class Employee implements Comparable<Employee> {
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 &&
                Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    // Compare two employee objects by their salary
    @Override
    public int compareTo(Employee employee) {
        if(this.getSalary() > employee.getSalary()) {
            return 1;
        } else if (this.getSalary() < employee.getSalary()) {
            return -1;
        } else {
            return 0;
        }
    }
}

