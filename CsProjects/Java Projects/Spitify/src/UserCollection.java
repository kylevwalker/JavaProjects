import java.util.Iterator;
import java.util.TreeSet;

/*
* AUTHOR: Kyle Walker
* FILE: UserCollection.java
* ASSIGNMENT: Programming Assignment 4 - Spitify
* COURSE: CSc 210; Fall 2021
* PURPOSE: This class represents a Tree Set of Users. This allows for multiple different Users
* to access the program, and allows for different users to log in and out.
*
*/
public class UserCollection {
	
	private TreeSet<User> users_set;
	
	/*
	* UserCollection class constructor. When called, a new instance of UserCollection will be created, allowing
	* multiple users to be stored within the new empty users_set
	*/
	public UserCollection() {
		users_set = new TreeSet<User>();
	}
	
	/*
	* Method for checking if the given username corresponds to an existing User in the UserCollection.
	* A boolean represents if the user exists or not.
	* @param username, String for User's username.
	* @return does_exist, bool representing if user exists on collection or not.
	*/
	public boolean userExists(String username) {
		boolean does_exist = false;
		Iterator<User> itr = users_set.iterator();
		while (itr.hasNext()){
			User cur_user = itr.next();
			if (cur_user.getName() == username) {
				does_exist = true;
			}
		}
		return does_exist;
	}
	
	/*
	* Method allowing Users to login between accounts. If the username and password is correct,
	* the login is successful. Otherwise, returns null user.
	* @param username, username String of User for login
	* @param password, password String for login attempt
	* @return cur_user, current User node of Tree that corresponds to the login attempt.
	*/
	public User login(String username, String password) {
		Iterator<User> itr = users_set.iterator();
		while (itr.hasNext()){
			User cur_user = itr.next();
			if (cur_user.getName().equals(username) && cur_user.attemptLogin(password)) {
				return cur_user;
			}
		}
		return null;
	}
	
	/*
	* Method for adding new users to the UserCollection set.
	* @param add, a User object to be added.
	*/
	public void addUser(User add) {
		users_set.add(add);
	}
	
	/*
	* Returns a string containing all Users in alphabetical order in the format: "{user1, user2, etc..}"
	* Depends on User.toString method to create strings.
	* @return all_users, string containing "set" of all Users.
	*/
	public String toString() {
		String all_users = new String("{ ");
		Iterator<User> itr = users_set.iterator();
		while (itr.hasNext()){
			User cur_user = itr.next();
			all_users += cur_user.toString();
		}
		all_users += "}";
		return all_users;
	}
}
