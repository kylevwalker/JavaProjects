
/**
 * This class implements a single instance of the Lil Lexi Program.
 * It will create a GUI and open a single document before running the GUI.
 * Editing existing documents is not currently supported.
 * 
 * @author Ryan Pecha
 */
public class LilLexiMain {
	
	// Class variables
	// All static so they can be referenced quickly
	protected static GUI gui;
	protected static Document currentDoc;

	// LilLexi Main
	public static void main(String[] args) {
		
		// Creates new GUI
		gui = new GUI();
		
		// Creates the current Document
		Document currentDocument = new Document();
		currentDoc = currentDocument;
		
		// Starts UI and opens shell
		gui.start();
	}

}
