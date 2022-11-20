import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Char is a Leaf Glyph which is used to represent a single character of text in
 * the document. As Leaf nodes in the hierarchy, they override certain functions
 * involving children because they will never have any children. Chars have
 * fields for text size and font which follow the settings of the Document.
 * These can be changed with the menu buttons.
 * 
 * @author Kyle Walker
 *
 */
public class Char extends Glyph {
	public Character character;
	private Point location;
	protected boolean correctSpelling;

	/**
	 * Constructs a new Char representing the given character c. Assumes correct
	 * spelling by default until the Iterator finds errors.
	 * 
	 * @param c char to assign to the Char's character field, which will be
	 *          displayed as text in Document
	 */
	public Char(char c) {
		character = c;
		correctSpelling = true;
		location = new Point(0, 0);
	}

	@Override
	/**
	 * Draws the character on screen following the Font style and Color assigned by
	 * the Document settings. Uses the gc of the PaintEvent parameter e to draw the
	 * char as a string. Correctly spelled words are drawn in black text while
	 * incorrect words are marked red. Overrides Glyph.draw(PaintEvent e).
	 */
	public void draw(PaintEvent e) {
		// Set text red on incorrect word
		if (!correctSpelling) {
			Color fgColor = gui.getDisplay().getSystemColor(SWT.COLOR_RED);
			e.gc.setForeground(fgColor);
			fgColor.dispose();
		}
		// Set text color to black for correctly spelled words
		else {
			Color fgColor = gui.getDisplay().getSystemColor(SWT.COLOR_BLACK);
			e.gc.setForeground(fgColor);
			fgColor.dispose();
		}

		// Set font to current Document standard.
		Font charFont = new Font(gui.getDisplay(), gui.font, gui.charSize, SWT.BOLD);
		e.gc.setFont(charFont);
		e.gc.drawString("" + character, getBounds().x, getBounds().y);
		charFont.dispose();
	}

	@Override
	public void setBounds() {
		// Sets location of the Char to the current position in the row using the size
		// of the Row.
		location = new Point(parent.getBounds().x + parent.getBounds().width - gui.charSize, parent.getBounds().y);
	}

	@Override
	public void resize() {
		// No use for Leaf Glyphs.

	}

	@Override
	public Glyph getSelected(Point coord) {
		// As a Leaf, it will be the most specific Glyph to be selected so if selected,
		// it returns itself.
		if (intersects(coord.x, coord.y)) {
			return this;
		}
		return null;
	}

	@Override
	public Rectangle getBounds() {
		// Uses charSize as the width and height of the bounds.
		return new Rectangle(location.x, location.y, gui.charSize, gui.charSize);
	}
}
