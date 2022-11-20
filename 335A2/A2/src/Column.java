import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;

/**
 * The Column is a Glyph which has Rows as children. It arranges Rows vertically
 * and allows for reformatting into single or double column layouts. Pressing
 * Enter to create a new line will create a new Column with indentation.
 * 
 * @author Kyle Walker
 *
 */
public class Column extends Glyph {
	protected Point offset;

	/**
	 * Constructs a Column and initializes its measurements using default values.
	 */
	public Column() {
		children = new ArrayList<Glyph>();
		margins = new Point(20, 20);
		size = new Point(16, 16);
		offset = new Point(0, 0);

	}

	@Override
	public void draw(PaintEvent e) {
		/* Debugging draw code
		 * Color bgColor = gui.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		 * e.gc.setBackground(bgColor); e.gc.fillRectangle(getBounds());
		 * bgColor.dispose();
		 */
		for (Glyph g : children) {
			g.draw(e);
		}
	}

	@Override
	public void setBounds() {
		// Bounds are calculated as an area inset from the page by the margins.
		// size = new Point(parent.getBounds().width - (margins.x*2),
		// parent.getBounds().height - (margins.y*2));
		size = new Point(parent.getBounds().width - (margins.x * 2), gui.charSize + margins.y);
		location = new Point(parent.getBounds().x + margins.x + offset.x, parent.getBounds().y + margins.y + offset.y);

	}

	@Override
	public void resize() {
		// Increases the size of the column based on size of all row contents within its
		// children.
		int maxHeight = 0;
		for (Glyph g : children) {
			maxHeight += g.getBounds().height;
		}
		size.y = maxHeight + margins.y;
	}

	/**
	 * Allows the Compositor to adjust the offset of the Column as it depends on the
	 * knowledge of the size of the column before it rather than a predictable
	 * height value.
	 * 
	 * @param offset Point containing x and y pixel offset for the column
	 */
	public void addOffset(Point offset) {
		this.offset = offset;
	}

}
