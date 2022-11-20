import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

/**
 * Class representing the current GUI instance.
 * Contains all graphics, Glyphs, settings, and the current Document.
 * Contains, tracks, and executes Commands.
 * Executes Spell Checking and updates all Glyphs under the open documents.
 * 
 * @author Ryan Pecha
 */
public class GUI {
	
	//
	
	// GUI components
	private Display display;
	private Shell shell;
	private Canvas canvas;
	private Composite docComp;
	private IBeam caret;
	private Document currentDoc;
	
	// GUI settings
	protected int charSize;
	protected String font;
	protected int columnCount;
	
	// commandPast and commandFuture have their head at the current state
	private ArrayList<Command> commandsPast;
	private ArrayList<Command> commandsFuture;
	
	// set containing the valid word dictionary
	HashSet<String> dictSet;

	
	
	/*
	 * Constructor
	 * Initializing all class variables
	 */
	public GUI() {

		
		// initializing the window and the shell
		Display.setAppName("Lil' Lexi");
		display = new Display();
		shell = new Shell(display);
		shell.setText("Lil' Lexi");
		shell.setSize(760, 1000);
		RowLayout layout = new RowLayout();
		shell.setLayout(layout);
		
		// initializing empty Composite used for doc layout
		docComp = new Composite(shell, SWT.NONE);
		
		// initializing command history arrays
		commandsPast = new ArrayList<Command>();
		commandsFuture = new ArrayList<Command>();
		
		// settings the default 
		charSize = 16;
		font = "Courier";
		columnCount = 1;
		
		// initializing the dictionary using the contents of dictionary.txt
		dictSet = new HashSet<String>();
		Scanner fs = null;
		try {
			fs = new Scanner(new File("dictionary.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fs.hasNext()) {
			dictSet.add(fs.next().toLowerCase());
		}
		fs.close();
	}

	
	
	/*
	 * Method used to start the editor loop.
	 * Initialized the currentDoc, events, and update loop
	 */
	public void start() {
		
		// create widgets for the interface
		currentDoc = LilLexiMain.currentDoc;

		// Set Menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.setMenu(display, shell);
		
		// Canvas
		canvas = new Canvas(docComp, SWT.NONE);
		canvas.setSize(760, 1000);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				currentDoc.draw(e);
			}
		});

		// Mouse click parsing
		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				CommandMouse cm = new CommandMouse(e);
				executeCommand(cm);
			}
			public void mouseUp(MouseEvent e) { }
			public void mouseDoubleClick(MouseEvent e) { }
		});
		
		// Key press parsing
		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				CommandCaret cc = new CommandCaret(e);
				executeCommand(cc);
			}
			public void keyReleased(KeyEvent e) { }
		});
		
		// Caret icon updating
		canvas.addListener(SWT.MouseEnter, new Listener() {
			public void handleEvent(Event e) {
				canvas.setCursor(canvas.getDisplay().getSystemCursor(SWT.CURSOR_IBEAM));
			}
		});
		canvas.addListener(SWT.MouseExit, new Listener() {
			public void handleEvent(Event e) {
				canvas.setCursor(canvas.getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
			}
		});
		
		// CARET
		caret = new IBeam(canvas);
		caret.select(currentDoc.getComposition());
		
		// SLIDER
		Slider slider = new Slider(shell, SWT.VERTICAL);
		Rectangle clientArea = shell.getClientArea();
		RowData sliderSize = new RowData(24, clientArea.height);
		slider.setLayoutData(sliderSize);
		slider.addListener(SWT.Selection, event -> {
			int sliderOffset = -slider.getSelection();
			canvas.setLocation(0, (int)(currentDoc.getBounds().height * (sliderOffset * 0.005)));
			updateUI();
		});

		// GUI UPDATE LOOP
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch()) {
			}
		shell.dispose();
		display.dispose();
	}

	
	// method used to call spellCheck and update all graphics 
	public void updateUI() {
		spellCheck();
		canvas.redraw();
	}
	
	/*
	 * GUI field get Methods
	 */
	public Display getDisplay() {
		return display;
	}

	public Shell getShell() {
		return shell;
	}
	
	public IBeam getCaret() {
		return caret;
	}
	public Canvas getCanvas() {
		return canvas;
	}
	
	
	
	/*
	 * Method used to execute commands and update the redo/undo Command list
	 */
	public void executeCommand(Command command) {
		boolean blocked = command.Execute();
		if (blocked) { return; }
		commandsFuture.clear();
		commandsPast.add(0,command);
	}
	
	/*
	 * Method used to undo commands from the past Command list and update the future command list
	 */
	public void undo() {
		if (commandsPast.isEmpty()) { return; }
		Command command = commandsPast.remove(0);
		command.Unexecute();
		commandsFuture.add(0,command);
	}
	
	/*
	 * Method used to redo commands from the future Command list and update the past command list
	 */
	public void redo() {
		if (commandsFuture.isEmpty()) { return; }
		Command command = commandsFuture.remove(0);
		command.Execute();
		commandsPast.add(0,command);
	}

	
	
	/*
	 * Method run each UI update to set the spelledCorrect field of each character.
	 * Uses the Iterator design pattern of Glyph to iterate over each Character Glyph
	 */
	public void spellCheck() {
		// String and Char list for the current word
		String cur = "";
		ArrayList<Char> charList = new ArrayList<Char>();
		
		// Iterating over the Children of our document composition using the Iterator Pattern
		for (Glyph glyph: LilLexiMain.currentDoc.getComposition()) {
			
			// Filtering out images for spell check
			if (!(glyph instanceof Char)) { continue; }
			
			// Up-casting for Glyph > Char to grab character field and do spell checking
			Char gc = (Char)glyph;
			Character character = gc.character;
			
			// Skipping spaces
			if (character == ' ') {
				cur = "";
				charList.clear();
				gc.correctSpelling = true;
				continue;
			}
			
			// Adding Char to current word and Skipping non-alphabetics
			charList.add(gc);
			if (Character.isAlphabetic(character)) {
				cur += character;
			}
			
			// setting spelledCorrect field for each Char Glyph
			boolean spelledCorrect = dictSet.contains(cur.toLowerCase());
			for (Char tc : charList) {
				tc.correctSpelling = spelledCorrect;
			}
		}
		
	}
	
	
	
	
	
	
	
}
