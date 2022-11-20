import java.awt.*;
import java.util.*;

/**
 * Projectiles that spawn from tank turrets and deal damage to other tanks.
 * Travel from the position and direction of the tanks with a defined speed and
 * the damage from the tanks.
 * 
 * @author Kyle Walker
 *
 */
public class ServerProjectile extends Rectangle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Transform transform;
	private Rectangle bounds;
	private int damage;
	static final int PROJECTILE_SIZE = 16;
	private Vector2 moveDirection;
	private float movementSpeed = 350f;
	private MapGrid levelGrid;
	private ArrayList<ServerTank> allTanks;
	private boolean isDestroyed = false;
	private int id;

	/**
	 * Constructs a projectile from the tanks position and facing direction.
	 * 
	 * @param startingPos Starting location from where tank fired projectile
	 * @param travelDir   Direction vector that the tank was facing determining
	 *                    travel direction
	 * @param level       The Level grid used for wall collisions
	 */
	ServerProjectile(Vector2 startingPos, Vector2 travelDir, int projDamage, ArrayList<ServerTank> tanks , MapGrid level) {
		int xPos = (int) startingPos.getX();
		int yPos = (int) startingPos.getY();
		bounds = new Rectangle(xPos, yPos, PROJECTILE_SIZE, PROJECTILE_SIZE);
		transform = new Transform();
		transform.setPosition(startingPos);
		// Sets move vector to the travel direction * Speed
		moveDirection = new Vector2(travelDir.getX() * movementSpeed, travelDir.getY() * movementSpeed);
		levelGrid = level;
		damage = projDamage;
		allTanks = tanks;
	}

	/**
	 * Moves the bullet each frame by its travel direction vector
	 */
	public void move(float timeDelta) {
		Vector2 movement = new Vector2(moveDirection.getX() * timeDelta, moveDirection.getY() * timeDelta);
		checkWallCollision(moveDirection);
		checkTankCollision();
		transform.translate(movement);
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
	 * Checks for walls adjacent to the projectile's current position and direction,
	 * and if the projectile intersects the bounds of any wall tiles it will destroy
	 * itself.
	 * 
	 * @param target The target vector for the projectile's next frame position
	 *               before collision
	 */
	public void checkWallCollision(Vector2 target) {
		Rectangle curBounds = getBounds();
		float xTarget = target.getX();
		float yTarget = target.getY();
		WallTile[] xTiles;
		WallTile[] yTiles;

		float xDir = 0;
		float yDir = 0;

		// Determine which X and Y directions projectile is traveling to know which
		// direction to check for wall tiles.
		if (xTarget > 0) {
			// xDir = Direction.Positive.val;
			xDir = 1;
		} else if (xTarget < 0) {
			// xDir = Direction.Negative.val;
			xDir = -1;
		}
		if (yTarget > 0) {
			// yDir = Direction.Positive.val;
			yDir = 1;
		} else if (yTarget < 0) {
			// yDir = Direction.Negative.val;
			yDir = -1;
		}

		// Sends a collision detection point in horizontal direction then gets the 3
		// adjacent tiles at that position
		Vector2 rayHorizontal = new Vector2(transform.getPosition().getX() + (bounds.width / 2 * xDir),
				transform.getPosition().getY());
		xTiles = levelGrid.getHorizontalTilesAtPosition(rayHorizontal);
		// Checks if any of the tiles intersect the projectile
		for (WallTile xTile : xTiles) {
			if (xTile != null && curBounds.intersects(xTile.getBounds())) {
				destroy();
				break;
			}
		}
		// Sends a collision detection point in vertical direction then gets the 3
		// adjacent tiles at that position
		Vector2 rayVertical = new Vector2(transform.getPosition().getX(),
				transform.getPosition().getY() + (bounds.height / 2 * yDir));
		yTiles = levelGrid.getVerticalTilesAtPosition(rayVertical);
		// Checks if any of the tiles intersect the projectile
		for (WallTile yTile : yTiles) {
			if (yTile != null && curBounds.intersects(yTile.getBounds())) {
				destroy();
				break;
			}
		}
	}

	/**
	 * Checks if the projectile is intersecting with any tanks other than the parent
	 * tank. If so, deal damage to that tank and destroy self.
	 */
	public void checkTankCollision() {
		for (ServerTank tank: allTanks) {
			if (this.getBounds().intersects(tank.getBounds())) {
				System.out.println(this + " just hit tank " + tank + " for" + damage + " damage");
				tank.takeDamage(damage);
				destroy();
			}
		}
	}
	
	
	/**
	 * Stops the projectile from moving and destroys it so it can be removed from
	 * the game.
	 */
	public void destroy() {
		// Stop moving
		moveDirection = new Vector2();
		isDestroyed = true;
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

	/**
	 * Returns the Transform component of the projectile.
	 * 
	 * @return transform
	 */
	public Transform getTransform() {
		return transform;
	}

	/**
	 * Returns true if the bullet is destroyed.
	 * 
	 * @return isDestroyed
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

}
