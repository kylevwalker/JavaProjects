/*
 * Read and understand this class before writing the other classes.
 * 
 * This class would be designed a bit differently if this was not an assignment, 
 * but some functionality has been put here to reduce the work needed on the other classes.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PA4Main {
	private static Scanner input = null;

	public static void main(String[] args) {
		User currentUser = null;
		UserCollection allUsers = new UserCollection();
		Library lib = null;

        try {lib = initSongLibrary(args[0]);} 
        catch (FileNotFoundException e) {e.printStackTrace();}
        
		input = new Scanner(System.in);		
		userInterface(currentUser, allUsers, lib);
		input.close();
	}

	public static void userInterface(User currentUser, UserCollection allUsers, Library lib) {
		int EXIT_SELECTION = 9;
		int selected = 0;
		
		System.out.println("Welcome to Spitify, please select an option");
		
		while (selected != EXIT_SELECTION) {
			switch (selected) {
				case 1:
					showPlaylists(currentUser);
					break;
				case 2:
					playPlaylist(currentUser);
					break;
				case 3:
					createPlaylist(currentUser, lib);
					break;
				case 4:
					showSongs(currentUser, lib);
					break;
				case 5:
					playSong(currentUser, lib);
					break;
				case 6:
					addNewUser(allUsers);
					break;
				case 7:
					currentUser = login(allUsers);
					break;
				case 8:
					currentUser = null;
					logout();
					break;
			}
			
			prompt();
			try {
				selected = input.nextInt();
			} 
			catch (InputMismatchException e) {
				selected = 0;
				System.out.println("Invalid command!");
			}
			input.nextLine();
		}
		System.out.println("Bye Bye, ptooey!");	
	}

	public static void prompt() {
		System.out.println();
		System.out.println("1: Show Playlists");
		System.out.println("2: Play Playlist");
		System.out.println("3: Create New Playlist");
		System.out.println("4: Show Songs");
		System.out.println("5: Play Song");
		System.out.println("6: Create New User");
		System.out.println("7: Log In");
		System.out.println("8: Log Out");
		System.out.println("9: Exit");
		System.out.println("Next Option? ");
	}
	
	
	public static Library initSongLibrary(String filename) throws FileNotFoundException {
		Library out = new Library();
		File file = new File(filename);
		
		Scanner read = new Scanner(file);
		String curr;
		while (read.hasNextLine()) {
			curr = read.nextLine();
			String[] songArtist = curr.split("/");
			out.addSong(new Song(songArtist[1], songArtist[0]));
		}
		read.close();
		return out;
	}
	
	public static void showPlaylists(User curr) {
		if (loggedIn(curr)) {
			List<Playlist> playlists = curr.getPlaylists();
			for (Playlist playlist : playlists) {
				System.out.println(playlist.getName());
			}
		}
	}
	
	public static void playPlaylist(User curr) {
		if (loggedIn(curr)) {
			System.out.println("Playlist name? ");
			String name = input.next();
			curr.selectPlaylist(name);
		}
	}
	
	public static void createPlaylist(User curr, Library songs) {
		if (loggedIn(curr)) {
			System.out.println("Playlist name? ");
			String name = input.nextLine();
			
			Playlist toAdd = new Playlist(name);
			Song s;
			
			System.out.println("Next Song? ");
			name = input.nextLine();
			while (!name.equals("")) {
				s = songs.getSong(name);
				if (songExists(s)) {
					toAdd.addSong(s);
				}
				System.out.println("Next Song? ");
				name = input.nextLine();
			}
			curr.addPlaylist(toAdd);
			System.out.printf("Playlist %s Created!\n", toAdd.getName());
		}
	}
	
	public static void showSongs(User curr, Library allSongs) {
		if (loggedIn(curr)) {
			System.out.println(allSongs);
		}
	}
	
	public static void playSong(User curr, Library allSongs) {
		if (loggedIn(curr)) {
			System.out.println("Song name? ");
			String name = input.nextLine();
			Song toPlay = allSongs.getSong(name);
			if (songExists(toPlay)) {
				toPlay.play();
			}
		}
	}
	
	public static boolean songExists(Song s) {
		if (s == null) {
			System.out.println("Song not found!");
			return false;
		} else {
			return true;
		}
	}
	
	public static void addNewUser(UserCollection all) {
		System.out.println("New Username? ");
		String name = input.nextLine();
		System.out.println("Password? ");
		String pass = input.nextLine();
		
		User toAdd = new User(name, pass);
		all.addUser(toAdd);
		System.out.println("User " + name + " Added!");
	}
	
	public static User login(UserCollection all) {
		System.out.println("Username? ");
		String name = input.nextLine();
		System.out.println("Password? ");
		String pass = input.nextLine();
		
		User attempt = all.login(name, pass);
		if (attempt == null) {
			System.out.println("Login failed!");
		} else {
			System.out.println("Logged in as " + name);
		}
		return attempt;
	}
	
	public static void logout() {
		System.out.println("Logout Successful");
	}
	
	public static boolean loggedIn(User in) {
		if (in == null) {
			System.out.println("You must log in to do this!");
			return false;
		} else {
			return true;
		}
	}
}

