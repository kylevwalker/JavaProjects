/* Simulates a physical device that performs (signed) subtraction on
 * a 32-bit input.
 *
 * Author: Kyle Walker
 */

public class Sim1_SUB {
	public void execute() {
		/*
		 * This function uses the Sim1_2sComplement component to negate the subtracted
		 * number (b input) and then uses a Sim1_ADD adder to calculate a minus b (a
		 * plus 2's complement of b) The result is stored in the Sum number.
		 */
		// Sets each bit of b as input for 2's Complement component.
		for (int i = 0; i < 32; i++) {
			negator.in[i].set(b[i].get());
		}
		negator.execute();
		// Sets the negative B number as the output from the 2's complement of b. Then,
		// sets a and negative b as inputs for the adder.
		for (int i = 0; i < 32; i++) {
			negB[i].set(negator.out[i].get());
			adder.a[i].set(a[i].get());
			adder.b[i].set(negB[i].get());
		}
		adder.execute();
		// Stores the result from the adder as the sum for subtraction.
		for (int i = 0; i < 32; i++) {
			sum[i].set(adder.sum[i].get());
		}
	}

	// inputs
	public RussWire[] a, b; // Two inputs

	// output
	public RussWire[] sum; // Result output

	// components
	public RussWire[] negB; // negative b, 2's complement of b input
	private Sim1_2sComplement negator; // Calculates 2's complement for b
	private Sim1_ADD adder; // adds a and negative b to subtract inputs

	public Sim1_SUB() {
		// Initializes the variables as RussWire Components.
		a = new RussWire[32];
		b = new RussWire[32];
		sum = new RussWire[32];
		negB = new RussWire[32];
		negator = new Sim1_2sComplement();
		adder = new Sim1_ADD();

		// Creates a new RussWire in each bit of the 32 bit Arrays so that all bits
		// represent 1 RussWire component.
		for (int i = 0; i < 32; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			sum[i] = new RussWire();
			negB[i] = new RussWire();
		}
	}
}
