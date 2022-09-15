import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * GameBoard Class representing a board grid of cards from the deck. Stores cards from a deck into
 * a board that the game displays.
 *
 */
public class GameBoard {
	// Card Lists
	private List<Card> currentDeck;
	private CardDeck boardDeck;

	/**
	 * Constructs a new GameBoard instance with a game board created from the given CardDeck.
	 * @param deck CardDeck of Card objects used to fill board
	 */
	public GameBoard(CardDeck deck) {
		currentDeck = deck.getDeckList();
		createBoard();
	}
	
	/**
	 * Creates a board from the current CardDeck. A board is created with enough spaces to fit 2 of each card
	 * from the deck, and each card in each pair of matching cards is placed in a randomized position. 
	 */
	private void createBoard() {
		// Creates enough room to fit 2 of each card.
		Set<Integer> availableLocations = new HashSet<Integer>();
		int totalLocations = 2 * currentDeck.size();
		boardDeck = new CardDeck(new ArrayList<Card>(Collections.nCopies(totalLocations, null)));
		
		// Stores locations as indexes.
		for(int i = 0; i< totalLocations; i++) {
			availableLocations.add(i);
		}
		// Gets next new Card from the deck and creates a copy
		for(int i = 0; i<currentDeck.size(); i++) {
			Card card1 = currentDeck.get(i);
			Card card2 = new Card(card1.getName(), card1.getFront());
			
			// Creates a random index for card 1, then removes that index from available indexes.
			int card1Location = newRandomLocation(availableLocations);
			availableLocations.remove(card1Location);
			// Repeats this for matching card.
			int card2Location = newRandomLocation(availableLocations);
			availableLocations.remove(card2Location);
			
			boardDeck.set(card1Location, card1);
			boardDeck.set(card2Location, card2);
		}
		
	}
	
	/**
	 * Generates a random index for the Cards to randomly populate the GameBoard.
	 * @param locations Set<Integer> of remaining available index locations
	 * @return int random index using randomIndex.nextInt
	 */
	private int newRandomLocation(Set<Integer> locations) {
		List<Integer> currentAvailableLocations = new ArrayList<Integer>();
		Iterator<Integer> tempIterator = locations.iterator(); //Uses Set to avoid duplicates and empty indexes.
		while (tempIterator.hasNext()) {
			currentAvailableLocations.add((int)tempIterator.next());
		}
		// Random int generator
		int max = currentAvailableLocations.size();
		Random randomIndex = new Random();
		return currentAvailableLocations.get(randomIndex.nextInt(max));
	}
	
	/**
	 * Removes a pair of matching cards from the board so they are no longer part of the game.
	 * @param card1 Card first of pair
	 * @param card2 Card matching Card of card1
	 */
	public void removeMatchingPair(Card card1, Card card2) {
		int i = boardDeck.indexOf(card1);
		int j = boardDeck.indexOf(card2);
		// Sets the removed cards' slots to null so the other cards of the grid do not rearrange.
		boardDeck.set(i, null);
		boardDeck.set(j, null);
	}
	
	/**
	 * Getter for the current GameBoard deck.
	 * @return CardDeck boardDeck
	 */
	public CardDeck getBoard(){
		return this.boardDeck;
	}
	
}
