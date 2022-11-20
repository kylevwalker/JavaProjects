
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * Img is a Leaf Glyph which draws an Image to the current Document. An image
 * file must be passed through the constructor, but this is controlled from a
 * menu button which contains the given image. Images are inserted to Rows
 * similarly to Rects and Chars.
 * 
 * @author Kyle Walker
 *
 */
public class Img extends Glyph {
	protected Image image;

	/**
	 * Constructs an Image from specified image file.
	 * 
	 * @param dimensions Point dimensions containing width and height data
	 */
	public Img(Image image) {
		this.image = image;
	}

	@Override
	public void draw(PaintEvent e) {
		// Draws the Image
		e.gc.drawImage(image, location.x, location.y);
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
