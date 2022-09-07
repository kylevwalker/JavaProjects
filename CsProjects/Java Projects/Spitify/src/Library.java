import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

/*
* AUTHOR: Kyle Walker
* FILE: Library.java
* ASSIGNMENT: Programming Assignment 4 - Spitify
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents an entire library of songs to be played by the user. In the main function, 
* the songs will be extracted from an input file and the songs will be put into the library, contained in
* the library_songs array list variable. The library will be sorted in alphabetical order and show all user 
* songs added.
*
*/
public class Library {

	private ArrayList<Song> library_songs;
	
	/*
	* Library class constructor. When called, a new instance of Library will be created and it will contain
	* a new array list of all songs in the library.
	*/
	public Library() {
		library_songs = new ArrayList<Song>();
	}
	
	/*
	* A getter method for retrieving a specific song from the libray given the song name as an arg.
	*
	* @param title, String representing the title of the song being searched for.
	* @return Song, the Song object containing the same title as the title parameter. If song does not
	* 	exist in the library, returns null.
	*/
	public Song getSong(String title) {
		  for (int i = 0; i < library_songs.size(); i++) {
			  if (library_songs.get(i).getTitle().equals(title)) {
				  return library_songs.get(i);
		        	}
		        }
		    return null;
		}
	
	/*
	* A method for adding a specific song to the library given the Song object passed as an argument. Maintains
	* alphabetical ordering of songs.
	*
	* @param song, Song object to be added to library list.
	*/
	public void addSong(Song song) {
		library_songs.add(song);
		Collections.sort(library_songs);
	}
	
	/*
	* A method for removing a given song from the song library list. Checks if the given song exists in the
	* library, and removes the Song if it is found.
	*
	* @param song, Song object to be removed from the library list.
	*/
	public void removeSong(Song song) {
		  for (int i = 0; i < library_songs.size(); i++) {
			  if (library_songs.get(i).equals(song)) {
				  library_songs.remove(i);
		        	}
		        }
	}
	
	/*
	* A method for returning the entirety of the library contents as an ordered string,
	* where each song is separated by a line break.
	*
	* @return library_contents, the new string containing ordered library contents.
	*/
	public String toString() {
		String library_contents = new String();
		for (int i = 0; i < library_songs.size(); i++) {
		    	library_contents += library_songs.get(i).toString() + "\n";
		        }
		    return library_contents;
		}
}
