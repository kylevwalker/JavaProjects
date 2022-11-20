
/**
 * This abstract class help to implement the Iterator 
 * design pattern. 
 * It sets the default methods for an iterator, and 
 * is used to parse the children of the glyph class.
 * This is extended within Glyph as ChildIterator.
 * 
 * @author Ryan Pecha
 */
public abstract class Iterator<T> implements Iterable<T> {
	// Method returning the next value
	public abstract T Next();
	// Returns whether or not we have hit the end of our iterator
	public abstract Boolean isDone();
}
