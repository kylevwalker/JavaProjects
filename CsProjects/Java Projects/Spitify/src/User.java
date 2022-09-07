import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
* AUTHOR: Kyle Walker
* FILE: User.java
* ASSIGNMENT: Programming Assignment 4 - Spitify
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents a User object for logging in and granting acces to the program's
* functionality. Each user has their own username and password, and a list of playlists they can play 
* songs from.
*
*/
public class User implements Comparable<User>{
	private String username = null;
	private String userpassword = null;
	private ArrayList<Playlist> playlists;
	
	/*
	* User class constructor. When called, a new instance of User will be created, allowing for program
	* functionality. Each user has a username, password, and list of personal playlists.
	* @param name, the chosen username for the current User.
	* @param pass, the chosen password for the current User.
	*/
	public User(String name, String pass) {
		username = name;
		userpassword = pass;
		playlists = new ArrayList<Playlist>();
	}
	
	/*
	* Getter method for the User's username, returns username string.
	* @return username, the string containing the user name.
	*/
	public String getName() {
		return username;
	}
	
	/*
	* Method for checking if the login password matches the user's password. Returns
	* a boolean representing success of login attempt.
	* @param password, the password from the login attempt.
	* @return is_correct_pass, boolean of if the password was correctr or not.
	*/
	public boolean attemptLogin(String password) {
		boolean is_correct_pass = false;
		if (password.equals(userpassword)) {
			is_correct_pass = true;
			return is_correct_pass;
		}
		else {
			return is_correct_pass;
		}
	}
	
	/*
	* Method for adding new playlists to the user playlists list.
	* @param newPlaylist, new Playlist object to be added to user playlists list.
	*/
	public void addPlaylist(Playlist newPlaylist) {
		playlists.add(newPlaylist);
	}
	
	/*
	* Getter method for the User's list of playlists.
	* @return playlists, array list of Playlist objects.
	*/
	public List<Playlist> getPlaylists(){
		return playlists;
	}
	
	/*
	* Method that plays a selected playlist from the User's list. 
	* @ param name, String name of the playlist chosen to be played. Compared
	* with all elements until a match is found, then the playlist will be played.
	*/
	public void selectPlaylist(String name) {
		for (int i = 0; i < playlists.size(); i++) {
			if (playlists.get(i).getName().equals(name)) {
				playlists.get(i).play();
			}
		}
	}
	
	/*
	* Returns useful information on the User. Returns a string containing the
	* current User's name, and number of playlists.
	* @return user_info, a new string containing the username and number of playlists.
	*/
	public String toString() {
		String user_info = new String();
		user_info = (username + ", " + playlists.size() + " playlists"); 
		return user_info;
	}
    
	/*
	* compareTo method to compare a User object with current User's name for ordering.
	*
	* @param user, User object
	* @return int, integer representing order comparison of strings. (greater than, equal to, less than)
	*/
	public int compareTo(User user)
    {
    	return username.compareTo(user.getName());
    }
}
