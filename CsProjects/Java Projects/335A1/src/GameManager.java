import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * GameManager class which keeps track of players and performs game logic used
 * in gameplay loop. Processes outcome of each turn and changes gameplay rules
 * based on current gameplay Mode chosen by user.
 */
public class GameManager {
	// Game Settings
	private int playerCount = 1;
	private int gameMode = 0;
	private int gameSpeed;
	private int totalPairs;
	private int foundPairs;
	// Player Storage
	private Queue<Player> playerQueue = new LinkedList<Player>();
	private List<Player> playerList = new ArrayList<Player>();
	public Player currentPlayer;
	// Game Status
	private GameBoard board;
	private boolean isFinished = false;

	/**
	 * 
	 * @param mode    Integer representing which game rules will be followed
	 * @param speed   Integer representing the speed factor of card flip delay time.
	 *                Higher value means shorter delay
	 * @param players Integer representing number of players chosen
	 * @param board   GameBoard instance of current game board
	 */
	public GameManager(int mode, int speed, int players, GameBoard board) {
		this.gameMode = mode;
		this.gameSpeed = speed;
		this.playerCount = players;
		this.board = board;
		totalPairs = board.getBoard().size() / 2;

		// Generates new playerQueue and playerList based on player count setting.
		for (int i = 0; i < playerCount; i++) {
			Player newPlayer = new Player(i + 1);
			playerQueue.add(newPlayer);
			playerList.add(newPlayer);
		}
		currentPlayer = playerQueue.peek(); // Initializes currentPlayer.
	}

	/**
	 * Returns true if the pair of cards have matching names and are not the same
	 * instance.
	 * 
	 * @param card1 Card of first card in pair
	 * @param card2 Card in pair to be compared with card1
	 * @return boolean if matching
	 */
	public boolean compareCards(Card card1, Card card2) {

		if (card1.getName().equals(card2.getName()) && !card1.equals(card2)) {
			return true;
		}
		return false;
	}

	/**
	 * Processes one game turn for the currentPlayer based on the pair of cards they
	 * chose. The player's turn number increases by 1. If the pair is matching, the
	 * pair of cards will be removed from the active game board and the onMatchFound
	 * method is called. Otherwise, the cards are flipped back.
	 * 
	 * @param currentPair List<Card> Storing the pair of selected Cards
	 * @return clearedList List<Card> Empty list allowing for new pair of cards to
	 *         be chosen
	 */
	public List<Card> detectMatch(List<Card> currentPair) {
		Card card1 = currentPair.get(0);
		Card card2 = currentPair.get(1);
		currentPlayer.addTurn();

		if (compareCards(card1, card2)) {
			board.removeMatchingPair(card1, card2); // Remove matching pair from board
			onMatchFound();
		} else {
			// Flip back and end turn.
			card1.flip();
			card2.flip();
			changeTurn();
		}
		// Empties current pair.
		List<Card> clearedList = new ArrayList<Card>();
		return clearedList;
	}

	/**
	 * Poll the current player to the back of the playerQueue and change
	 * currentPlayer to next in Queue.
	 */
	private void changeTurn() {
		Player cur = playerQueue.poll();
		playerQueue.add(cur);
		currentPlayer = playerQueue.peek();
	}

	/**
	 * Triggers when a pair of cards matches. Uses the game mode setting to
	 * determine rules for changing players after a match is found. GameMode 0
	 * (Standard): Player gets extra turn after finding match. GameMode 1
	 * (One-Flip): Players only get one turn at a time.
	 */
	private void onMatchFound() {
		foundPairs += 1; // records number of found pairs to see if game is finished
		if (foundPairs == totalPairs) {
			onGameFinished();
		}
		if (gameMode == 0) {
			currentPlayer.addScore();

		} else if (gameMode == 1) {
			currentPlayer.addScore();
			changeTurn();
		}
	}

	/**
	 * Returns the Player object with the highest score.
	 * 
	 * @return winner Player with highest score
	 */
	public Player getWinner() {
		Player winner = playerList.get(0);
		int max = 0;
		for (int i = 0; i < playerCount; i++) {
			int curScore = playerList.get(i).getScore();
			if (curScore > max) {
				max = curScore;
				winner = playerList.get(i);
			}
		}
		return winner;
	}

	/**
	 * Sets game to finished mode.
	 */
	private void onGameFinished() {
		isFinished = true;
	}

	/**
	 * Returns true if the game is finished.
	 * 
	 * @return boolean isFinished
	 */
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * Getter for currentPlayer.
	 * 
	 * @return Player currentPLayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Getter for current game speed.
	 * 
	 * @return int gameSpeed
	 */
	public int getGameSpeed() {
		return this.gameSpeed;
	}

	/**
	 * Getter for playerList.
	 * 
	 * @return List<Player> playerList
	 */
	public List<Player> getPlayerList() {
		return playerList;
	}

}

/**
 * Player class used for storing individual player data which is displayed in
 * game. Allows multiple users to play at same time and compare scores.
 */
class Player {
	// Player Data
	private int playerNum;
	private int score = 0;
	private int turns = 0;
	private Color color;

	/**
	 * Constructs a new Player with a number identifier.
	 * 
	 * @param num int representing player number in game turn queue
	 */
	public Player(int num) {
		this.playerNum = num;
		// Gives each player a unique color based on their number identifier.
		Color[] colors = { Main.display.getSystemColor(SWT.COLOR_DARK_BLUE),
				Main.display.getSystemColor(SWT.COLOR_DARK_RED), Main.display.getSystemColor(SWT.COLOR_DARK_GREEN),
				Main.display.getSystemColor(SWT.COLOR_DARK_MAGENTA) };
		this.color = colors[playerNum - 1];
	}

	/**
	 * Increases Player score by 1.
	 */
	public void addScore() {
		score += 1;
	}

	/**
	 * Increases Player turn count by 1.
	 */
	public void addTurn() {
		turns += 1;
	}

	/**
	 * Getter for Player score.
	 * 
	 * @return int score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Getter for Player turn count.
	 * 
	 * @return int turns
	 */
	public int getTurns() {
		return this.turns;
	}

	/**
	 * Getter for Player number identifier.
	 * 
	 * @return int playerNum
	 */
	public int getNum() {
		return this.playerNum;
	}

	/**
	 * Getter for Player color.
	 * 
	 * @return Color color
	 */
	public Color getColor() {
		return this.color;
	}

}
