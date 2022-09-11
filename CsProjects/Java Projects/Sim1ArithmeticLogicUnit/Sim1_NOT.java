/* Simulates a physical NOT gate.
 *
 * Author: Kyle Walker
 */

public class Sim1_NOT {
	public void execute() {
		// Negates the input using logical negation.
		out.set(!in.get());
	}

	// input
	public RussWire in; // input to be negated
	// output
	public RussWire out; // negated result

	public Sim1_NOT() {
		// creates RussWire components for input and output variables.
		in = new RussWire();
		out = new RussWire();
	}
}
