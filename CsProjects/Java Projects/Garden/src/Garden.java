/*
* AUTHOR: Kyle Walker
* FILE: Garden.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents the entire Garden 2d Array, which consists of up to 16 rows and columns
* of Objects inheriting the Plant Class. The garden originally constructs itself entirely of empty
* Plant class objects, but through the plant commands new Vegetable, Tree, and Flower objects will replace
* the empty plots. The Garden class serves as the direct method caller for the commands given in Main.
* All main commands are translated into Garden methods, which then call Plant methods based on their
* position within the Garden's 2D array.
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Garden {
	private ArrayList<ArrayList<Plant>> garden_array = new ArrayList<ArrayList<Plant>>();
	private int rows;
	private int cols;
	private static final int plot_size = 5;
	// Classifies plant types by name to distinguish constructors
	private Set<String> flower_types = new HashSet<>(
			Arrays.asList("iris", "lily", "rose", "daisy", "tulip", "sunflower"));
	private Set<String> vegetable_types = new HashSet<>(
			Arrays.asList("garlic", "zucchini", "tomato", "yam", "lettuce"));
	private Set<String> tree_types = new HashSet<>(Arrays.asList("oak", "willow", "banana", "coconut", "pine"));
	
	/*
	* This is the Constructor for the Garden. It takes the first two lines
	* of the in file as parameters for the row and column count, then
	* creates the garden_array 2D Array with new empty Plant objects in
	* each spot.
	*
	* @param r, int number of rows for garden array
	* @param c, int number of columns for garden array
	*/ 
	public Garden(int r, int c) {
		this.rows = r;
		this.cols = c;
		if (r > 16 || c > 16) {
			System.out.println("Too many plot columns.\n");
			System.exit(0);
		}
		for (int i = 0; i < r; i++) {
			ArrayList<Plant> cur_row = new ArrayList<Plant>();
			for (int j = 0; j < c; j++) {
				cur_row.add(new Plant());
			}
			garden_array.add(cur_row);
		}
	}
	
	/*
	* This is the main garden print function that converts the contents of
	* the garden plot 2D array to a string, which is printed in standard output.
	*/ 
	public void print() {
		for (int r = 0; r < rows; r++) {
			for (int p = 0; p < plot_size; p++) {
				String cur_line = new String();
				for (int c = 0; c < cols; c++) {
					cur_line += garden_array.get(r).get(c).getPlot(p);
				}
				System.out.println(cur_line);
			}
		}
		System.out.println();
	}

	/*
	* The plant method uses the garden's plant type sets to compare the
	* type parameter to all possible plant types, then categorizes the plant
	* by its class type. This means based on the type parameter, the
	* plant function will construct a new Flower, Tree, or Vegetable 
	* object at the location given by the r and c parameters.
	*
	* @param r, int index of target row
	* @param c, int index of target column
	* @param type, type or variety of Plant object
	*/ 
	public void plant(int r, int c, String type) {
		type = type.toLowerCase();
		if (r < rows && c < cols && garden_array.get(r).get(c).getClass() == Plant.class) {
			if (flower_types.contains(type)) {
				garden_array.get(r).set(c, new Flower(type));
			} else if (vegetable_types.contains(type)) {
				garden_array.get(r).set(c, new Vegetable(type));
			} else if (tree_types.contains(type)) {
				garden_array.get(r).set(c, new Tree(type));
			}
		}
	}

	/*
	* This is one signature of the grow method, which grows all plants in the
	* garden by the num parameter. It iterates through the entire garden plot
	* and calls the grow method for every Plant object detected, not including the
	* empty plant plots.
	*
	* @param num, int number of times growth index will increase for each
	* plant.
	*/ 
	public void grow(int num) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				garden_array.get(r).get(c).grow(num);
			}
		}
	}

	/*
	* This is another signature of the grow method, which grows all plants of the
	* given type by the num parameter. It iterates through the entire garden plot
	* and calls the grow method for every Plant object which has the same variety 
	* type value. If the name of the plant's class is passed for type, then the
	* method will detect this and instead grow every plant of the class type
	* given. Because both the variety type and class name can be provided as 
	* a string from Main, both functions are combined in this one method.
	*
	* @param num, int number of times growth index will increase for each
	* plant.
	* @param type, string representing target variety type of plant. However,
	* 				can also act as class type if the String is "Flower", "Tree",
	* 				or "Vegetable".
	*/ 
	public void grow(int num, String type) {
		type = type.toLowerCase();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				// Checks if type parameter is meant to represent class type, then grows class type
				if (type.equals("flower") || type.equals("tree") || type.equals("vegetable")) {
					String cur_class = garden_array.get(r).get(c).getClass().toString().toLowerCase().substring(6);
					if (cur_class.equals(type)) {
						garden_array.get(r).get(c).grow(num);
					}
				// Checks if type parameter is meant to represent variety type, then grows plants of variety type
				} else {
					String cur_type = garden_array.get(r).get(c).type;
					if (cur_type != null && cur_type.toLowerCase().equals(type)) {
						garden_array.get(r).get(c).grow(num);
					}
				}
			}
		}
	}

	/*
	* This is another signature of the grow method, which grows the specific plant
	* at the given location in the garden by the num parameter. It gets the plant at 
	* the given location in the garden array, and calls grow method if a plant
	* exists at the location and the location is within the garden bounds.
	* If no plant exists or the location is outside of the garden's array
	* indexes, an error message will be printed.
	*
	* @param num, int number of times growth index will increase for each
	* plant.
	* @param r, int index of target row
	* @param c, int index of target column
	*/ 
	public void grow(int num, int row, int col) {
		if (row < rows && col < cols && garden_array.get(row).get(col) != null) {
			garden_array.get(row).get(col).grow(num);
		} else {
			System.out.println("Can't grow there.\n");
		}
	}
	
	/*
	* This is one signature of the remove method, which translates the Harvest, Cut, and 
	* Pick commands into one function. It iterates through the entire garden plot
	* and replaces the target plant objects with a newly constructed blank plant.
	*
	* @param operation, string representing the command passed through Main. Each
	* 					operation targets different plant class types, so
	* 					only plants of the same class will be removed. ex: "Harvest",
	*/ 
	public void remove(String operation) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Class<?> cur_class = garden_array.get(r).get(c).getClass();
				if (operation.equals("CUT")) {
					if (cur_class == Tree.class) {
						garden_array.get(r).set(c, new Plant());
					}
				} else if (operation.equals("HARVEST")) {
					if (cur_class == Vegetable.class) {
						garden_array.get(r).set(c, new Plant());
					}
				} else if (operation.equals("PICK")) {
					if (cur_class == Flower.class) {
						garden_array.get(r).set(c, new Plant());
					}
				}
			}
		}
	}

	/*
	* This is another signature of the remove method, which translates the Harvest, Cut, and 
	* Pick commands into one function. It iterates through the entire garden plot
	* and searches for plants with the same class and variety types. It will then replace
	* these plants with newly constructed empty Plant objects.
	*
	* @param operation, string representing the command passed through Main. Each
	* 					operation targets different plant class types, so
	* 					only plants of the same class will be removed. ex: "Harvest".
	* @param type, string representing the target variety type of the plants, 
	* 				ex: "zucchini".
	*/ 
	public void remove(String operation, String type) {
		type = type.toLowerCase();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String cur_type = garden_array.get(r).get(c).type;
				Class<?> cur_class = garden_array.get(r).get(c).getClass();
				if (cur_type != null) {
					if (operation.equals("CUT")) {
						if (cur_type.toLowerCase().equals(type) && cur_class == Tree.class) {
							garden_array.get(r).set(c, new Plant());
						}
					} else if (operation.equals("HARVEST")) {
						if (cur_type.toLowerCase().equals(type) && cur_class == Vegetable.class) {
							garden_array.get(r).set(c, new Plant());
						}
					} else if (operation.equals("PICK")) {
						if (cur_type.toLowerCase().equals(type) && cur_class == Flower.class) {
							garden_array.get(r).set(c, new Plant());
						}
					}
				}
			}
		}
	}

	/*
	* This is another signature of the remove method, which translates the Harvest, Cut, and 
	* Pick commands into one function. It finds the target plant based on the row and
	* column indexes of the garden array provided as arguments. It will then replace
	* these plants with newly constructed empty Plant objects if the class type is correct.
	*
	* @param operation, string representing the command passed through Main. Each
	* 					operation targets different plant class types, so
	* 					only plants of the same class will be removed. ex: "Harvest".
	* 
	* @param r, int index of target row
	* @param c, int index of target column
	*/ 
	public void remove(String operation, int row, int col) {
		Class<?> cur_class = garden_array.get(row).get(col).getClass();
		if (operation.equals("CUT")) {
			if (cur_class == Tree.class) {
				garden_array.get(row).set(col, new Plant());
			} else {
				System.out.println("Can't cut there.\n");
			}
		} else if (operation.equals("HARVEST")) {
			if (cur_class == Vegetable.class) {
				garden_array.get(row).set(col, new Plant());
			} else {
				System.out.println("Can't harvest there.\n");
			}
		} else if (operation.equals("PICK")) {
			if (cur_class == Flower.class) {
				garden_array.get(row).set(col, new Plant());
			} else {
				System.out.println("Can't pick there.\n");
			}
		}
	}
}
