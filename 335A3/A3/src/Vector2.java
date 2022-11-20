
/**
 * Vector used in 2d vector calculations for position and transforms.
 * @author Kyle Walker
 *
 */
public class Vector2 {
	private float x;
	private float y;
	
	/**
	 * Constructs new vector 2 with x and y coordinates.
	 * @param x
	 * @param y
	 */
	Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructs new 0,0 vector
	 */
	Vector2(){
		this.x = 0;
		this.y = 0;
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	
	/**
	 * Performs vector addition with other vector
	 * @param other
	 * @return
	 */
	public Vector2 addVector(Vector2 other) {
		return new Vector2(x + other.getX(), y + other.getY());
	}
	
}
