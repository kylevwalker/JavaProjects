
import java.util.ArrayList;
import org.eclipse.swt.graphics.Point;

/**
 * Document is the least specific subclass of Glyph which acts as the main
 * parent of all Glyph subclasses. It contains a Composition object to store
 * input and reformat the document contents, and is used to recursively call
 * functions for all visual Glyphs contained in the hierarchy of its children.
 * The Document is not a visible Glyph but rather a Control which stores and
 * draws all visual Glyphs in the Composed Hierarchy.
 * 
 * @author Kyle Walker
 *
 */
public class Document extends Glyph {
	protected Composition activeComposition;

	/**
	 * Constructs a new Document with a new Composition. The Composition is set to
	 * SingleColumn formatting by default.
	 */
	public Document() {
		children = new ArrayList<Glyph>();
		activeComposition = new Composition(new SingleColCompositor()); /// Default composition, can be changed by
																		/// formatting
		// activeComposition = new Composition(new DoubleColCompositor());

		location = new Point(0, 0);
		margins = (new Point(10, 10));
		size = new Point(760, 1000);
	}

	@Override
	public void setBounds() {
		// Follows the location of the Canvas to allow for scrolling and accurate
		// position in Shell.
		location = gui.getCanvas().getLocation();

	}

	@Override
	public void resize() {
		// The Document workspace is as large as the total number of pages contained
		// within it.
		size.y = children.get(0).getBounds().height * children.size();

	}

	/**
	 * Sets the Composition Strategy to the given concrete Compositor Strategy
	 * 
	 * @param compositor Concrete Compositor Strategy
	 */
	public void setComposition(Compositor compositor) {
		activeComposition = new Composition(compositor);
	}

	/**
	 * Returns the active Composition component.
	 * 
	 * @return Composition activeComposition
	 */
	public Composition getComposition() {
		return activeComposition;
	}

}
