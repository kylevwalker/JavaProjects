/*
* AUTHOR: Kyle Walker
* FILE: Tree.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents the Tree subclass of Plant. Each Tree object 
* inherits all properties and plot shape of the Plant class. It has its own origin
* and growth pattern, however. It is designed to grow upward from the bottom center
* of the plot, and responds to the "Cut" command.
*/

public class Tree extends Plant {
	private static final int origin_r = 4;
	private static final int origin_c = 2;

	/*
	* This is the Constructor for Tree objects. It inherits the
	* plot of the Plant Base class and stores its variety type
	* based on the string argument provided. It stores the
	* origin point to determine the origin of growth, and uses the
	* first character of the type as the ASCII symbol in printing.
	* Upon creation, it will call it's own grow method with an index 
	* of 0 to create a single symbol character at the origin point.
	*
	* @param plant, string for the variety type of the Tree.
	*/ 
	public Tree(String plant) {
		this.center_r = 4;
		this.center_c = 2;
		this.type = plant;
		this.symbol = type.charAt(0);
		grow(grow_index);
	}

	/*
	* The grow method for the Tree has it's own algorithm to grow upwards
	* in a straight line. Based on the growth index, the 
	* method will determine how far the characters will spread from the origin.
	*
	* @param num, int representing increase in growth index, which tells the
	* 			function how far to grow.
	*/ 
	public void grow(int num) {
		if (grow_index < plot_size) {
			grow_index += num;
			for (int r = 0; r < plot_size; r++) {
				if (r >= (center_r - grow_index)) {
					plot.get(r).set(center_c, symbol);
				}
			}
		}
	}
}
