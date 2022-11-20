import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * The Clientside tank graphics used for displaying the different tank sides on the game screen. Each type has a different
 * design to reflect the stats. 
 * @author Kyle Walker
 *
 */
public abstract class ClientTank {
	protected Color tankColor;
	protected Transform transform;
	protected Rectangle bounds;
	protected int id;
	
	/**
	 * Constructs a new Client Tank with the given ID and player color, at the given position
	 * @param id	Identification index
	 * @param color	player tank color chosen from menu
	 * @param position	position to first display the tank
	 */
	ClientTank(int id, Color color, Vector2 position){
		this.id = id;
		tankColor = color;
		bounds = new Rectangle(0,0,60,60);
		transform = new Transform();
		transform.setPosition(position);
	}
		
	/**
	 * Returns the bounds of the tank with updated position values.
	 * @return
	 */
	public Rectangle getBounds() {
		int xPos = (int)transform.getPosition().getX() - bounds.width / 2;
		int yPos = (int)transform.getPosition().getY() - bounds.height / 2;
		bounds = new Rectangle(xPos, yPos, bounds.width, bounds.height);
		return bounds;
	}
	
	/**
	 * Returns the ID index
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns this tank's player color
	 * @return tankColor
	 */
	public Color getColor() {
		return tankColor;
	}
	
	/**
	 * Allows server to set the location of the tank to reflect the updated position from Server Tank's movement.
	 * @param x	X coordinate
	 * @param y Y coordinate
	 */
	public void setPosition(int x, int y) {
		transform.setPosition(x, y);
	}
	
	/**
	 * Allows server to set the new rotation of tank to reflect rotation made from Server Tank's rotation.
	 * @param rot rotation in angles of desired final rotation.
	 */
	public void setRotation(float rot) {
		transform.setRotation(rot);
	}
	
	/**
	 * Draws the tank, overridden by subclasses for unique designs per type.
	 * @param g Graphics component
	 */
	public abstract void draw(Graphics g);
	
}

/**
 * Standard version of the tank has a regular medium design.
 * @author Kyle Walker
 *
 */
class StandardTankClient extends ClientTank{
	
	StandardTankClient(int id, Color color, Vector2 position) {
		super(id, color, position);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Rectangle curBounds = getBounds();
		int xPos = (int)transform.getPosition().getX();
		int yPos = (int)transform.getPosition().getY();

		Rectangle tankTreads = new Rectangle(xPos - curBounds.height /2, yPos - curBounds.width /2, curBounds.height, curBounds.width);
		Rectangle tankBody = new Rectangle(curBounds.x, curBounds.y + 10, curBounds.width, curBounds.height-20);
		Rectangle tankGun = new Rectangle(xPos, yPos - curBounds.height /8, curBounds.width, curBounds.height /5);

		// Debugging draw collision bounds
			//g2d.setColor(Color.magenta);
			//g2d.draw(curBounds);
		// Rotate around transform position point (Center of bounds)
		AffineTransform originalOrientation = g2d.getTransform();
		g2d.rotate(transform.getRotation(), transform.getPosition().getX(), transform.getPosition().getY());
		// Draw Tank Graphics
		g2d.setStroke(new BasicStroke(2));
		// Treads
		g2d.setColor(Color.darkGray);
		g2d.fill(tankTreads);
		g2d.setColor(Color.white);
		g2d.draw(tankTreads);
		// Body
		g2d.setColor(tankColor);
		g2d.fill(tankBody);
		g2d.setColor(Color.white);
		g2d.draw(tankBody);
		// Gun
		g2d.setColor(tankColor);
		g2d.fill(tankGun);
		g2d.setColor(Color.white);
		g2d.draw(tankGun);
		// Reset g rotation
		g2d.setTransform(originalOrientation);
	}
}

/**
 * Heavy version of the tank has a larger and bulkier design to reflect greater damage and armor.
 * @author KyleWalker
 *
 */
class HeavyTankClient extends ClientTank{
	
	HeavyTankClient(int id, Color color, Vector2 position) {
		super(id, color, position);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Rectangle curBounds = getBounds();
		int xPos = (int)transform.getPosition().getX();
		int yPos = (int)transform.getPosition().getY();

		int treadWidth = curBounds.width + 10;
		Rectangle tankTreads = new Rectangle(xPos - curBounds.height /2, yPos - treadWidth/2, curBounds.height, treadWidth);
		int bodWidth = curBounds.height -10;
		Rectangle tankBody = new Rectangle(curBounds.x, curBounds.y + 5, curBounds.width, bodWidth);
		int turWidth = curBounds.height /3;
		Rectangle tankGun = new Rectangle(xPos, yPos - turWidth/2, curBounds.width, turWidth);

		// Debugging draw collision bounds
			//g2d.setColor(Color.magenta);
			//g2d.draw(curBounds);
		// Rotate around transform position point (Center of bounds)
		AffineTransform originalOrientation = g2d.getTransform();
		g2d.rotate(transform.getRotation(), transform.getPosition().getX(), transform.getPosition().getY());
		// Draw Tank Graphics
		g2d.setStroke(new BasicStroke(2));
		// Treads
		g2d.setColor(Color.darkGray);
		g2d.fill(tankTreads);
		g2d.setColor(Color.white);
		g2d.draw(tankTreads);
		// Body
		g2d.setColor(tankColor);
		g2d.fill(tankBody);
		g2d.setColor(Color.white);
		g2d.draw(tankBody);
		// Gun
		g2d.setColor(tankColor);
		g2d.fill(tankGun);
		g2d.setColor(Color.white);
		g2d.draw(tankGun);
		// Reset g rotation
		g2d.setTransform(originalOrientation);
	}
}

/**
 * Light version of the tank is slimmer and has a smaller gun to reflect high speed and low armor with low damage.
 * @author Kyle Walker
 *
 */
class LightTankClient extends ClientTank{
	
	LightTankClient(int id, Color color, Vector2 position) {
		super(id, color, position);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Rectangle curBounds = getBounds();
		int xPos = (int)transform.getPosition().getX();
		int yPos = (int)transform.getPosition().getY();

		int treadWidth = curBounds.width - 10;
		Rectangle tankTreads = new Rectangle(xPos - curBounds.height /2, yPos - treadWidth/2, curBounds.height, treadWidth);
		int bodWidth = curBounds.height -26;
		Rectangle tankBody = new Rectangle(curBounds.x, curBounds.y + 13, curBounds.width, bodWidth);
		int turWidth = curBounds.height /8;
		Rectangle tankGun = new Rectangle(xPos, yPos - turWidth/2, curBounds.width, turWidth);

		// Debugging draw collision bounds
			//g2d.setColor(Color.magenta);
			//g2d.draw(curBounds);
		// Rotate around transform position point (Center of bounds)
		AffineTransform originalOrientation = g2d.getTransform();
		g2d.rotate(transform.getRotation(), transform.getPosition().getX(), transform.getPosition().getY());
		// Draw Tank Graphics
		g2d.setStroke(new BasicStroke(2));
		// Treads
		g2d.setColor(Color.darkGray);
		g2d.fill(tankTreads);
		g2d.setColor(Color.white);
		g2d.draw(tankTreads);
		// Body
		g2d.setColor(tankColor);
		g2d.fill(tankBody);
		g2d.setColor(Color.white);
		g2d.draw(tankBody);
		// Gun
		g2d.setColor(tankColor);
		g2d.fill(tankGun);
		g2d.setColor(Color.white);
		g2d.draw(tankGun);
		// Reset g rotation
		g2d.setTransform(originalOrientation);
	}
}
