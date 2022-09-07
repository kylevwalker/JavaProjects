
/*
* AUTHOR: Kyle Walker
* FILE: Song.java
* ASSIGNMENT: Programming Assignment 4 - Spitify
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents a Song object which contains the title, artist, and number of plays.
* All songs in the Spitify program are stored as Song objects, which can be "played" to show the 
* title, artist, and number of plays as an informative output string.
*
*/
public class Song implements Comparable<Song> {
	
	private String song_title;
	private String song_artist;
	private int times_played;
	
	/*
	* Song class constructor. When called, a new instance of Song will be created and it will contain
	* a new title string and artist string. It also contains a default times_played number of 0.
	* @param title, the title of the song found through the first half of the song string in the input file.
	* @param artist, the artist of the song found through the second half of the song string in the input file.
	*/
	public Song(String title, String artist) {
		song_title = title;
		song_artist = artist;	
	}
	
	/*
	* Getter method for the Song's title, returns song_title string.
	* @return song_title, the string containing the song's title.
	*/
	public String getTitle() {
		return song_title;
	}
	
	/*
	* Getter method for the Song's artist, returns song_artist string.
	* @return song_artist, the string containing the song's artist.
	*/
	public String getArtist() {
		return song_artist;
	}
	
	/*
	* Getter method for the Song's play count, returns times_played int.
	* @return times_played, the int representing number of plays.
	*/
	public int getTimesPlayed() {
		return times_played;
	}
	
	/*
	* A method for "playing" the song, prints out the message for the user and increases play count.
	* Calls toString method to print the song playing information.
	*/
	public void play() {
		System.out.println(toString());
		times_played ++;
	}
	
	/*
	* A method for printing out song info and a message for the user to see the song is playing.
	* Formatted as: "(song title) by (song artist), (play count) play(s)"
	*/
	public String toString() {
		return(song_title + " by " + song_artist + ", " + times_played + " play(s)");
		
	}
	/*
	* compareTo method to compare a Song object with current song's title for alphabetical ordering.
	*
	* @param song, a Song object
	* @return int, integer representing order comparison of strings. (greater than, equal to, less than)
	*/
	public int compareTo(Song song){
    	return song_title.compareTo(song.getTitle());
    }
}

