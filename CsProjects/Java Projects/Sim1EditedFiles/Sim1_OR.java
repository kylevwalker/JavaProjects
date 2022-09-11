/* Simulates a physical OR gate.
 *
 * Author: Kyle Walker
 */

public class Sim1_OR {
	public void execute() {
		// Uses logical OR to simulate OR of both inputs.
		out.set(a.get() || b.get());
	}

	// inputs
	public RussWire a, b; // inputs to be compared in OR
	// output
	public RussWire out; // output of result

	public Sim1_OR() {
		// Initializes variables as new RussWire components.
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
	}
}
