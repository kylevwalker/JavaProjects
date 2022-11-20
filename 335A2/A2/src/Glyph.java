import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Glyph is an abstract class that serves as the basis of the Composite Pattern
 * for the GUI representation of the document. Everything from the Document,
 * Pages, Columns, Rows, Images, Rects, and Chars extend or override the
 * abstract functionality of this Glyph class. The Composite Glyph Design
 * Pattern allows all UI elements to be treated equally with shared functions,
 * and they are organized in a hierarchical tree so that changes made to a
 * parent can be carried to all of the children recursively. This allows things
 * like the current document Page and all the Rows of text within it to be
 * scrollable.
 * 
 * @author Kyle Walker
 *
 */
public abstract class Glyph implements Iterable<Glyph> {
	public Glyph parent = null;
	protected Point location;
	public ArrayList<Glyph> children;
	protected Point margins;
	protected Point size;
	protected GUI gui = LilLexiMain.gui;
	public int index;

	/**
	 * Uses the graphics context of the current PaintEvent e to draw the Glyph and
	 * its children. Some subclasses inherit the default function of recursively
	 * calling draw for all children, but Leaf Glyphs such as Char, Img, and Rect
	 * have dedicated draw functionality to display themselves. The Page Glyph is
	 * also drawn even though it is not a Leaf.
	 * 
	 * @param e PaintEvent from the GUI which stores gc used for drawing to the
	 *          Canvas
	 */
	public void draw(PaintEvent e) {
		for (Glyph g : children) {
			g.draw(e);
		}
	}

	/**
	 * Checks if the bounds of the glyph contain the coordinate pixels, in which
	 * case it returns an intersecting value of true. Mouse click positions can be
	 * passed as the parameters to determine which Glyph the user has clicked on.
	 * 
	 * @param x int for x position of input on client area
	 * @param y int for y position of input on client area
	 * @return doesIntersect: boolean
	 */
	public boolean intersects(int x, int y) {
		if (this.getBounds().contains(x, y)) {
			return true;
		}
		return false;
	}

	/**
	 * Inserts a given Glyph to the chosen index of a Glyph's children array.
	 * Because inserting new Glyphs corresponds to many changes in the composition
	 * and layout, this function also prepares the child and parent Glyphs to be
	 * accurately composed.
	 * 
	 * @param glyph Glyph child to be added
	 * @param index int index position to insert child into parent's children array
	 */
	public void insert(Glyph glyph, int index) {
		children.add(index, glyph);
		glyph.setParent(this);
		glyph.index = index;
		glyph.setBounds();
		glyph.parent.resize();
	}

	/**
	 * Removes the given Glyph object from the children array of current Glyph.
	 * Resizes the parent Glyph to make up for the lost space from removing child.
	 * 
	 * @param glyph Glyph child to remove from children array
	 */
	public void remove(Glyph glyph) {
		children.remove(glyph);
		glyph.parent.resize();
	}

	/**
	 * Calculates the location and/or size of current Glyph in relation to its
	 * parent's location and other factors used for composing the layout of the
	 * Document.
	 */
	public abstract void setBounds();

	/**
	 * Returns the location and bounds of the Glyph as a Rectangle(location.x,
	 * location.y, size.x, size.y).
	 * 
	 * @return new Rectangle(location.x, location.y, size.x, size.y)
	 */
	public Rectangle getBounds() {
		return new Rectangle(location.x, location.y, size.x, size.y);
	}

	/**
	 * Returns the most specific level Glyph that has been selected at a given
	 * location. Recursively checks if the coordinate input intersects with any
	 * children, until the most specific Glyph is returned. This is used by the
	 * Caret to select any character to edit, or if a less specific Glyph is chosen
	 * it will default to the last character in the document.
	 * 
	 * @param coord Point to check intersections with Glyphs
	 * @return
	 */
	public Glyph getSelected(Point coord) {
		if (intersects(coord.x, coord.y)) {
			for (Glyph g : children) {
				if (g.intersects(coord.x, coord.y)) {
					return g.getSelected(coord);
				}
			}
			return this;
		}
		return null;
	}

	/**
	 * Calculates the new size of the Glyph to accommodate any changes made from
	 * adding/removing children. Each Glyph has a specific resizing algorithm used
	 * to match standard document composition.
	 */
	public abstract void resize();

	/**
	 * Returns the current index of the Glyph as a child in its parent's children
	 * array.
	 * 
	 * @return int index = parent.children.indexOf(this)
	 */
	public int getIndex() {
		index = parent.getChildren().indexOf(this);
		return index;
	}

	/**
	 * Gets the child found at the specified index if one exists.
	 * 
	 * @param index int for specified index within children array
	 * @return children.get(index)
	 */
	public Glyph getChild(int index) {
		if (children == null || children.size() <= index) {
			return null;
		}
		return children.get(index);
	}

	public ArrayList<Glyph> getChildren() {
		return children;
	}

	/**
	 * Returns the ArrayList of Glyphs containing this Glyph's children.
	 * 
	 * @return ArrayList<Glyph> children
	 */
	@Override
	public Iterator<Glyph> iterator() {
		return new ChildIterator().iterator();
	}

	private class ChildIterator implements Iterator<Glyph> {

		private ArrayList<Glyph> children;
		private int index;

		public ChildIterator() {
			this.children = getChildren();
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return this.index >= children.size();
		}

		@Override
		public Glyph next() {
			if (!this.hasNext()) {
				return null;
			}
			this.index += 1;
			return this.children.get(index);
		}

		public Iterator<Glyph> iterator() {

			return children.iterator();
		}
	}

	/**
	 * Parents the given Glyph to the current Glyph by storing its reference in the
	 * parent component.
	 * 
	 * @param parent Glyph to set as parent
	 */
	public void setParent(Glyph parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent of the current Glyph.
	 * 
	 * @return Glyph parent
	 */
	public Glyph getParent() {
		return parent;
	}

}
