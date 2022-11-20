import java.awt.*;
import java.util.*;

/**
 * Abstract class for physical Tank gameObjects used in server Game logic. These Tanks can move,
 * rotate, calculate collisions, shoot, and handle input. They are controlled by each player
 * and there are different subclass types of Tanks for different playstyles.
 * @author Kyle Walker
 *
 */
public abstract class ServerTank extends Rectangle {
	private static final long serialVersionUID = 1L;
	protected Transform transform;
	protected Rectangle bounds;
	protected Color playerColor;
	protected MapGrid levelGrid;
	protected float rotationSpeed;
	protected float movementSpeed;
	protected long reloadTime;
	protected long targetReloadTime = 0;
	protected int health;
	protected int damage;
	protected int id;
	private ArrayList<ServerTank> allTanks;
	protected String type;
	protected boolean isDestroyed = false;
	
	/**
	 * Constructs a server tank and stores important info such as the ID, color, the current level for collisions, and 
	 * the spawn location. All tanks have a bounds of 60x60 square used for all collisions. 
	 * @param idNum	Identification index of current tank in server
	 * @param color The player's chosen color
	 * @param level	The MapGrid containing the wall tiles for collision detection
	 * @param spawnLocation	Vector2 location to spawn the tank on startup
	 */
	ServerTank(int idNum, Color color, MapGrid level, ArrayList<ServerTank> tanks, Vector2 spawnLocation){
		id = idNum;
		playerColor = color;
		bounds = new Rectangle(0,0,60,60);
		transform = new Transform();
		transform.setPosition(spawnLocation);
		levelGrid = level;
		allTanks = tanks;
	}
	
	/**
	 * Rotates the tank by a constant speed around the center so that it is facing a new direction to shoot and travel.
	 * Uses a direction argument to determine which way to turn, and uses the timeDelta to account for differences in framerate.
	 * @param direction	int from -1 to 1 representing turn direction
	 * @param timeDelta	float difference between current and last frame of server calculation in seconds 
	 */
	public void rotate(int direction, float timeDelta) {
		// -1: counterclockwise, 0: none, 1: clockwise
		float rot = rotationSpeed * direction * timeDelta;
		transform.rotate(rot);
	}
	
	/**
	 * Moves the tank by a constant speed in the target direction using deltaTime. Checks for collisions before
	 * applying any movement so that the tank will not enter colliding areas.
	 * @param direction	int between -1 and 1 for moving backwards or forwards
	 * @param timeDelta float difference between current and last frame of server calculation in seconds 
	 */
	public void move(int direction, float timeDelta) {
		
		float magnitude = direction * movementSpeed * timeDelta;
		float x = (float) (magnitude * Math.cos(transform.getRotation()));
		float y = (float) (magnitude * Math.sin(transform.getRotation()));
		Vector2 targetVector = new Vector2(x, y);
				
		// CALC COLLISIONS WITH TARGET VECTOR
		targetVector = calculateCollisionVector(targetVector);
		transform.translate(targetVector);
	}
	
	/**
	 * Fires a new projectile and returns it so it can be stored in the list of all active projectiles. 
	 * The Projectile will travel in the direction the tank is facing.
	 * @return	ServerProjectile instantiated from firing.
	 */
	public ServerProjectile shoot() {
		if (!(System.currentTimeMillis() >= targetReloadTime)) {
			return null;
		}
		targetReloadTime = (System.currentTimeMillis() + reloadTime);
		
		float x = (float) (Math.cos(transform.getRotation()));
		float y = (float) (Math.sin(transform.getRotation()));
		Vector2 travelDirection = new Vector2(x,y);
		Vector2 startingPos = new Vector2(transform.getPosition().getX() + (bounds.width * x), transform.getPosition().getY() + (bounds.height * y));
		return new ServerProjectile(startingPos, travelDirection, damage, allTanks, levelGrid);
	}
	
	public void takeDamage(int damage) {
		System.out.println(this + "took " + damage + " damage");
		health -= damage;
		if (health <=0) {
			destroy();
		}
	}
	
	/**
	 * Allows health to be changed for game modes.
	 * @param newHP
	 */
	public void setHealth(int newHP) {
		health = newHP;
	}

	/**
	 * Returns the current health
	 * @return
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Destroys this tank instance
	 */
	private void destroy() {
		System.out.println("Tank Destroyed");
		isDestroyed = true;
	}
	

	/**
	 * Returns the next position of the tank while accounting for any collisions detected in the way.
	 * Searches the adjacent wall tiles in the moving x and y direction of the tank and if any of them
	 * will collide after moving to the target position, then return an updated Vector that does not move 
	 * into the colliding area.
	 * @param target The next planned position of the tank before applying collisions
	 * @return	The updated target vector accounting for collisions
	 */
	private Vector2 calculateCollisionVector(Vector2 target) {
		// Check if the target vector will intersect a wall in its direction. If so, set it to 0;
		float xTarget = target.getX();
		float yTarget = target.getY();
		float xNew = xTarget;
		float yNew = yTarget;
		
		Rectangle curBounds = getBounds();
		
		WallTile[] xTiles;
		WallTile[] yTiles;
		
		float xDir = 0;
		float yDir = 0;
		
		if (xTarget > 0) {
			xDir = 1;
		}
		else if (xTarget<0) {
			xDir = -1;
		}
		if (yTarget > 0) {
			yDir = 1;
		}
		else if (yTarget < 0) {
			yDir = -1;
		}
		// Sends a collision detection point in horizontal direction then gets the 3
		// adjacent tiles at that position
		Vector2 rayHorizontal = new Vector2(transform.getPosition().getX() + (bounds.width/2 * xDir), transform.getPosition().getY());
		xTiles = levelGrid.getHorizontalTilesAtPosition(rayHorizontal);
		for (WallTile xTile : xTiles) {
			if (xTile != null && curBounds.intersects(xTile.getBounds())) {
				xNew = 0;
				break;
			}
		}
		// Sends a collision detection point in vertical direction then gets the 3
		// adjacent tiles at that position
		Vector2 rayVertical = new Vector2(transform.getPosition().getX(), transform.getPosition().getY() + (bounds.height/2 * yDir));
		yTiles = levelGrid.getVerticalTilesAtPosition(rayVertical);
		for (WallTile yTile : yTiles) {
			if (yTile != null && curBounds.intersects(yTile.getBounds())) {
				yNew = 0;
				break;
			}
		}

		return new Vector2(xNew, yNew);
	}
	
	/**
	 * Returns the rectangle bounds for the tank's collider at its current position.
	 */
	public Rectangle getBounds() {
		int xPos = (int)transform.getPosition().getX() - bounds.width / 2;
		int yPos = (int)transform.getPosition().getY() - bounds.height / 2;
		bounds = new Rectangle(xPos, yPos, bounds.width, bounds.height);
		return bounds;
	}
	
	/**
	 * Handles all current input controls for the tank from the server's HashSet of currently
	 * pressed keycodes. Returns a projectile if firing occurred so the server can add it to the list.
	 * @param codes
	 * @param timeDelta
	 * @return
	 */
	public ServerProjectile handleInput(HashSet<Integer> codes, float timeDelta) {
		/// WASD OR ARROW KEYS. 
		// ARROW KEYCODES: LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40.
		// WASD Keycodes: W: 87, A: 65, S: 83, D: 68
		//System.out.println(e.getKeyCode() + " " + e.getKeyChar() + " " );
		
		for (int keyCode : codes) {
			// Forward
			if (keyCode == 87 || keyCode == 38) {
				//move(Direction.Positive, timeDelta);
				move(1, timeDelta);
			}
			// Left
			if (keyCode == 65 || keyCode == 37) {
				//rotate(Direction.Negative, timeDelta);
				rotate(-1, timeDelta);
			}
			// Right
			if (keyCode == 68 || keyCode == 39) {
				//rotate(Direction.Positive, timeDelta);
				rotate(1, timeDelta);
			}
			// Back
			if (keyCode == 83 ||keyCode == 40) {
				//move(Direction.Negative, timeDelta);
				move(-1, timeDelta);
			}
			// Shoot
			if (keyCode == 32) {
				return shoot();
				
			}
		}
		return null;
	}
	
	/**
	 * Returns the tanks transform component.
	 * @return transform
	 */
	public Transform getTransform() {
		return transform;
	}
	
	/**
	 * Returns this tanks index id 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns color.r
	 * @return
	 */
	public int getR() {
		return playerColor.getRed();
	}
	/**
	 * Returns color.g
	 * @return
	 */
	public int getG() {
		return playerColor.getGreen();
	}
	/**
	 * Returns color.b
	 * @return
	 */
	public int getB() {
		return playerColor.getBlue();
	}
	/**
	 * Returns tank type string
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns true if the tank is destroyed.
	 * @return
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
}

/**
 * The Standard tank has medium health, speed, and damage with a medium fire rate.
 * @author Kyle Walker
 *
 */
class StandardTankServer extends ServerTank{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StandardTankServer(int idNum, Color color, MapGrid level, ArrayList<ServerTank> tanks, Vector2 spawnLocation) {
		super(idNum, color, level, tanks, spawnLocation);
		rotationSpeed = 3.4f;
		movementSpeed = 150f;		//2.6
		reloadTime = 1000;
		health = 2;
		damage = 2;
		type = "Standard";
	}
}

/**
 * The Heavy tank has high health, low speed, and high damage with a slow fire rate.
 * @author Kyle Walker
 *
 */
class HeavyTankServer extends ServerTank{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HeavyTankServer(int idNum, Color color, MapGrid level, ArrayList<ServerTank> tanks, Vector2 spawnLocation) {
		super(idNum, color, level, tanks, spawnLocation);
		rotationSpeed = 3.4f;
		movementSpeed = 100f;		//2.6
		reloadTime = 2000;
		health = 3;
		damage = 3;
		type = "Heavy";
	}
}

/**
 * The Light tank has low health, high speed, and low damage with a fast fire rate.
 * @author Kyle Walker
 *
 */
class LightTankServer extends ServerTank{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	LightTankServer(int idNum, Color color, MapGrid level, ArrayList<ServerTank> tanks, Vector2 spawnLocation) {
		super(idNum, color, level, tanks, spawnLocation);
		rotationSpeed = 3.4f;
		movementSpeed = 220f;		//2.6
		reloadTime = 500;
		health = 1;
		damage = 1;
		type = "Light";
	}
}

