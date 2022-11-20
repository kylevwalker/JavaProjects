/**
 * Transform component contains the world position and rotation values for any movable gameObject.
 * position is the location in pixels and rotation is the rotation amount in degrees. Allows for
 * transforms to be translated or rotated for movements.
 * @author Kyle Walker
 *
 */
public class Transform {
	private Vector2 position;

	private float rotation;
	
	/**
	 * Constructs new transform with 0,0 position and 0 rotation
	 */
	Transform(){
		position = new Vector2(0f, 0f);
		rotation = 0f;
	}
	
	/**
	 * Translates the position of the transform by adding the new offset translation to it.
	 * @param translation Vector2 offset to move by
	 */
	public void translate(Vector2 translation) {
		position = position.addVector(translation);
	}
	
	public void translate(float x, float y) {
		position = position.addVector(new Vector2(x, y));
	}
	
	/**
	 * Sets the position to a defined Vector2 position
	 * @param newPos new Vector2 position to set as location
	 */
	public void setPosition(Vector2 newPos) {
		position = newPos;
	}
	
	public void setPosition(float x, float y) {
		position = new Vector2(x, y);
	}
	
	/**
	 * Sets the rotation to a given angle
	 * @param angle to set rotation
	 */
	public void setRotation(float angle) {
		rotation = angle;
	}
	
	/**
	 * Rotate the transform by an angle amount
	 * @param angle to rotate rotation by
	 */
	public void rotate(float angle) {
		rotation += angle;
		if (rotation > 360f || rotation < -360f) {
			rotation = 0f;
		}
	}
	
	
	public Vector2 getPosition() {
		return position;
	}
	
	public float getRotation() {
		return rotation;
	}
}
