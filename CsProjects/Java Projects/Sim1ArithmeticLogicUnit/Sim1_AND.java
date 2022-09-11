/* Simulates a physical AND gate.
 *
 * Author: Kyle Walker
 */

public class Sim1_AND {
	public void execute() {
		/*
		 * Gets the values of a and b in the AND gate for comparison. If they are both
		 * true (logical AND), then output is true. Otherwise, out will be false.
		 */
		if (a.get() && b.get()) {
			out.set(true);
		} else {
			out.set(false);
		}
	}

	// inputs
	public RussWire a, b; // two inputs
	// output
	public RussWire out; // result of AND on two inputs

	public Sim1_AND()

	// initializes the inputs and output as RussWire components.

	{
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
	}
}
