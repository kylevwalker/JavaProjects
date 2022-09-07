/*
* AUTHOR: Kyle Walker
* FILE: WordSearch.java
* ASSIGNMENT: Programming Assignment 1 - Word Search
* COURSE: CSc 210; Fall 2021
* PURPOSE: This program takes a dictionary file and a word search grid file
* and searches the four main cardinal directions for words in the grid. These words
* will be stored in a found words list, then will be printed out in the order in
* which they are found: Left to right, right to left, down, then up.
*
* USAGE: 
* 
* dictionary.txt
* grid.txt
*
*
*
** ----------- EXAMPLE INPUT -------------
* Input file: dictionary.txt
* -------------------------------------------
* Aarhus
* Aaron
* Ababa
* aback
* abaft
* -------------------------------------------
* ----------- EXAMPLE INPUT -------------
* Input file: grid.txt
* -------------------------------------------
* 6
* 6
* y c o d e j
* h s e y p k
* l p h b w a
* l o b w x z
* w o b a a i
* p l y y c g
* -------------------------------------------
*
* The first argument of main is the dictionary file, which contains thousands
* of possible words for the search algorithm to search through for comparisons.
* The next input file is a grid of any size, where the first two lines give 
* the number of rows and columns respectively. The main function will
* automatically remove any capital letters and excess characters to make the
* search consistent.
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class WordSearch 
{
	// Constant minimum word length requirement for indexing
	private static final int MIN_LENGTH = 3;

	public static void main(String[] args)
	{
		// Try to open dictionary file
				Scanner dictFile = null;
				try {dictFile = new Scanner (new File(args[0]));}
				catch (FileNotFoundException e) {e.printStackTrace();}
				// Try to open grid file
				Scanner gridFile = null;
				try {gridFile = new Scanner (new File(args[1]));}
				catch (FileNotFoundException e) {e.printStackTrace();}
				// Append all dictionary words to dictList
				List<String> dictList = new ArrayList<String>();
				while (dictFile.hasNext()) 
					dictList.add(dictFile.nextLine().trim().toLowerCase());
				dictFile.close();
				// Create 2D Array from grid file
				int gridRows = Integer.valueOf(gridFile.nextLine());
				int gridCols = Integer.valueOf(gridFile.nextLine());
				char [][] grid = new char[gridRows][gridCols];
				for (int r = 0; r < gridRows; r++) {
					String curLine = gridFile.nextLine().trim().toLowerCase();
					String[] letters = curLine.split(" ");
					for (int c = 0; c < gridCols; c++) {
						grid[r][c] = letters[c].charAt(0);}}
				gridFile.close();	
				// Run through search functions and add found word to list
				List<String> foundWords = new ArrayList<String>();
				searchLeftRight(grid, dictList, foundWords);
				searchRightLeft(grid, dictList, foundWords);
				searchUpDown(grid, dictList, foundWords);
				searchDownUp(grid, dictList, foundWords);
	}
		

	/*
	* A method that searches from left to right through each row. The contents of
	* the whole row will be passed through the checkWord method to find dictionary 
	* matches.
	*
	* @param grid: The 2D array representing the word search input file contents.
	* @param dictList: The list containing all words from the dictionary input file.
	* @param foundWords: The list containing all found words.
	* 
	* @return None
	*/ 
	public static void searchLeftRight(char[][] grid, List<String> dictList,  List<String> foundWords) 
	{
		for (int r = 0; r < grid.length; r++) {
			checkWord(new String(grid[r]), dictList, foundWords);}
	}
	
	/*
	* A method that searches from right to left through each row. The contents of
	* the whole reversed row will be passed through the checkWord method to find 
	* dictionary matches.
	*
	* @param grid: The 2D array representing the word search input file contents.
	* @param dictList: The list containing all words from the dictionary input file.
	* @param foundWords: The list containing all found words.
	* 
	* @return None
	*/ 
	public static void searchRightLeft(char[][] grid, List<String> dictList,  List<String> foundWords) 
	{
		for (int r = 0; r < grid.length; r++) {
			String rowReverse = "";
			for (int c = grid[r].length - 1; c >= 0; c--){
				rowReverse += grid[r][c];}
			checkWord(rowReverse, dictList, foundWords);}
	}
	
	/*
	* A method that searches from top to bottom through each column. The contents of
	* the whole column will be passed through the checkWord method to find dictionary 
	* matches.
	*
	* @param grid: The 2D array representing the word search input file contents.
	* @param dictList: The list containing all words from the dictionary input file.
	* @param foundWords: The list containing all found words.
	* 
	* @return None
	*/ 
	public static void searchUpDown(char[][] grid, List<String> dictList,  List<String> foundWords) 
	{
		for (int c = 0; c < grid[0].length; c++) {
			String columnDown = "";
			for (int r = 0; r < grid.length; r++) {
				columnDown += grid[r][c];}
			checkWord(columnDown, dictList, foundWords);}
	}
	
	/*
	* A method that searches from bottom to top through each column. The contents of
	* the whole reversed column will be passed through the checkWord method to find 
	* dictionary matches.
	*
	* @param grid: The 2D array representing the word search input file contents.
	* @param dictList: The list containing all words from the dictionary input file.
	* @param foundWords: The list containing all found words.
	* 
	* @return None
	*/ 
	public static void searchDownUp(char[][] grid, List<String> dictList,  List<String> foundWords) 
	{
		for (int c = 0; c < grid[0].length; c++) {
			String columnUp = "";
			for (int r = grid.length - 1; r >= 0; r--) {
				columnUp += grid[r][c];}
			checkWord(columnUp, dictList, foundWords);}
	}
	
	/*
	* The main algorithm method used in searching the dictionary for matches with
	* possible word strings. Based on the string passed in from the helper 
	* methods, this method will index through the string while creating strings
	* with the minimum word length requirement of at least 3 characters. If 
	* a word is found in the dictionary, it will be stored in the foundWord list
	* and printed out to the output console.
	*
	* @param string: The string containing the row/column contents from one of the 
	* 	four directions.
	* @param dictList: The list containing all words from the dictionary input file.
	* @param foundWords: The list containing all found words.
	* 
	* @return None
	*/ 
	public static void checkWord(String string, List<String> dictList, List<String> foundWords) 
	{
		//System.out.println("checking string: " + string);
		
		for (int i = 0; i <= string.length() - MIN_LENGTH; i++) {
			for (int l = MIN_LENGTH; i + l <= string.length(); l++) {
				String possibleString = string.substring(i, i + l);
				//System.out.println(possibleString);
				if (dictList.contains(possibleString)) {
					System.out.print(possibleString);
					System.out.println();
					foundWords.add(possibleString);}}}
	}
}	
	

