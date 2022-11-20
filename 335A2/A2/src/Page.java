import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

/**
 * Page is a Glyph which represents a single page of the document. It contains
 * Columns as children and displays the layout of the document by visible pages.
 * 
 * @author Kyle Walker
 *
 */
public class Page extends Glyph {

	/**
	 * Constructs a Page parented to the document with a fixed size of 700 x 920
	 * pixels.
	 */
	public Page() {
		parent = LilLexiMain.currentDoc;
		children = new ArrayList<Glyph>();
		size = new Point(700, 920);
		margins = new Point(25, 25);
		setBounds();
	}

	@Override
	public void draw(PaintEvent e) {
		// Draws the white page as a background for the characters for WYSIWYG format
		Color bgColor = gui.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		e.gc.setBackground(bgColor);
		e.gc.fillRectangle(getBounds());
		bgColor.dispose();

		for (Glyph g : children) {
			g.draw(e);
		}

	}

	@Override
	public void setBounds() {
		// Sets location in relation to the document's number of pages

		location = new Point(parent.getBounds().x + margins.x, (size.y * index) + margins.y);

	}

	@Override
	public void resize() {
		// Cannot be resized, because the Pages have a uniform size across the document.
	}

}
