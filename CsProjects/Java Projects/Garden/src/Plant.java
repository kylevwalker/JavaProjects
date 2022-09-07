/*
* AUTHOR: Kyle Walker
* FILE: Plant.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents the base class of all plants in the garden. While
* the Flower, Tree, and Vegetable subclasses all inherit the functionality of the
* Plant class, the Plant class is able to stand on its own and is used to create empty
* plots within the garden array. By default, each Plant has a 5x5 array of '.' symbols,
* a method for returning the plot as a string, and a grow method which records the 
* grow amount for the specific plant.
*/
import java.util.ArrayList;

public class Plant {
	protected ArrayList<ArrayList<Character>> plot = new ArrayList<ArrayList<Character>>();
	protected String type;
	protected char symbol;
	protected int grow_index = 0;
	protected int center_r;
	protected int center_c;
	protected static final int plot_size = 5;

	/*
	* This is the Constructor for the Plant. It will create the default
	* 5x5 2D array plot of period characters to represent an empty plot.
	* This is stored as the plot property of the Plant.
	*/ 
	public Plant() {
		for (int i = 0; i < plot_size; i++) {
			ArrayList<Character> cur_row = new ArrayList<Character>();
			for (int j = 0; j < plot_size; j++) {
				cur_row.add('.');
			}
			plot.add(cur_row);
		}
	}

	/*
	* The getPlot method is the getter for each line of the Plant's plot,
	* which is called in the Garden's print function to get each line of
	* each plant's plot. The index scales with each line that is printed,
	* iterating from 0-4 for each plant that is printed.
	*
	* @param index, int index of row to be returned for printing
	*/ 
	public String getPlot(int index) {
		String row_contents = new String();
		for (char e : plot.get(index)) {
			row_contents += e;
		}
		return row_contents;
	}

	/*
	* The grow method increases the stored grow_index property of the plant.
	*
	* @param index, int number of times to grow the plant
	*/ 
	public void grow(int num) {
		grow_index += num;
	}

}
