import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;

/**
 * The Cursor/Caret/Ibeam used for selecting the area of text to edit. Uses a
 * visual Caret to mark the location, but selects the index within the
 * Composition's array of document contents. User can select specific Glyphs to
 * insert between, or if they click on an area of a less specific Glyph such as
 * the Page it will automatically set its position to last index. This allows
 * the user to quickly add new Glyphs anywhere throughout the visible Document.
 * 
 * @author Kyle Walker
 *
 */
public class IBeam {
	private Point curLocation;
	private Glyph curGlyph;
	private Caret caret;
	private Composition docCTX;
	private int index = 0;

	/**
	 * Constructs a new IBeam/Caret on the Canvas, with a reference to the
	 * Composition's document contents.
	 * 
	 * @param canvas Canvas active canvas from GUI.
	 */
	public IBeam(Canvas canvas) {
		curLocation = new Point(88, 98);
		caret = new Caret(canvas, SWT.NONE);
		caret.setBounds(curLocation.x, curLocation.y, 2, 16);
		docCTX = LilLexiMain.currentDoc.getComposition();
	}

	/**
	 * Selects the Glyph of the current editing location of the Caret. The glyph
	 * parameter is found from the selected Glyph returned from the Mouse Event's
	 * location in the GUI. After Clicking anywhere on the canvas, the Document will
	 * return the most specific Glyph that intersects that location and this method.
	 * This selects that Glyph to set the editing location. If a Leaf is selected,
	 * the caret moves to the area before it for inserting. Otherwise, it defaults
	 * to the last index of the Document contents to add to the end.
	 * 
	 * @param glyph
	 */
	public void select(Glyph glyph) {
		curGlyph = glyph;

		// Selected Glyph is less specific than a Leaf (contains children)
		if (curGlyph.getChildren() != null) {
			while (curGlyph.getChildren() != null && !curGlyph.getChildren().isEmpty()) {
				int rowIndex = curGlyph.getChildren().size() - 1;
				curGlyph = curGlyph.getChild(rowIndex);
				index = docCTX.getChildren().indexOf(curGlyph) + 1;
				setLocation(new Point(curGlyph.getBounds().x + curGlyph.getBounds().width, curGlyph.getBounds().y));
				// Sets Caret to last index to add to end of document.
			}

		}
		// Selected Glyph is a Leaf, so select before it for inserting.
		else {
			setLocation(new Point(curGlyph.getBounds().x, curGlyph.getBounds().y));
			index = docCTX.getChildren().indexOf(curGlyph);

		}
	}

	/**
	 * Inserts the given Glyph into the currently selected index of the Caret.
	 * Automatically selects the area after the inserted Glyph to correctly continue
	 * typing.
	 * 
	 * @param g Glyph to be inserted into the current index of the document contents
	 */
	public void addGlyph(Glyph g) {
		docCTX.insert(g, index);
		index += 1;

		Point newLocation = new Point(g.getBounds().x + g.getBounds().width, g.getBounds().y);
		setLocation(newLocation);
	}

	/**
	 * Removes the Glyph found at the currently selected area of the Caret.
	 * Automatically selects the area after the removed Glyph to correctly continue
	 * deleting.
	 */
	public Glyph removeGlyph() {
		if (index < 1) {
			return null;
		}
		Glyph g = docCTX.getChild(index - 1);
		if (g != null) {
			Point newLocation = new Point(g.getBounds().x, g.getBounds().y);
			docCTX.remove(g);
			index -= 1;
			setLocation(newLocation);
		}
		return g;
	}

	/**
	 * Performs selection by an increment from the given direction. This allows for
	 * moving left and right using arrow keys or other commands that involves
	 * changing selection with key controls.
	 * 
	 * @param direction signed int for the number of indexes to jump from the
	 *                  current index
	 */
	public void shiftOver(int direction) {
		if (index + direction > docCTX.getChildren().size() || index + direction < 0) {
			return;
		}
		if (index + direction == docCTX.getChildren().size()) {
			Glyph g = docCTX.getChild(index);
			index += direction;
			Point newLocation = new Point(g.getBounds().x + g.getBounds().width, g.getBounds().y);
			setLocation(newLocation);
		} else {
			index += direction;
			Glyph g = docCTX.getChild(index);
			if (g != null) {
				Point newLocation = new Point(g.getBounds().x, g.getBounds().y);
				setLocation(newLocation);
			}
		}
	}

	/**
	 * Manually set the selection of the Caret by a given index value. Used in the
	 * Undo/Redo Commands to backtrack any selections made using the IBeam.
	 * 
	 * @param i int for the desired index to set the Caret selection to
	 */
	public void setIndex(int i) {
		if (i > docCTX.getChildren().size() || i < 0) {
			return;
		}
		index = i;
		Glyph g = docCTX.getChild(index);
		if (g != null) {
			select(g);
		} else {
			g = docCTX.getChild(index - 1);
			setLocation(new Point(g.getBounds().x + g.getBounds().width, g.getBounds().y));
		}
	}

	/**
	 * Sets the visual location of the Caret to accurately represent its selected
	 * position. Called whenever a new selection is made to reflect the correct
	 * location of the Caret.
	 * 
	 * @param coord Point for the new location
	 */
	public void setLocation(Point coord) {
		curLocation = coord;
		caret.setLocation(curLocation);
	}

	/**
	 * Returns the current selection index of the Caret in the document's contents.
	 * 
	 * @return int index
	 */
	public int getIndex() {
		return index;
	}

}
