
import java.util.*;

/**
 * CardDeck class stores a collection of different Card instances based on a
 * shared theme which can be selected as a user setting in the menu. The
 * individual Cards are created and added to the Deck's list which will later be
 * used in the GameBoard's main deck.
 */
class CardDeck extends ArrayList<Card> {

	private List<Card> deckList = new ArrayList<Card>();

	/**
	 * Takes the Cards created from the available decks of the CardDeckHolder and
	 * stores it as the game deck.
	 * 
	 * @param newDeck List<Card> stores a list of Cards from the CardDeckHolder as
	 *                current deck
	 */
	public CardDeck(List<Card> newDeck) {
		this.deckList = newDeck;
	}

	/**
	 * Getter for the DeckList. Returns the contents of the deck as a List of Cards.
	 * 
	 * @return List<Card> deck
	 */
	public List<Card> getDeckList() {
		return this.deckList;
	}

	/**
	 * Returns index of deckList which contains given Card.
	 * @param card Card to find index of
	 * @return
	 */
	public int indexOf(Card card) {
		return deckList.indexOf(card);
	}

	@Override
	public Card get(int i) {
		return deckList.get(i);
	}

	@Override
	public Card set(int index, Card card) {
		return deckList.set(index, card);
	}

	@Override
	public int size() {
		return deckList.size();
	}

}
