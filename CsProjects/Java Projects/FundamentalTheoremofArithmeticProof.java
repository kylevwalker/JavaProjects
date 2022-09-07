/*
 * AUTHOR: Kyle Walker
 * ASSIGNMENT: CSC245Hw6 - Problem 16: FTOA Solver
 * COURSE: CSc 245; Spring 2022
 * DATE: 4/7/22; Due: 4/8/22
 * PURPOSE: This class takes a command line prompt for any integer and shows that it is either a prime number or a product of
 * two or more primes, as is expected from the Fundamental Theorem of Arithmetic. The program uses the recursive factor() method
 * in order to divide the given number into its prime factors. The recursive method will continue to factor until every factor
 * is proven to be prime or the product of primes, at which point the FTOA will be proven for the starting number. The method
 * keeps track of indentation to organize the output, and will display all factorization steps to prove the final result. 
 */

public class FundamentalTheoremofArithmeticProof {

	/*
	 * Main method that takes command line and calls factor method.
	 * 
	 * @param args, command line arguments containing the number to be factored. 
	 */
	public static void main(String[] args) {
		// Initializes starting number var based on command line input argument. If none exists, it will notify the user.
		int num = 0;
        if (args.length > 0) {
        	try {
        		num = Integer.parseInt(args[0]);
        		if (num <= 1)
        			throw new Exception("Incorrect command. Command must be an Integer greater than 1.");
                // Calls recursive factor() method starting at divisor of 2 (smallest prime) with indentation of 0 units.
        		 System.out.println("This program will demonstrate that " + num + " is either prime or is the product of two or more prime numbers. \n");
                factor(num, 2, 0);
                // After recursion is finished, concludes the proof.
                System.out.println();
                System.out.println("As the output shows, the Fundamental Theorem of Arithmetic holds for " + num + ".");
        	}
            catch(Exception e) {
            	System.out.println("Incorrect command. Command must be an Integer greater than 1.");
            }
           
            } 
        else {
            System.out.println("Usage: java CmdLine <arguments>");
        }
        


	}
	
	/*
	 * The factor() method recursively finds all factors of a given number and displays the prime factorization. It keeps track of
	 * indentation for an organized output and prints comments explaining each step of factorization.
	 * 
	 * @param num, int keeping track of the current number to be factored. Starts with the command line number, but passes
	 * 			factors during recursion.
	 * @param divisor, int of the other factor of the number. Starts with 2, the smallest prime but changes as new factors are found.
	 * @param indent, int keeping track of how many indentations to add to current depth of recursion. Starts with 0, but increases as
	 * 			recursive depth increases to organize output. 
	 */
	public static void factor(int num, int divisor, int indent) {
		// Creates a new output string and adds current indentation to the output string.
		String output = new String();
		for (int i = 0; i < indent; i++) {
			output += "    ";
		}
		
		// Base case where factors are found. It will check if the divisor is prime to return and continue recursion.
		if(num % divisor == 0) {
    		int newNum = num/divisor;
    		
    		if (newNum == 1) {
    			output += (divisor + " is prime.");
    			System.out.println(output);
    			return;
    		}
    		
    		// If the two factors are equal, the output will be changed to show the number is a square. Otherwise, shows two factors
    		// and asks if they are prime.
    		if (newNum == divisor)
    			output += (num + " = " + newNum + " squared; are these factors either prime or products of primes?");
    		else
    			output += (num + " = " + newNum + " * " + divisor + "; are these factors either prime or products of primes?");
    		System.out.println(output);
    		
    		// Finds factorization of first factor.
    		factor(newNum, 2, indent + 1);
    		if (newNum != divisor)
    			factor(divisor, 2, indent + 1);
    		
    		// If the two factors are equal, the output will be changed to show the number is a square of primes. Otherwise, shows that
    		// both factors are prime or products of primes. The output string is reset to prevent duplicates.
    		output = new String();
    		for (int i = 0; i < indent; i++) {
    			output += "    ";
    		}
    		
    		if (newNum == divisor)
    			output += (num + " is the square of " + newNum + ", which is prime or the product of primes.");
    		else
    			output += (num + " is the product of primes (" + newNum + " and " + divisor + " are prime or prime products).");
    		System.out.println(output);
		}
		
		// If no factors are found with current divisor, it will recurse using next possible divisor. 
		else {
			factor(num, divisor + 1, indent);	
		}
	}
}
