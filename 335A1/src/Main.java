
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;

/*
 * Author: Kyle Walker.
 * Course: CSC 335, fall 2022.
 * Purpose: 
 * 		This program creates a simple but modular virtual memory game using SWT graphics and object-oriented design.
 * 		The Main class controls the UI and functions within the update loop, using instances of relevant classes to
 * 		simulate a concentration memory game. The game starts with a main menu UI, where the user can customize their
 * 		preferred game options. The code also allows for easy implementation of new options such as new game modes or card 
 * 		decks. The user can then start the game with displays showing various info for the game. The game also allows for 
 * 		replaying with the same options, or returning to the menu at any time to change the settings. 
 * PreConditions: 
 * 		Some data used in the program depends on external files present in the source folder. The program
 * 		assumes these files are present, and if any new ones were to be added, they must be declared within the CardDeckHolder
 * 		class. 
 * 		The GameManager class, while capable of multiple game modes, must contain the rules for all modes which
 * 		must be added to the existing class.
 * 		The game board grid will resize to any even numbered deck, but is designed to display a deck that is a multiple of
 * 		4 to display 4 cards per row.
 * 		
 */
public class Main {

	// UI/Gameplay variables
	public static Display display;
	private static Shell menuShell;
	private static Shell gameShell;
	private static List<Combo> userSettings;


	// Game Classes
	private static GameManager currentManager;
	private static CardDeck currentDeck;
	private static GameBoard currentBoard;

	public static void main(String[] args) {
		display = new Display();
		// Starts with main menu.
		mainMenuUI();
	}

	/**
	 * Prepares game screen using values from menu inputs to create necessary game
	 * class instances.
	 */
	public static void startGame() {

		CardDeckHolder allDecks = new CardDeckHolder();
		final int MAXDELAY = 2000;
		int playerCount = userSettings.get(0).getSelectionIndex() + 1;
		int memSpeed = MAXDELAY / (userSettings.get(1).getSelectionIndex() + 1); // divides the maximum delay(ms) by speed.
		int gameMode = userSettings.get(2).getSelectionIndex();
		int deckNumber = userSettings.get(3).getSelectionIndex();
		
		// Initialize game classes using user settings.
		currentDeck = allDecks.getAvailableDeck(deckNumber);
		currentBoard = new GameBoard(currentDeck);
		currentManager = new GameManager(gameMode, memSpeed, playerCount, currentBoard);
		// Creates the new Shell for the current game.
		enableGameCanvas();
	}

	/**
	 * Restarts the game by disposing current game shell and re-opening the main
	 * menu.
	 */
	public static void restart() {
		gameShell.dispose();
		menuShell.setVisible(true);
		menuShell.setActive();
	}

	/**
	 * Stores and initializes the UI design of the main menu, and contains the
	 * graphics update loop for the system. The main menu shell contains drop down
	 * menus for selecting preferred settings, and has buttons for starting or
	 * quitting the game. Settings contain tool tips to guide user.
	 */
	private static void mainMenuUI() {

		// One limitation of the program is that the available options must be
		// hard-coded in using String arrays.
		// Arrays are just used for displaying in menus.
		String[] availableDecks = { "Animals(8 Cards)", "Fruits(12 Cards)" };
		String[] playerCount = { "1", "2", "3", "4" };
		String[] gameModes = { "Standard", "One-Flip" };
		String[] speedVals = { "Sloth", "Slow", "Normal", "Fast", "Cheetah" };

		// Creates main menu shell and sets the background image.
		menuShell = new Shell(display);
		menuShell.setSize(500, 400);
		menuShell.setText("Concentration Game - Menu");
		menuShell.setBackgroundImage(new Image(display, "concentrationGame.jpg"));

		// Sets the layout of the main menu to a vertical, justified row layout.
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL | SWT.TRANSPARENT | SWT.CENTER);
		rowLayout.justify = true;
		rowLayout.marginLeft = 10;
		rowLayout.marginTop = 2;
		rowLayout.spacing = 2;
		menuShell.setLayout(rowLayout);

		// Composite section for the player number selection options.
		Composite playerSelectRow = new Composite(menuShell, SWT.NO_FOCUS | SWT.TRANSPARENT | SWT.CENTER);
		playerSelectRow.setLayout(new RowLayout(SWT.VERTICAL | SWT.CENTER));
		Label playerSelectLabel = new Label(playerSelectRow, SWT.NONE);
		playerSelectLabel.setText("Player Count: ");
		playerSelectLabel.setToolTipText("How many people are playing?");
		Combo playerSelect = new Combo(playerSelectRow, SWT.DROP_DOWN | SWT.READ_ONLY);
		playerSelect.setItems(playerCount);
		playerSelect.select(0);

		// Composite section for card delay speed selection options.
		Composite speedSelectRow = new Composite(menuShell, SWT.NO_FOCUS | SWT.TRANSPARENT | SWT.CENTER);
		speedSelectRow.setLayout(new RowLayout(SWT.VERTICAL | SWT.CENTER));
		Label speedSelectLabel = new Label(speedSelectRow, SWT.NONE);
		speedSelectLabel.setText("Memory Speed: ");
		speedSelectLabel.setToolTipText("How fast do you want the cards to disappear?");
		Combo speedSelect = new Combo(speedSelectRow, SWT.DROP_DOWN | SWT.READ_ONLY);
		speedSelect.setItems(speedVals);
		speedSelect.select(2);

		// Composite section for game mode selection options.
		Composite modeSelectRow = new Composite(menuShell, SWT.NO_FOCUS | SWT.TRANSPARENT | SWT.CENTER);
		modeSelectRow.setLayout(new RowLayout(SWT.VERTICAL | SWT.CENTER));
		Label modeSelectLabel = new Label(modeSelectRow, SWT.NONE);
		modeSelectLabel.setText("Game Mode: ");
		modeSelectLabel.setToolTipText("What version of the game are we playing?");
		Combo modeSelect = new Combo(modeSelectRow, SWT.DROP_DOWN | SWT.READ_ONLY);
		modeSelect.setItems(gameModes);
		modeSelect.select(0);

		// Composite section for card deck theme selection options.
		Composite deckSelectRow = new Composite(menuShell, SWT.NO_FOCUS | SWT.TRANSPARENT | SWT.CENTER);
		deckSelectRow.setLayout(new RowLayout(SWT.VERTICAL | SWT.CENTER));
		Label deckSelectLabel = new Label(deckSelectRow, SWT.NONE);
		deckSelectLabel.setText("Deck Theme: ");
		deckSelectLabel.setToolTipText("Which theme do you want for the card deck?");
		Combo deckSelect = new Combo(deckSelectRow, SWT.DROP_DOWN | SWT.READ_ONLY);
		deckSelect.setItems(availableDecks);
		deckSelect.select(0);
		
		// Store references to each Combo within userSettings Array.
		userSettings = new ArrayList<Combo>();
		userSettings.add(playerSelect);
		userSettings.add(speedSelect);
		userSettings.add(modeSelect);
		userSettings.add(deckSelect);

		// Creates the Start button used for calling the StartGame() method through the
		// ButtonSelectionListener.
		Button startButton = new Button(menuShell, SWT.PUSH);
		startButton.setText("Start Game");
		startButton.setLayoutData(new RowData(100, 40));
		startButton.addSelectionListener(new ButtonSelectionListener());

		// Creates the Quit button used for calling System.exit(0) through the
		// ButtonSelectionListener.
		Button quitButton = new Button(menuShell, SWT.PUSH);
		quitButton.setText("Quit");
		quitButton.setLayoutData(new RowData(100, 40));
		quitButton.addSelectionListener(new ButtonSelectionListener());

		// Main GUI update Loop. Because the main menu is never actually disposed while
		// playing, this
		// will run until the game is closed.
		menuShell.open();
		while (!menuShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * Stores and initializes the UI design for the current game shell window. Is
	 * called any time a new game is started, creating a new game window with
	 * updated user settings from the menu. The canvas holds the main card deck
	 * grid, which listens for mouse inputs and displays the visual simulation of
	 * the memory card game.
	 */
	private static void enableGameCanvas() {

		// Hides the menu while playing.
		menuShell.setVisible(false);
		// Creates a new game shell upon creation, starting at the dimensions of the
		// cards in grid layout.
		if (gameShell == null || gameShell.isDisposed()) {
			gameShell = new Shell(display);
			int cardSize = currentDeck.get(0).getFront().getImageData().width;
			int gameX = cardSize * 4;
			int gameY = cardSize * (currentDeck.size() / 2);
			gameShell.setSize(gameX, gameY);
			gameShell.setText("Concentration Game");
			gameShell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		}

		// Organizes the game shell as a centered vertical row, beginning with the game
		// grid.
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.center = true;
		gameShell.setLayout(layout);

		// The shell is divided into two halves, the top containing the game grid and
		// the bottom
		// containing game information and buttons.
		Composite top = new Composite(gameShell, SWT.NO_FOCUS);
		Composite bottom = new Composite(gameShell, SWT.NO_FOCUS);

		// Creates the game canvas for the deck grid, using a grid layout.
		Canvas gameCanvas = new Canvas(top, SWT.NONE);
		GridLayout grid = new GridLayout();
		gameCanvas.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		gameCanvas.setLayout(grid);
		gameCanvas.setSize(gameShell.getBounds().width, gameShell.getBounds().height);

		// Sets the layout of the bottom Composite to a packed, centered vertical row
		// layout.
		RowLayout bottomColumn = new RowLayout(SWT.VERTICAL | SWT.CENTER);
		bottomColumn.pack = true;
		bottom.setLayout(bottomColumn);

		// Composite for current player's turn info, which is set in bold font and given
		// a unique color
		// that distinguishes each of the 4 players.
		Composite playerTab = new Composite(bottom, SWT.NO_FOCUS);
		playerTab.setLayout(new RowLayout(SWT.HORIZONTAL | SWT.CENTER));
		Label playerLabel = new Label(playerTab, SWT.NONE);
		FontData fontData = playerLabel.getFont().getFontData()[0];
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		playerLabel.setFont(font);
		playerLabel.setText("Player " + currentManager.getCurrentPlayer().getNum() + "'s turn");
		playerLabel.setForeground(currentManager.getCurrentPlayer().getColor());

		// Composite for the score board of all players, showing each player's score and
		// turns in order.
		Composite scoreTab = new Composite(bottom, SWT.NO_FOCUS);
		scoreTab.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label scoreLabel = new Label(scoreTab, SWT.NONE);
		List<Player> players = currentManager.getPlayerList();
		String scoreBoard = new String();
		for (int i = 0; i < players.size(); i++) {
			Player cur = players.get(i);
			scoreBoard += ("Player " + cur.getNum() + ": " + cur.getScore() + " points, " + cur.getTurns()
					+ " turns \n");
		}
		scoreLabel.setText(scoreBoard);

		// Composite for the row of buttons on the bottom. Organized in horizontal row
		// layout.
		Composite buttonTab = new Composite(bottom, SWT.NO_FOCUS);
		RowLayout buttonRow = new RowLayout(SWT.HORIZONTAL);
		buttonRow.spacing = 10;
		buttonTab.setLayout(buttonRow);

		// Creates the menu button, which calls Restart() method from
		// ButtonSelectionListener to return to
		// main menu.
		Button menuButton = new Button(buttonTab, SWT.PUSH);
		menuButton.setText("Return to Menu");
		menuButton.addSelectionListener(new ButtonSelectionListener());

		// Creates the quit button, which calls System.exit(0) from
		// ButtonSelectionListener to end game
		// and close application.
		Button quitButton = new Button(buttonTab, SWT.PUSH);
		quitButton.setText("Quit");
		quitButton.addSelectionListener(new ButtonSelectionListener());

		// Creates the replay button, which only appears after a game has been finished.
		// Calls Restart() method
		// from ButtonSelectionListener after disposing active gameShell to create a new
		// game with the same user
		// settings.
		Button replayButton = new Button(buttonTab, SWT.PUSH);
		replayButton.setText("Replay");
		replayButton.setLayoutData(new RowData(100, 40));
		replayButton.setEnabled(false);
		replayButton.setVisible(false);
		replayButton.addSelectionListener(new ButtonSelectionListener());

		// Stores the text labels and buttons in arrays so that the CanvasMouseListener
		// has access to update
		// values for each. Assigns a canvasPaintListener and CanvasMouseListener to the
		// gameCanvas.
		Label[] labels = { playerLabel, scoreLabel };
		Button[] buttons = { menuButton, quitButton, replayButton };
		gameCanvas.addPaintListener(new CanvasPaintListener(gameCanvas));
		gameCanvas.addMouseListener(new CanvasMouseListener(gameCanvas, bottom, labels, buttons));

		// Opens the shell after all UI has been set, and packs it so that all widgets
		// are readable.
		gameShell.open();
		gameShell.pack();

	}
	// ---------------------Inner Classes
	// ---------------------------------------------

	/**
	 * PaintListener used for receiving paint events from the gameCanvas. Draws the
	 * gameBoard after each update, showing the current arrangement of cards in the
	 * game deck grid as well as the images for both sides of the cards when
	 * flipped.
	 */
	static class CanvasPaintListener implements PaintListener {

		// Reference to active gameCanvas.
		Canvas canvas;

		/**
		 * Construct a new CanvasPaintListener that listens to paint events from current
		 * game canvas.
		 * 
		 * @param gameCanvas Reference to current game Canvas
		 */
		public CanvasPaintListener(Canvas gameCanvas) {
			canvas = gameCanvas;
		}

		public void paintControl(PaintEvent event) {
			/*
			 * Uses the board list from the current GameBoard instance to display the images
			 * of each card in the grid. Draws each card's current side while displaying
			 * their position in the grid.
			 */
			CardDeck gameBoard = currentBoard.getBoard();
			// Gets client area of canvas grid for card selection bounds.
			Rectangle rect = canvas.getClientArea();
			// Gets image data from one card in the deck to represent all cards.
			ImageData data = currentDeck.get(0).getCurrentFace().getImageData();
			// Finds the proportional number of cards per row by dividing the client area by
			// the width of a card.
			int stride = rect.width / data.width;

			// Draws each card from the board, getting their current image to display.
			for (int i = 0; i < gameBoard.size(); i++) {
				Card currentCard = gameBoard.get(i);
				Image currentImage;
				if (currentCard != null) {
					currentImage = currentCard.getCurrentFace();
					Rectangle outline = new Rectangle((i % stride) * data.width, (i / stride) * data.height, data.width,
							data.height);
					event.gc.drawImage(currentImage, (i % stride) * data.width, (i / stride) * data.height);
					event.gc.setLineWidth(4);
					event.gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
					event.gc.drawRectangle(outline);
				}
			}
		}
	}

	/**
	 * CanvasListener used for receiving mouse events from the gameCanvas. Handles
	 * the main game logic using direct input from the user mouse to call outside
	 * methods from other game classes. Stores references to important UI widgets
	 * from the game shell to directly modify them for display. Contains public
	 * methods for outside classes to call in response to important game events.
	 */
	static class CanvasMouseListener implements MouseListener {
		// References to game UI widgets
		Canvas canvas;
		Composite comp;
		Label[] labels;
		Button[] buttons;
		Label playerLabel;
		Label scoreLabel;
		Button menuButton;
		Button quitButton;
		Button replayButton;
		// Stores current pair of Card objects
		Card prevCard = null;
		Card currentCard = null;
		List<Card> currentPair = new ArrayList<Card>();

		/**
		 * Constructs a new CanvasMouseListener with references to important game UI
		 * widgets.
		 * 
		 * @param gameCanvas Reference to active game Canvas
		 * @param bottom     Reference to active bottom Composite for game info
		 * @param labels     Array storing references to game info Labels
		 * @param buttons    Array storing references to game Buttons
		 */
		public CanvasMouseListener(Canvas gameCanvas, Composite bottom, Label[] labels, Button[] buttons) {
			canvas = gameCanvas;
			comp = bottom;
			this.labels = labels;
			this.buttons = buttons;
			playerLabel = labels[0];
			scoreLabel = labels[1];
			menuButton = buttons[0];
			quitButton = buttons[1];
			replayButton = buttons[2];
		}

		@Override
		public void mouseDoubleClick(MouseEvent event) {
		}

		public void mouseDown(MouseEvent event) {
			/*
			 * Handles important game logic when a Mouse click Event occurs. Each turn
			 * comprises of the selection of two different cards. This event will trigger
			 * until a pair of different cards are selected, at which point the GameManager
			 * class instance currentManager will perform the relevant game logic for the
			 * current turn. Includes a delay timer to pause input while the cards are
			 * flipped, giving the user time to see the cards before they are flipped back.
			 */

			Rectangle rect = canvas.getClientArea();
			ImageData data = currentDeck.get(0).getFront().getImageData();

			// Divides the size of the client area by the width of a card to find the width
			// of each index.
			// Splits the client area into selectable zones corresponding to a GameBoard
			// index containing a card.
			int col = event.x / data.width;
			int row = event.y / data.height;
			int idx = col + row * rect.width / data.width;
			if (idx < currentBoard.getBoard().size()) {
				prevCard = currentCard; // stores a reference to the previously selected card for comparisons.
				currentCard = currentBoard.getBoard().get(idx);
			}

			// Only completes a turn if two different available cards are selected.
			if (currentCard != null && !(currentCard == prevCard)) {
				currentCard.flip(); // makes a selected card visible.
				updateCanvas(canvas);
				currentPair.add(currentCard);
				// When a turn is completed, disables further mouse input and runs timer before
				// GameManager calculates
				// current turn outcome.
				if (currentPair.size() == 2) {
					canvas.removeMouseListener(this);
					menuButton.setEnabled(false); // Disables exiting to menu while timer is running to prevent
													// isDisabled exception.
					// Runs an asynchronous timer based on the user's chosen speed setting, pausing
					// input and showing cards until over.
					display.asyncExec(() -> display.timerExec(currentManager.getGameSpeed(), () -> onDelayFinish()));
				}
				updateCanvas(canvas);
			}
		}

		@Override
		public void mouseUp(MouseEvent e) {
		}

		/**
		 * Called when the delay timer is finished to execute any logic that was
		 * withheld during delay.
		 */
		private void onDelayFinish() {
			// GameManager checks if current pair is a match and empties the currentPair
			// array.
			currentPair = currentManager.detectMatch(currentPair);
			menuButton.setEnabled(true); // Exit to menu OK now.

			// Creates a new CanvasMouseListener to replace the one removed from the game
			// canvas.
			canvas.addMouseListener(new CanvasMouseListener(canvas, this.comp, labels, buttons));
			// Updates the player info from when the turn was completed.
			playerLabel.setForeground(currentManager.getCurrentPlayer().getColor());
			playerLabel.setText("Player " + currentManager.getCurrentPlayer().getNum() + "'s turn");
			// Displays all players' scores and turn numbers on a score board.
			List<Player> players = currentManager.getPlayerList();
			String scoreBoard = new String();
			for (int i = 0; i < players.size(); i++) {
				Player cur = players.get(i);
				scoreBoard += ("Player " + cur.getNum() + ": " + cur.getScore() + " points, " + cur.getTurns()
						+ " turns \n");
			}
			scoreLabel.setText(scoreBoard);
			// Checks if the GameManager finished the game to call the onGameFinished
			// method.
			if (currentManager.isFinished()) {
				onGameFinished();
			}
			updateCanvas(canvas);
		}

		/**
		 * Updates and redraws the game Canvas and game Shell so that changes to the UI
		 * widgets are applied.
		 * 
		 * @param canvas Reference to active game Canvas
		 */
		private void updateCanvas(Canvas canvas) {
			canvas.redraw();
			canvas.update();
			gameShell.redraw();
			gameShell.update();
		}

		/**
		 * Called when the GameManager reports a finished game so user can either replay
		 * or exit the game.
		 */
		public void onGameFinished() {
			// Enables the replay button upon completion.
			replayButton.setVisible(true);
			replayButton.setEnabled(true);
			// Gets player data from GameManager to output winning player info.
			Player winner = currentManager.getWinner();
			playerLabel.setText("Player " + winner.getNum() + " WINS!"); // Winner number
			// Displays winner's score and turn number.
			scoreLabel.setText("Score: " + winner.getScore() + "	Turns: " + winner.getTurns());
			updateCanvas(canvas);
		}

	}

	/**
	 * SelectionListener that listens for Button Selection Events and calls
	 * corresponding method.
	 */
	static class ButtonSelectionListener implements SelectionListener {
		// Listens to which button was pressed and matches the switch case to perform a
		// task.
		public void widgetSelected(SelectionEvent event) {
			/*
			 * When a button is pressed, its name substring is extracted and used in switch
			 * cases to call the matching method for the button.
			 */
			String eventWidget = event.widget.toString().substring(7);
			switch (eventWidget) {
			case "{Start Game}":
				startGame(); // Initializes game classes, then creates game screen from menu.
				break;
			case "{Return to Menu}":
				restart(); // Ends current game and returns back to the menu.
				break;
			case "{Replay}":
				gameShell.dispose(); // Disposes the current game and starts a new one with same settings.
				startGame();
				break;
			case "{Quit}":
				System.exit(0);
				break;
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	}

}
