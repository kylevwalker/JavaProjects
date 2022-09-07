import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* AUTHOR: Kyle Walker
* FILE: Playlist.java
* ASSIGNMENT: Programming Assignment 4 - Spitify
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents a Playlist object which holds an array list of songs added by user.
* Songs can be added or removed by the user, and the playlist can be played in the order it was created.
*
*/
public class Playlist {

	private String playlist_name;
	private ArrayList<Song> playlist_songs;
	
	/*
	* Playlist class constructor. When called, a new instance of Playlist will be created. It will store its
	* name as a string and create a new empty array list whihc will contain all added Songs.
	* @param name, the name of the playlist as given by the user.
	*/
	public Playlist(String name) {
		playlist_name = name;
		playlist_songs = new ArrayList<Song>();
	}
	/*
	* Other Playlist class constructor. When called, a new instance of Playlist will be created. It will store its
	* name as a string and store the passed list of songs in the playlists_songs list.
	* @param name, the name of the playlist as given by the user.
	* @param contents, List of Songs that will be stored into the playlist_songs array.
	*/
	public Playlist(String name, List<Song> contents) {
		playlist_name = name;
		playlist_songs = (ArrayList<Song>)contents;
	}
	
	/*
	* Getter method for the Playlist's name, returns playlist_name string.
	* @return playlist_name, the string containing the playlist's name.
	*/
	public String getName() {
		return playlist_name;
	}
	
	/*
	* Method for adding Songs to the playlist. Adds passed Song object to playlist's array.
	*/
	public void addSong(Song song) {
		playlist_songs.add(song);
	}
	
	/*
	* A method for "playing" all songs in the playlist. Iterates through the playlist array and calls
	* the Song.play() method to print the song info for each song.
	*/
	public void play() {
		for (int i = 0; i < playlist_songs.size(); i++) {
			playlist_songs.get(i).play();
		}
	}
	
	/*
	* Method for removing Songs from the playlist. Removes the given song if it exists.
	* @param song, Song to be removed from playlist.
	*/
	public void removeSong(Song song) {
		for (int i = 0; i < playlist_songs.size(); i++) {
			if (playlist_songs.get(i).equals(song)) {
				playlist_songs.remove(i);
			}
		}
	}
	
}

