/*
* AUTHOR: Kyle Walker
* FILE: Vegetable.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents the Vegetable subclass of Plant. Each Vegetable object 
* inherits all properties and plot shape of the Plant class. It has its own origin
* and growth pattern, however. It is designed grow from the top center of the plot,
* and responds to the "Pick" command.
*/
public class Vegetable extends Plant {
	private static final int origin_r = 0;
	private static final int origin_c = 2;

	/*
	* This is the Constructor for Vegetable objects. It inherits the
	* plot of the Plant Base class and stores its variety type
	* based on the string argument provided. It stores the
	* origin point to determine the origin of growth, and uses the
	* first character of the type as the ASCII symbol in printing.
	* Upon creation, it will call it's own grow method with an index 
	* of 0 to create a single symbol character at the origin point.
	*
	* @param plant, string for the variety type of the Vegetable.
	*/ 
	public Vegetable(String plant) {
		this.center_r = origin_r;
		this.center_c = origin_c;
		this.type = plant;
		this.symbol = type.charAt(0);
		grow(grow_index);
	}

	/*
	* The grow method for the Vegetable has it's own algorithm to grow downwards
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
				if (r <= (center_r + grow_index)) {
					plot.get(r).set(center_c, symbol);
				}
			}
		}
	}
}
