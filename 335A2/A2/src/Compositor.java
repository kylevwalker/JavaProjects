import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;

/**
 * The Compositor is an abstract class used to implement concrete Composition
 * Strategies as part of the Strategy Design Pattern. Compositor Strategies use
 * different algorithms to linebreak and compose the contents of the document in
 * order to display them differently on screen. The algorithms iterate through
 * the array of arbitrary Glyphs contained in the Composition's children, then
 * store them in a layout using Pages, Columns, and Rows. The Composed array of
 * visual Glyphs is then passed into the children of the Document to be drawn.
 * 
 * @author Kyle Walker
 *
 */
public abstract class Compositor {
	protected ArrayList<Glyph> composedGlyphs;
	protected IBeam caret;

	/**
	 * Signals for the current Concrete Strategy to Compose the passed document
	 * context.
	 * 
	 * @param context ArrayList<Glyph> from the Composition object containing the
	 *                Document's unformatted contents
	 */
	public abstract void compose(ArrayList<Glyph> context);

	/**
	 * Replaces the current Document's children with the newly composed Glyph
	 * Hierarchy.
	 */
	public void setComposition() {
		LilLexiMain.currentDoc.children = composedGlyphs;
	}
}

/**
 * One Concrete Compositor Strategy which arranges the Document in pages with a
 * single column.
 * 
 * @author Kyle Walker
 *
 */
class SingleColCompositor extends Compositor {
	Page curPage;
	Column curBlock;
	Column curCol;
	Row curRow;
	Document curDoc;

	@Override
	public void compose(ArrayList<Glyph> context) {
		composedGlyphs = new ArrayList<Glyph>();
		caret = LilLexiMain.gui.getCaret();
		curDoc = LilLexiMain.currentDoc;
		// Creates first Page with empty column and row.
		curPage = new Page();
		curDoc.insert(curPage, 0);
		curPage.setBounds();
		composedGlyphs.add(curPage);

		curBlock = new Column();
		curPage.insert(curBlock, 0);
		curCol = new Column();
		curBlock.insert(curCol, 0);
		curRow = new Row();
		curCol.insert(curRow, 0);

		// Stores the total vertical offset of the columns so new columns are created in
		// the right spot.
		Point colOffset = new Point(0, 0);

		// Iterates through all arbitrary Glyphs in document context, then breaks them
		// into columns and rows.
		for (int i = 0; i < context.size(); i++) {
			Char curChar = null;

			// Handle Enter key = new Column
			if (context.get(i) instanceof Char) {
				curChar = (Char) context.get(i);
				if (curChar.character == '\n') {
					colOffset.y += curCol.getBounds().height; // Offset increases by Column size
					curCol = new Column();
					curCol.addOffset(colOffset);
					curRow = new Row();
					curBlock.insert(curCol, curBlock.getChildren().size());
					curCol.insert(curRow, curCol.getChildren().size());
					curRow.insert(context.get(i), curRow.getChildren().size());
					continue;
				}
			}

			// If columns go past page size create new page
			if (curBlock.getBounds().height > curPage.getBounds().height - 50) {
				//curPage = new Page();
				//curBlock = new Column();
				//curCol = new Column();
				//curPage.insert(curBlock, 0);
				//curBlock.insert(curCol, 0);
				//curRow = new Row();
				//curCol.insert(curRow, 0);
				//curDoc.insert(curPage, curDoc.getChildren().size());
				// composedGlyphs.add(curDoc.getChildren().size(), curPage);
				continue;
			}
			// If row is longer than column limit, create a new Row below it and handle
			// Hyphenation.
			if (curRow.getBounds().width > curCol.getBounds().width - 30) {
				Row prevRow = curRow;
				curRow = new Row();
				curCol.insert(curRow, curCol.getChildren().size());
				// Only adds hyphens between a word that is cut off from page size limit.
				if (curChar != null && curChar.character != ' ') {
					prevRow.insert(new Char('-'), prevRow.getChildren().size());
				}
			}

			// Inserts the current Glyph into the active Row.
			curRow.insert(context.get(i), curRow.getChildren().size());

		}
		// Sends the final composed array of visual Glyphs to the Document for drawing.
		setComposition();
	}

}

/**
 * Variant of the LineBreak algorithm which formats the page with 2 columns.
 * 
 * @author Kyle Walker
 *
 */
class DoubleColCompositor extends Compositor {
	Page curPage;
	Column curCol;
	Row curRow;
	Document curDoc;

	@Override
	public void compose(ArrayList<Glyph> context) {
		composedGlyphs = new ArrayList<Glyph>();
		caret = LilLexiMain.gui.getCaret();
		curDoc = LilLexiMain.currentDoc;
		// Creates first Page with empty column and row.
		curPage = new Page();
		curDoc.insert(curPage, 0);
		curPage.setBounds();
		composedGlyphs.add(curPage);

		Column colBlock1 = new Column();
		Column colBlock2 = new Column();
		curPage.insert(colBlock1, 0);
		curPage.insert(colBlock2, 1);
		curCol = new Column();
		colBlock1.insert(curCol, 0);
		colBlock2.addOffset(new Point(400, 0));

		curRow = new Row();
		curCol.insert(curRow, 0);

		Column curBlock = colBlock1;

		// Stores the total vertical offset of the columns so new columns are created in
		// the right spot.
		Point colOffset = new Point(0, 0);

		// Iterates through all arbitrary Glyphs in document context, then breaks them
		// into columns and rows.
		for (int i = 0; i < context.size(); i++) {
			Char curChar = null;

			// Handle Enter key = new Column
			if (context.get(i) instanceof Char) {
				curChar = (Char) context.get(i);
				if (curChar.character == '\n') {
					colOffset.y += curCol.getBounds().height; // Offset increases by Column size
					curCol = new Column();
					curCol.addOffset(colOffset);
					curRow = new Row();
					curBlock.insert(curCol, curBlock.getChildren().size());
					curCol.insert(curRow, curCol.getChildren().size());
					curRow.insert(context.get(i), curRow.getChildren().size());
					continue;
				}
			}
			// Wrap text to second column when first is full.
			if (curBlock.getBounds().height > curPage.getBounds().height - 50) {
				curBlock = colBlock2;
				curCol = new Column();
				curBlock.insert(curCol, 0);
				curPage.insert(curBlock, 1);
				colOffset.x = curCol.getBounds().width / 2 + 30;
				colOffset.y = 25;
				// curCol.addOffset(colOffset);

			}

			// If row is longer than column limit, create a new Row below it and handle
			// Hyphenation.
			if (curRow.getBounds().width > curCol.getBounds().width / 2 - 30) {
				Row prevRow = curRow;
				curRow = new Row();
				curCol.insert(curRow, curCol.getChildren().size());
				// Only adds hyphens between a word that is cut off from page size limit.
				if (curChar != null && curChar.character != ' ') {
					prevRow.insert(new Char('-'), prevRow.getChildren().size());
				}
			}
			// Inserts the current Glyph into the active Row.
			curRow.insert(context.get(i), curRow.getChildren().size());

		}
		// Sends the final composed array of visual Glyphs to the Document for drawing.
		setComposition();
	}
}
