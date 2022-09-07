/*
* AUTHOR: Kyle Walker
* FILE: PA5Main.java
* ASSIGNMENT: Programming Assignment 5 - Garden
* COURSE: CSc 210; Fall 2021;
* PURPOSE: This program takes an input and creates a garden of plotted characters.
* Each plot is 5x5 dots by default but plants objects of three types can be planted.
* These are Vegetables, Trees, and Flowers, which each have their own classes and growth
* patterns. There are also multiple varieties of each, represented by their ASCII character
* on the garden output plot. There are many commands for interacting with the garden that
* are given through the input file including: planting a new plant, growing plants,
* removing plants, and printing the entire garden out. these commands include different
* forms which allow for control over specific plant varieties or locations in the garden.
* The Garden plot can be a maximum of 16x16 plots for a total of 80 chars wide.
* 
* USAGE: 
* java PA6Main infile 
*
* where infile is the name of an input file in the following format
*
* ----------- EXAMPLE INPUT -------------
* Input file:
* -------------------------------------------
* | rows: 6
* | cols: 14
* |
* | PLANT (0, 0) banana
* | PRINT
* | GROW 2
* | GROW 4 (1, 3)
* | GROW 1 vegetable
* | GROW 1 lily
* | HARVEST
* | HARVEST (2, 3)
* | HARVEST garlic
* | PICK 
* | PICK (4, 3) 
* | PICK rose
* | CUT 
* | CUT (5, 2) 
* | CUT pine 
* -------------------------------------------
*
* The commands shown in the file must follow this format to be accepted. The
* program is case insensitive, but spacing and ordering of the arguments
* for each command must follow this structure.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class PA5Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner file_input = new Scanner(System.in);
		try {
			// File file = new File(file_input.nextLine());
			File file = new File(args[0]);
			Scanner file_contents = new Scanner(file);

			String rows = file_contents.nextLine();
			String cols = file_contents.nextLine();
			int r = Integer.valueOf(rows.substring(6));
			int c = Integer.valueOf(cols.substring(6));
			Garden garden = new Garden(r, c);

			while (file_contents.hasNext()) {

				String[] cur_line = file_contents.nextLine().split(" ");
				cur_line[0] = (cur_line[0]).toUpperCase();
				String cur_command = (cur_line[0]);
				String output_string = new String();
				for (String e : cur_line) {
					output_string += e + " ";
				}

				int target_r = 0;
				int target_c = 0;
				if (cur_line.length > 1 && cur_line[1].contains("(")) {
					String[] coords = cur_line[1].split(",");
					target_r = Integer.valueOf(coords[0].substring(1));
					target_c = Integer.valueOf(coords[1].substring(0, coords[1].length() - 1));
				}

				// Compares command string to known cases
				if (cur_command.equals("PRINT")) {
					System.out.println("> PRINT");
					garden.print();
				} else if (cur_command.equals("GROW")) {
					System.out.println("> " + output_string + "\n");
					int grow_val = Integer.valueOf(cur_line[1]);
					if (cur_line.length == 2) { // Default grow
						garden.grow(grow_val);
					}

					else if (cur_line.length == 3) { // Class specific grow
						String plant_info = cur_line[2];
						garden.grow(grow_val, plant_info);

						if (plant_info.contains("(")) { // Location specific grow
							String[] coords = cur_line[2].split(",");
							target_r = Integer.valueOf(coords[0].substring(1));
							target_c = Integer.valueOf(coords[1].substring(0, coords[1].length() - 1));
							garden.grow(grow_val, target_r, target_c);
						}
					}
				}

				else if (cur_command.equals("PLANT")) {
					// takes row and column data from command
					String type = cur_line[2];
					garden.plant(target_r, target_c, type);
				}

				else if (cur_command.equals("PICK") || cur_command.equals("HARVEST") || cur_command.equals("CUT")) {
					System.out.println("> " + output_string + "\n");
					if (cur_line.length == 1) {
						garden.remove(cur_command);
					} else if (cur_line.length == 2) {
						if (cur_line[1].contains("(")) {
							garden.remove(cur_command, target_r, target_c);
						} else {
							String plant_type = cur_line[1];
							garden.remove(cur_command, plant_type);
						}
					}
				}
			}

			file_contents.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
