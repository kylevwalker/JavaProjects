import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;


/**
 * The Row contains the Arbitrary Glyphs which make up the document text. It
 * arranges them horizontally along the page within each Column. Its size grows
 * along with its contents.
 * 
 * @author Kyle Walker
 *
 */
public class Row extends Glyph {

	/**
	 * Constructs a Row with the default size of 0,0.
	 */
	public Row() {
		children = new ArrayList<Glyph>();
		margins = new Point(5, 5);
		size = new Point(0, 0);
	}

	@Override
	public void draw(PaintEvent e) {
		/*
		 * Debugging Draw code 
		 * Color bgColor = gui.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		 * e.gc.setBackground(bgColor); e.gc.fillRectangle(getBounds());
		 * bgColor.dispose();
		 */
		for (Glyph g : children) {
			g.draw(e);
		}
	}

	@Override
	public void setBounds() {
		// Location set by its index within the column
		location = new Point(parent.getBounds().x + margins.x,
				parent.getBounds().y + margins.y + ((int) (gui.charSize * 1.5) * index + margins.y));
	}

	@Override
	/**
	 * Resizes the row based on its contents. Calculates the total width of all
	 * children for the row's length, and uses the maximum height from all of its
	 * child Glyphs to determine height.
	 */
	public void resize() {
		int rowWidth = 0;
		int maxHeight = 0;
		for (Glyph g : children) {
			rowWidth += g.getBounds().width;
			if (g.getBounds().height > maxHeight) {
				maxHeight = g.getBounds().height;
			}
		}
		size.x = rowWidth;
		size.y = maxHeight;
	}

}
