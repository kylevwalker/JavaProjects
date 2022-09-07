/*
 * AUTHOR: Kyle Walker
 * FILE: PA12Main.java
 * ASSIGNMENT: Programming Assignment 12 - Anagrams
 * COURSE: CSc 210; Fall 2021
 * PURPOSE: This class serves as a whole program capable of taking command line inputs and finding all 
 * possible anagrams of a given word through the input dictionary file. There is a also the ability to
 * control the max number of words to be combined when finding the anagrams, which is also passed as a 
 * command line input. This class holds the main function which creates a dictionary from the input file 
 * and calls the recursive AnagramSOlver function from the inner AnagramSolver class. It will print info 
 * to the console about the Phrase being scrambled, all possible words in the dictionary that are contained
 * in the phrase, and finally all possible anagrams lists. The AnagramSolver inner class uses recursive
 * backtracking to find all possible combinations of words that will produce a complete anagram of the 
 * chosen word or phrase.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class PA12Main {
	/*
	 * Main method which stores command line inputs as variables, and instantiates an AnagramSolver to 
	 * find all possible anagrams. Stores Strings from the dictionary file in an Array, and passes the
	 * input phrase and max as arguments in the AnagramSolver constructor.
	 * 
	 * @param args, String[] of command line arguments.
	 */
	public static void main(String[] args) {
		Scanner file = null;
		try {
			file = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// convert dictionary file contents into an ArrayList
		List<String> dictionary = new ArrayList<String>();
		while (file.hasNextLine()) {
			dictionary.add(file.nextLine());
		}

		String text = new String(args[1]);
		int max = Integer.valueOf(args[2]);

		// Makes an outer Main class to allow instance of Anagram Solver inner class
		PA12Main helper = new PA12Main();
		AnagramSolver solver = helper.new AnagramSolver(text, max, dictionary);
		List<String> choices = solver.containedWords();

		// Console output
		System.out.println("Phrase to scramble: " + text + "\n");
		System.out.println("All words found in " + text + ":");
		System.out.println(choices + "\n");
		System.out.println("Anagrams for " + text + ":");
		solver.printAll();

	}

	/*
	 * The AnagramSolver is an inner class which takes parameters from main and uses recursive backtracking
	 * to solve and print out all possible anagram solutions. 
	 * 
	 * It has a constructor that accepts arguments from main, the containedWords method that creates a List of 
	 * acceptable choices from the dictionary, the printAll helper function that calls the backtracking method,
	 * and the recursive backtracking method itself called findAnagrams.
	 */
	class AnagramSolver {
		String phrase = new String();
		LetterInventory letters = new LetterInventory(phrase);
		int max = 0;
		List<String> dict = new ArrayList<String>();
		List<String> choices = new ArrayList<String>();

		/*
		 * AnagramSolver constructor that takes arguments from main and stores them as properties of the class.
		 * Stores the input phrase, the max target, dictionary string, and creates a LetterInventory of the
		 * input phrase.
		 * 
		 * @param text, String of target phrase from main.
		 * @param max, int for the maximum number of words used in creating anagram solutions.
		 * @param dict, List<String> of dictionary file entries to store all dictionary words.
		 */
		public AnagramSolver(String text, int max, List<String> dict) {
			this.phrase = text;
			this.max = max;
			this.dict = dict;
			this.letters = new LetterInventory(phrase);
		}

		/*
		 * Returns a List of all Strings that can be used in forming anagrams of the target word. Returns a list
		 * representing all possible choices for creating anagrams. 
		 * 
		 * @return choices, List<String> of all possible anagram part choices.
		 */
		public List<String> containedWords() {
			for (String word : this.dict) {
				if (letters.contains(word)) {
					choices.add(word);
				}
			}
			return choices;
		}

		/*
		 * Helper function for the recursive backtracking method. Creates a new empty stack which will be
		 * passed through the findAnagrams function in order to store solution paths.
		 * 
		 */
		public void printAll() {
			Stack<String> stack = new Stack<String>();
			findAnagrams(letters, max, stack);
		}

		/*
		 * The recursive backtracking method used in finding all anagram solution paths. It will print out 
		 * the solution stack every time a proper anagram is found, and will continue to search through every
		 * possible path to print every possible combination. It will ensure that the anagrams follow the 
		 * maximum number of entries.
		 * 
		 * @param letters, LetterInventory of current remaining letters from the target phrase. It begins
		 * 					as the original phrase, but through each recursive call it passes the modified
		 * 					remaining letters.
		 * @param max, int of the command line defined maximum length of any given anagram solution stack.
		 * @param stack, Stack<String> of current solution path. Records which words are currently chosen,
		 * 					and will be printed as the solution when an anagram is found.
		 */
		public void findAnagrams(LetterInventory letters, int max, Stack<String> stack) {
			// Possible solution may be found when letters are empty
			if (letters.isEmpty()) {
				// A new LetterInventory of the anagram solution is created for comparison with original phrase
				LetterInventory anagram = new LetterInventory("");
				for (String e : stack) {
					anagram.add(e);
				}
				if (anagram.equals(this.letters)) {
					System.out.println(stack);
					return;
				}
			}

			LetterInventory newLetters = new LetterInventory(letters);
			if (max == 0 || max > stack.size()) {
				for (String word : this.choices) {
					LetterInventory curWord = new LetterInventory(word);

					if (newLetters.contains(curWord)) {
						newLetters.subtract(curWord);

						stack.push(word);
						findAnagrams(newLetters, max, stack);
						newLetters.add(word);
						stack.pop();

					}
				}
			}
		}
	}
}
