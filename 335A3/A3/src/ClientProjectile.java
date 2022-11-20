import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * Visual representation of projectiles used for clientside UI model. Allows
 * server to update the position of the projectile and for client to draw each
 * frame.
 * 
 * @author Kyle Walker
 *
 */
public class ClientProjectile {
	protected Transform transform;
	protected Rectangle bounds;
	private int id;

	/**
	 * Constructs a new Client Projectile graphic at the given position, and sets
	 * the size.
	 * 
	 * @param position Starting position in game to first draw the projectile.
	 */
	ClientProjectile(Vector2 position) {
		transform = new Transform();
		transform.setPosition(position);
		bounds = new Rectangle((int) position.getX(), (int) position.getY(), ServerProjectile.PROJECTILE_SIZE,
				ServerProjectile.PROJECTILE_SIZE);
	}

	/**
	 * Sets the current position of the projectile to the given coordinates each
	 * frame.
	 * 
	 * @param x X coordinate in pixels
	 * @param y Y coordinate in pixels
	 */
	public void setPosition(float x, float y) {
		transform.setPosition(x, y);
	}
	
	/**
	 * Returns the rectangular collision bounds of the projectile, updated to match
	 * the current position.
	 */
	public Rectangle getBounds() {
		int xPos = (int) transform.getPosition().getX() - bounds.width / 2;
		int yPos = (int) transform.getPosition().getY() - bounds.height / 2;
		bounds = new Rectangle(xPos, yPos, bounds.width, bounds.height);
		return bounds;
	}

	/**
	 * Draws a white circle for the projectile at the current position.
	 * 
	 * @param g graphics component from client Panel
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle curBounds = getBounds();
		int xPos = (int) curBounds.x;
		int yPos = (int) curBounds.y;

		g2d.setColor(Color.orange);
		g2d.fillOval(xPos, yPos, bounds.width, bounds.height);

	}

	/**
	 * Set the id of the projectile for identification
	 * 
	 * @param index the chosen index
	 */
	public void setIndex(int index) {
		id = index;
	}

	/**
	 * Returns the projectile's id
	 * 
	 * @return id
	 */
	public int getIndex() {
		return id;
	}
}
