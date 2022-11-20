import java.util.ArrayList;

/**
 * Composition is a Glyph which uses the Strategy Design pattern to compose the
 * visual Glyph hierarchy. The contents of the document are first recorded as an
 * array of arbitrary Glyph objects, but the Composition converts the composed
 * arrangement of Glyphs after the chosen LineBreaking Strategy is called. The
 * Composition uses the Composition Design Pattern to arrange the Glyphs of the
 * documents, using a concrete Compositor Strategy for formatting.
 * 
 * @author Kyle Walker
 *
 */
public class Composition extends Glyph {
	protected Compositor compositor;

	/**
	 * Constructs a Composition to contain the composed Glyph hierarchy by calling
	 * the current Compositor Strategy.
	 * 
	 * @param compositor Compositor concrete Strategy used for formatting Document
	 */
	public Composition(Compositor compositor) {
		children = new ArrayList<Glyph>();
		this.compositor = compositor;
	}

	@Override
	public void insert(Glyph glyph, int index) {
		children.add(index, glyph);
		glyph.index = index;
		// Composes the current arrangement of Glyphs using Compositor, which returns
		// the Composed layout to
		// the current Document.
		compositor.compose(children);

	}

	@Override
	public void remove(Glyph glyph) {
		children.remove(glyph);
		compositor.compose(children);
	}

	@Override
	public void setBounds() {
		// Although a Glyph, Compositions are not stored in the Glyph hierarchy and have
		// no tangible representation
	}

	@Override
	public void resize() {
		// Although a Glyph, Compositions are not stored in the Glyph hierarchy and have
		// no tangible representation
	}
}
