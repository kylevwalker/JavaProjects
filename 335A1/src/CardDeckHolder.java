import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * CardDeckHolder class stores the available lists of Cards from images so that new Decks can be created from 
 * the available decks. New Decks can be added here as long as there image files are present in the source folder
 * and stored in a String[] in the DeckHolder's availableDecks.
 *
 */
public class CardDeckHolder {
	// Store Image Data in separate groups
	String[] imagesFruits = { "apple.jpg", "avocado.jpg", "greenapple.jpg", "peach.jpg", "pear.jpg", "pineapple.jpg" };
	String[] imagesAnimals = { "chicken.jpg", "cow.jpg", "dog.jpg", "duck.jpg" };

	List<CardDeck> availableDecks = new ArrayList<CardDeck>();

	/**
	 * Constructs a new CardDeckHolder which creates new Card Lists from the file arrays and stores them in order
	 * in the availableDecks List.
	 */
	public CardDeckHolder() {
		availableDecks.add(createNewDeck(imagesAnimals));
		availableDecks.add(createNewDeck(imagesFruits));

	}

	/**
	 * Converts an input String[] of filenames into a CardDeck of Card objects.
	 * @param files String[] of filenames to be converted into new Cards
	 * @return newDeck CardDeck of newly created Card objects
	 */
	private CardDeck createNewDeck(String[] files) {
		List<Card> newList = new ArrayList<Card>();
		for (int i = 0; i < files.length; i++) {
			Image newImage = new Image(Main.display, files[i]);
			String imageName = files[i].substring(0, files[i].length() - 4);
			Card newCard = new Card(imageName, newImage);
			newList.add(newCard);
		}
		CardDeck newDeck = new CardDeck(newList);
		return newDeck;
	}

	/**
	 * Getter for the available CardDeck List at the given index.
	 * @param index int index for deck in availableDecks
	 * @return CardDeck found at given index in availableDecks.
	 */
	public CardDeck getAvailableDeck(int index) {
		return availableDecks.get(index);
	}

}
