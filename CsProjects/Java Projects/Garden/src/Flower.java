/*
* AUTHOR: Kyle Walker
* FILE: Flower.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents the Flower subclass of Plant. Each flower object 
* inherits all properties and plot shape of the Plant class. It has its own origin
* and growth pattern, however. It is designed to bloom outward from the center
* in a diamond pattern, and responds to the "Pick" command.
*/
import java.util.ArrayList;

public class Flower extends Plant {
	private static final int origin_r = 2;
	private static final int origin_c = 2;

	/*
	* This is the Constructor for Flower objects. It inherits the
	* plot of the Plant Base class and stores its variety type
	* based on the string argument provided. It stores the
	* origin point to determine the origin of growth, and uses the
	* first character of the type as the ASCII symbol in printing.
	* Upon creation, it will call it's own grow method with an index 
	* of 0 to create a single symbol character at the origin point.
	*
	* @param plant, string for the variety type of the Flower.
	*/ 
	public Flower(String plant) {
		this.center_r = origin_r;
		this.center_c = origin_c;
		this.type = plant;
		this.symbol = type.charAt(0);
		grow(grow_index);
	}
	
	/*
	* The grow method for the flower has it's own algorithm to "bloom"
	* outwards in a diamond shape. Based on the growth index, the 
	* method will determine how far the characters will spread from the center.
	* The loops only cover half of the plot each, so the calculations are focused
	* on only the top left corner and all other coordinates are "mirrored".
	*
	* @param num, int representing increase in growth index, which tells the
	* 			function how far to grow.
	*/ 
	public void grow(int num) {
		if (grow_index < plot_size) {
			grow_index += num;
			for (int r = 0; r <= plot_size / 2; r++) {
				int offset_c = grow_index +r-1;
				if (r >= center_r - grow_index) {
					plot.get(r).set(center_c, symbol);
					plot.get(plot_size - r - 1).set(center_c, symbol);
				}
				if (grow_index > 0) {
					for(int c = 0; c<= plot_size/2; c++) {
						if(c > center_c - offset_c){
							plot.get(r).set(c, symbol);
							plot.get(r).set(plot_size - c-1, symbol);
							plot.get(plot_size - r -1).set(c, symbol);
							plot.get(plot_size - r -1).set(plot_size - c-1, symbol);
						}
					}
				}
				
			}
		}
	}

}
