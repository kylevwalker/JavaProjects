/*
 *  Name: Kyle Walker
 *  Course: Csc 245 Fall 2021
 *  Assignment: HomeWork 6 #15: Magic Square
 *  Instructor: Lester McCann
 *  Due Date: 11/12/21
 *  
 *  Program Description: This program takes an input of 9 integers from the command line
 *  and runs it through an algorithm which calculates each next permutation in Lexicographical
 *  order, based on the example pseudocode on page 459 of the Rosen textbook. This version
 *  is tweaked so that the algorithm loops through every permutation and "wraps" back to the
 *  user's starting sequence. For each calculated permutation, the program also checks if
 *  these numbers represent a proper magic square. If each row, column, and diagonal adds
 *  up to 15 using only the digits from 1 to 9, then it will print a visual representation
 *  of that magic square. While the command line input sequence is a string, every permutation
 *  is stored as an integer array and both algorithms rely on looping through the indexes
 *  for calculations.
 * 
 * Operational Requirements: Java 8, input comes from command line and is read with an imported 
 * Java Scanner utility.
 * 
 * Required Features: While the nextPermutation algorithm can operate on a permutation string of
 * any length, the magicSquareTest algorithm only works with a proper 3 x 3 magic square
 * sequence from 1-9. It also assumes the user does not reuse integers or type in more or less 
 * than 9 values, each with a space between them.
 */

import java.util.Scanner;


public class MagicSquareProof {
	private static final int MAGIC_C = 15; 		// Constant value for 3x3 magic square sums

	/*
	 * This method takes an input permutation array and runs the algorithm which finds all
	 * possible consecutive permutations in Lexicographical order. It will start from
	 * the user defined point passed in as the argument, and will "wrap" all the way back
	 * to this point after finding the largest possible permutation. Each resulting
	 * permutation is passed as the argument for the magicalSquareCheck.
	 * 
	 * @param permutation, integer array representation of the user's permutation string,
	 * 						created within Main().
	 */
	public void nextPermutation(int[] permutation) {
		boolean completed = false;
		while (!completed) {		// Runs algorithm until largest permutation is found

			int j = permutation.length - 1;
			while (j > 0 && permutation[j] <= permutation[j - 1]) {	
				j--;
			}
			if (j <= 0) {		// Stops algorithm when largest permutation is found (aka all j <= j-1)
				completed = true;
				break;
			}

			int k = permutation.length - 1;
			while (permutation[k] <= permutation[j - 1]) {
				k--;
			}
			int prevK = permutation[j - 1];
			permutation[j - 1] = permutation[k];
			permutation[k] = prevK;		// replaces aj and ak.
			int r = permutation.length - 1;
			while (r > 0 && r > j) {
				int prevS = permutation[j];
				permutation[j] = permutation[r];
				permutation[r] = prevS;	// replaces aj and ar.
				r--;
				j++;
			}
			
			magicSquareTest(permutation);	// Checks if current permutation is a Magic Square 
//
		}
	}
	
	/*
	 * This method takes an input permutation array and runs the algorithm which checks
	 * that all rows, columns, and diagonals add up to the Magic Constant 15. If these
	 * conditions are met, the grid representation of the magic square will be printed out
	 * onto the user console. The algorithm is based on a single dimension indexing of 
	 * the permutation array.
	 * @param permutation, integer array representation of the current largest permutation
	 * 						passed in from the nextPermutation function.
	 */
	public void magicSquareTest(int[] permutation) {
		boolean isMagic = false;	// Boolean that keeps track of Magic Square condition
		if (permutation[4] == 5) {
			isMagic = true;
			
			for (int i = 0; i < 9; i += 3) {	// Checks if each row adds up to 15
				int rowSum = 0;
				for (int j = 0; j < 3; j++) {
					rowSum += permutation[i + j];
				}
				if(rowSum != MAGIC_C)
					isMagic = false;
			}
			
			for (int i = 0; i < 3; i++) {	// Checks if each column adds up to 15
				int colSum = 0;
				for (int j = 0; j < 9; j += 3) {
					colSum += permutation[i + j];
				}
				if(colSum != MAGIC_C)
					isMagic = false;
			}
			int dSum1 = permutation[0] + permutation[4] + permutation[8];	// Checks if each diagonal adds up to 15
			int dSum2 = permutation[2] + permutation[4] + permutation[6];
			if(dSum1 != MAGIC_C || dSum2 != MAGIC_C)
				isMagic = false;
			
		}
		if (isMagic) {		// Prints grid representation of correct Magic Squares.
			System.out.println("| " + permutation[0] + " | " + permutation[1] + " | " + permutation[2] + " |"); 
			System.out.println("| " + permutation[3] + " | " + permutation[4] + " | " + permutation[5] + " |"); 
			System.out.println("| " + permutation[6] + " | " + permutation[7] + " | " + permutation[8] + " |\n"); 
		}
	}

	public static void main(String[] args) {
		
		
		MagicSquareProof magicSquareFinder = new MagicSquareProof();		// Creates new class to run  algorithm
		String[] permutationData = new String[9];
        if (args.length > 0) {						// Stores CommandLine info
            for (int i=0; i<args.length; i++) {
            	permutationData[i] = args[i];
            }
        }
		int[] permutation = new int[permutationData.length];
		for (int i = 0; i < permutationData.length; i++) {		//converts string array into Int array
			permutation[i] = Integer.valueOf(permutationData[i]);
		}
		magicSquareFinder.nextPermutation(permutation);

	}

}
