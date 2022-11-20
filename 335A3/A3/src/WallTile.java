import java.awt.*;

/**
 * Represents a tile used to make walls in the map.
 * @author Kyle Walker
 *
 */
public class WallTile {
	private Rectangle bounds;
	static final int TILE_SIZE = ClientUI.GAME_RES_Y /32;
	
	/**
	 * Constructs a new walltile at the indexes in the MapGrid array.
	 * @param x
	 * @param y
	 */
	WallTile(int x, int y){
		int xPos = x * TILE_SIZE;
		int yPos = y * TILE_SIZE;
		bounds = new Rectangle(xPos, yPos, TILE_SIZE, TILE_SIZE);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * Draws the tile as a white square.
	 * @param g
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.white);
		g2d.draw(bounds);
	}
	
}
