import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

/**
 * Rect is a Leaf of the Glyph abstract class that acts as a rectangle shape
 * which can be inserted into documents. A Rect is constructed with a defined
 * size and is inserted into a row to be drawn alongside text. Text will wrap
 * around the rectangle.
 * 
 * @author Kyle Walker
 *
 */
public class Rect extends Glyph {

	/**
	 * Constructs a Rectangle of specified size.
	 * 
	 * @param dimensions Point dimensions containing width and height data
	 */
	public Rect(Point dimensions) {
		location = new Point(dimensions.x, dimensions.y);
		size = new Point(dimensions.x, dimensions.y);
	}

	@Override
	public void draw(PaintEvent e) {
		// Draws the Rectangle outlines
		Color bgColor = gui.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		e.gc.setForeground(bgColor);
		e.gc.drawRectangle(getBounds());
		bgColor.dispose();
	}

	@Override
	public void setBounds() {
		// Location is defined in relation to position in row
		location = new Point(parent.getBounds().x + parent.getBounds().width - (int) (gui.charSize * 1.1),
				parent.getBounds().y);
	}

	@Override
	public void resize() {
		// Does not contain children so has no need to be resized

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
}
