
import java.util.*;

import org.eclipse.swt.graphics.Image;

/**
 * CardDeck class stores a collection of different Card instances based on a shared theme which can be
 * selected as a user setting in the menu. The individual Cards are created and added to the Deck's list
 * which will later be used in the GameBoard's main deck.
 */
public class CardDeck {
	// Deck Theme Filename Groups
	private String[] imagesFruits= {"apple.jpg", "avocado.jpg", "greenapple.jpg",
			"peach.jpg", "pear.jpg", "pineapple.jpg"}; 
	private String[] imagesAnimals = {"chicken.jpg", "cow.jpg", "dog.jpg", "duck.jpg"};
	
	private List<String[]> availableDecks = new ArrayList<String[]>();
	public List<Card> deck = new ArrayList<Card>();
	
	/**
	 * Constructs a new CardDeck instance from the chosen deck theme.
	 * @param deckNumber int representing index of chosen deck theme
	 */
	public CardDeck(int deckNumber) {
		// Stores all possible decks and uses deckNumber to choose one.
		availableDecks.add(imagesAnimals);
		availableDecks.add(imagesFruits);
	
		// Creates each new card from the image files and stores in deck.
		String[] imageFiles = availableDecks.get(deckNumber);
		for (int i = 0; i<imageFiles.length; i++) {
				Image newImage = new Image(Main.display, imageFiles[i]); 
				String imageName = imageFiles[i].substring(0, imageFiles[i].length()-4);
				Card newCard = new Card(imageName, newImage);
				deck.add(newCard);
		}
	}
	
	/**
	 * Getter for the DeckList. Returns the contents of the deck as a List of Cards.
	 * @return List<Card> deck
	 */
	public List<Card> getDeckList(){
		return this.deck;
	}
	
	/**
	 * Prints the current Deck to check contents.
	 */
	public void printDeck() {
		for(int i = 0; i<deck.size(); i++) {
			System.out.println(deck.get(i).getName());
		}
	}
}
