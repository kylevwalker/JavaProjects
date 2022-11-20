import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * This class implements the abstract Command class
 * with all abstract default methods.
 * 
 * Commands will be instantiated and passed to GUI.executeCommand
 * in order to track command history, execute commands
 * and check for blocked commands.
 * 
 * The execute method must be called to make changes to the GUI.
 * The unexecute method will automatically make the reverse command as execute();
 * 
 * @author Ryan Pecha
 */
public abstract class Command {
	/*
	 * This method executes the given command type,
	 * updates the GUI,
	 * and return a boolean representing whether the 
	 * command was blocked or executed properly.
	 * 
	 * A blocked command is an incorrect action like
	 * shifting the caret right into unusable space,
	 * or setting a field to its existing value.
	 */
	public abstract boolean Execute();
	/*
	 * This method undoes the result of execute.
	 * 
	 * This must be implemented using a class variable 
	 * which stores the previous state, or by storing the
	 * command and completing the inverse.
	 */
	public abstract void Unexecute();
}



/*
 * This command sets the current charSize of the
 */
class CommandFontSize extends Command {

	// Class variables
	int prevSize;
	int newSize;
	
	// Constructor
	public CommandFontSize(int newSize) {
		// Storing the existing font size
		this.prevSize = LilLexiMain.gui.charSize;
		this.newSize = newSize;
	}
	
	// Command execution and GUI updating
	@Override
	public boolean Execute() {
		// setting the fontSize field GUI of GUi to newSize
		LilLexiMain.currentDoc.gui.charSize = newSize;
		LilLexiMain.gui.updateUI();
		return newSize == prevSize;
	}

	// Reverting the results of execute
	@Override
	public void Unexecute() {
		// resetting the fontSize field of GUI to prevSize
		LilLexiMain.currentDoc.gui.charSize = prevSize;
		LilLexiMain.gui.updateUI();
	}

}



/*
 * This command
 */
class CommandFontType extends Command {
	
	// Class variables
	String prevFont;
	String newFont;
	
	// Constructor
	public CommandFontType(String newFont) {
		// Storing the existing font
		this.prevFont = LilLexiMain.gui.font;
		this.newFont = newFont;
	}
	
	// Command execution and GUI updating
	@Override
	public boolean Execute() {
		// Setting the font field of GUI to newFont
		LilLexiMain.gui.font = newFont;
		LilLexiMain.gui.updateUI();
		return prevFont == newFont;
	}
	
	// Reverting the results of execute
	@Override
	public void Unexecute() {
		// Resetting the font of GUI to previous font
		LilLexiMain.gui.font = prevFont;
		LilLexiMain.gui.updateUI();
	}

}



/*
 * This command
 */
class CommandMouse extends Command {
	
	// Class variables
	private MouseEvent e;
	private int prevIndex;
	
	// Constructor
	public CommandMouse(MouseEvent e) {
		this.prevIndex = LilLexiMain.gui.getCaret().getIndex();
		this.e = e;
	}
	
	// Command execution and GUI updating
	@Override
	public boolean Execute() {
		// Setting the caret location using the mouse event coordinates
		Point clickLocation = new Point(e.x, e.y);
		Glyph selectedGlyph = LilLexiMain.currentDoc.getSelected(clickLocation);
		LilLexiMain.gui.getCaret().select(selectedGlyph);
		// Blocked if new location is the same as old location
		return this.prevIndex == LilLexiMain.gui.getCaret().getIndex();
	}
	
	// Reverting the results of execute
	@Override
	public void Unexecute() {
		// setting caret to previous location
		LilLexiMain.gui.getCaret().setIndex(prevIndex);
	}

}



/*
 * This command handles anything related to the caret location.
 * This includes caret moving, Glyph insertion, and Glyph removal
 */
class CommandCaret extends Command {
	
	// Class variables
	private KeyEvent e;
	private Glyph removedGlyph;
	
	// Constructor
	public CommandCaret(KeyEvent e) {
		this.e = e;
	}
	
	// Command execution and GUI updating
	@Override
	public boolean Execute() {
		
		// Converting our event into a readable keyCode
		Character c = (e.character);
		int keyCode = e.keyCode;
		
		// grabbing the caret
		IBeam caret = LilLexiMain.gui.getCaret();
		
		// Using indices of the caret to implement blocking
		int sIndex = caret.getIndex();
		int aIndex;
		boolean blocked = false; 
		
		// Parsing the keyCode
		switch(keyCode) {
			case 8:
				// Removing Glyph at caret location and storing into removedGlyph
				removedGlyph = caret.removeGlyph();
				break;
			case 13:
				// Adding newline at caret location
				caret.addGlyph(new Char('\n'));
				break;
			case 16777220:
				// Shifting caret right by one index
				caret.shiftOver(1);
				aIndex = caret.getIndex();
				// detecting block
				blocked = sIndex == aIndex;
				break;
			case 16777219:
				// Shifting caret left by one index
				caret.shiftOver(-1);
				aIndex = caret.getIndex();
				// detecting block
				blocked = sIndex == aIndex;
				break;
				
			default:
				// Adding a Glyph at the caret location
				caret.addGlyph(new Char(c));
		}
		
		// Updating the UI
		LilLexiMain.gui.updateUI();
		// blocked if old index is the same as new index
		return blocked;
	}

	// Reverting the results of execute
	@Override
	public void Unexecute() {
		int keyCode = e.keyCode;
		IBeam caret = LilLexiMain.gui.getCaret();
		switch(keyCode) {
			case 8:
				// Adding the removed Glyph
				caret.addGlyph(removedGlyph);
				break;
			case 13:
				// removing the added glyph
				caret.removeGlyph();
				break;
			case 16777220:
				// Shifting caret back left
				caret.shiftOver(-1);
				break;
			case 16777219:
				// shifting caret back right
				caret.shiftOver(1);
				break;
				
			default:
				// Removing added glyph
				caret.removeGlyph();
		}
		
		// Updating the UI
		LilLexiMain.gui.updateUI();
	}

}



/*
 * This command handles document column formating
 * Only single and double column formatting are currently supported.
 */
class CommandFormat extends Command {
	
	private int newColumnCount;
	private int prevColumnCount;

	public CommandFormat(int newColumnCount) {
		this.newColumnCount = newColumnCount;
		this.prevColumnCount = LilLexiMain.gui.columnCount;
	}
	
	@Override
	// Command execution and GUI updating
	public boolean Execute() {
		if (newColumnCount == 1) {
			LilLexiMain.currentDoc.setComposition(new SingleColCompositor());
		}
		else if (newColumnCount == 2) {
			LilLexiMain.currentDoc.setComposition(new DoubleColCompositor());
		}
		else {
			return true;
		}
		// Updating the UI
		LilLexiMain.gui.updateUI();
		return this.newColumnCount == this.prevColumnCount;
	}
	
	// Reverting the results of execute
	@Override
	public void Unexecute() {
		if (prevColumnCount == 1) {
			LilLexiMain.currentDoc.setComposition(new SingleColCompositor());
		}
		else if (prevColumnCount == 2) {
			LilLexiMain.currentDoc.setComposition(new DoubleColCompositor());
		}
		// Updating the UI
		LilLexiMain.gui.updateUI();
	}
	
}



