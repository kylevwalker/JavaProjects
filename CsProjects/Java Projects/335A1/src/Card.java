
import org.eclipse.swt.graphics.Image;

/**
 * Card class which represents the physical cards used in the memory game. Each card has a front and back side image,
 * and can be flipped between them.
 *
 */
public class Card {

	private String name;
	private Image front; // image on front of card (distinct)
	private Image back; // image on back of card (uniform)
	private Image currentFace;
	private boolean isFlipped;
	
	/**
	 * Constructs a new Card instance with a unique image and name on its front side.
	 * @param newName String identifier for the Card based on image file name
	 * @param newFront Image displayed on front side of card
	 */
	public Card(String newName, Image newFront) {
		
		this.name = newName;
		this.front = newFront;
		this.back = new Image(Main.display, "blank.jpg");
		isFlipped = false; //starts on back side to hide
		currentFace = back;
	}
	
	/**
	 * Changes which side of the card will currently be displayed using a toggle.
	 */
	public void flip() {
		isFlipped = !isFlipped; // Toggles card flip status upon each call
		if (isFlipped) {
			playFlipAnim(front);
		}
		else {
			playFlipAnim(back);
		}
		
	}
	
	/**
	 * Changes the face of the card so that it is displayed on the chosen side.
	 * @param newSide Image of which side Card will flip to
	 */
	private void playFlipAnim(Image newSide) {
		currentFace = newSide; // SWT Image scale animation was too advanced for me
	}
	
	/**
	 * Getter for the Card unique name identifier.
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter for the front side Image of the Card.
	 * @return Image front side Image
	 */
	public Image getFront() {
		return this.front;
	}
	
	/**
	 * Getter for the Card's current face side.
	 * @return Image currentFace
	 */
	public Image getCurrentFace() {
		return this.currentFace;
	}

}
