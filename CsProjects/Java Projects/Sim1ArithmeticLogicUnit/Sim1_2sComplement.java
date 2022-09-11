/* Simulates a physical device that performs 2's complement on a 32-bit input.
 *
 * Author: Kyle Walker
 */

public class Sim1_2sComplement {
	public void execute() {
		/*
		 * This function first iterates through the input number's bits and uses the
		 * Sim1_NOT gate to flip all bit values. Then, it uses the Sim1_ADD component to
		 * add 1 to the negated number, simulating the 2's complement.
		 * 
		 */
		for (int i = 0; i < 32; i++) {
			// Set all input bits to the NOT gate's bits. Then, execute to flip all bits of
			// input, storing the negated number in the 'negated' array.
			not[i].in.set(in[i].get());
			not[i].execute();
			negated[i].set(not[i].out.get());
			// Use the Sim1_ADD to add 1 to the negated number.
			adder.a[i].set(negated[i].get());
			adder.b[i].set(oneConst[i].get());
		}
		adder.execute();
		// Store the output from the 1 added to negated number as the 2's complement out
		// number array.
		for (int i = 0; i < 32; i++) {
			out[i].set(adder.sum[i].get());
		}
	}

	// inputs
	public RussWire[] in; // input number
	public RussWire[] negated; // negated input number
	// output
	public RussWire[] out; // 2's complement result of input
	// components
	public RussWire[] oneConst; // The '1' added is simply a 32 bit number representing 1 (0000 .... 0001).
	private Sim1_NOT[] not; // NOT gate component used in negation
	private Sim1_ADD adder; // ADD gate used for adding 1

	public Sim1_2sComplement() {
		/*
		 * Initializes variables as RussWire Components.
		 */
		in = new RussWire[32];
		negated = new RussWire[32];
		out = new RussWire[32];
		oneConst = new RussWire[32];
		not = new Sim1_NOT[32];
		adder = new Sim1_ADD();

		// Creates a new RussWire in each bit of the 32 bit Arrays so that all bits
		// represent 1 RussWire component.
		for (int i = 0; i < 32; i++) {
			in[i] = new RussWire();
			negated[i] = new RussWire();
			out[i] = new RussWire();
			oneConst[i] = new RussWire();
			not[i] = new Sim1_NOT();

			// Creates a 32 bit number for 1 by setting LSB to 1 and all others to 0.
			if (i == 0)
				oneConst[i].set(true);
			else
				oneConst[i].set(false);
		}
	}
}
