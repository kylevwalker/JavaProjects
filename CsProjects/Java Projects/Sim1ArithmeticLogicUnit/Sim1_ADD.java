/* Simulates a physical device that performs (signed) addition on
 * a 32-bit input.
 *
 * Author: Kyle Walker.
 */

public class Sim1_ADD {
	public void execute() {
		/*
		 * This function simulates the 32 full adders found within the CPU. The Sim1_ADD
		 * class constructor creates 32 bit arrays for the XOR and AND gates found in
		 * each half-adder, then creates a 32 bit array for the OR gate which passes the
		 * carried bit to the 32 bit carry array. This way, the for loop iterates
		 * through each of the 32 bits and for each, executes both half-adder logic
		 * routines passing the second XOR's output to the current sum bit and the OR
		 * output to the current carry bit. The carry bit starts at 0 but is changed
		 * based on the OR of both half adders' AND outputs. Finally, after the loop is
		 * finished, overflow is checked based on the fact that if both a and b have the
		 * same sign but add to a different sign, then overflow must have occurred.
		 */

		// Iterates through all 32 bits of a, b, XOR gates, AND gates, and OR gates of
		// the 32 simulated full-adders.
		for (int i = 0; i < 32; i++) {

			// First half-adder, calculate the XOR of a and b
			xorH1[i].a.set(a[i].get());
			xorH1[i].b.set(b[i].get());
			xorH1[i].execute();

			// First half-adder, find the AND of a and b
			andH1[i].a.set(a[i].get());
			andH1[i].b.set(b[i].get());
			andH1[i].execute();

			// Second half-adder, calculate XOR of carried bit and output of XOR from first
			// half-adder.
			xorH2[i].a.set(carryIn);
			xorH2[i].b.set(xorH1[i].out.get());
			xorH2[i].execute();

			// Second half-adder, find AND of carried bit and output from XOR of first
			// half-adder.
			andH2[i].a.set(carryIn);
			andH2[i].b.set(xorH1[i].out.get());
			andH2[i].execute();

			// Using OR on the outputs from both half-adders' AND gates, we can determine if
			// we are carrying a 1 or not.
			or[i].a.set(andH2[i].out.get());
			or[i].b.set(andH1[i].out.get());
			or[i].execute();

			// Set the result from the OR into the current carry bit for the next
			// calculation in the loop.
			carryIn = or[i].out.get();

			// Sets the current bit in the sum from the result of the XOR gate in the second
			// half-adder, giving correct addition.
			sum[i].set(xorH2[i].out.get());
		}
		// Checks for Carry-out and Overflow. If the current carry bit is 1, then
		// carry-out occurred.
		carryOut.set(carryIn);
		// If the two positives or two negatives resulted in a sum with a different
		// sign, then overflow must have occurred.
		if (a[31].get() == b[31].get() && a[31].get() != sum[31].get())
			overflow.set(true);
		else
			overflow.set(false);
	}

	// inputs
	public RussWire[] a, b;
	// outputs
	public RussWire[] sum;
	public RussWire carryOut, overflow;
	// Logic Gates
	private Sim1_XOR[] xorH1; // XOR gate found in first half-adder (1 XOR gate in each of 32 half-adders)
	private Sim1_XOR[] xorH2; // XOR gate found in second half-adder (1 XOR gate in each of 32 half-adders)
	private Sim1_AND[] andH1; // AND gate found in first half-adder (1 AND gate in each of 32 half-adders)
	private Sim1_AND[] andH2; // AND gate found in second half-adder (1 AND gate in each of 32 half-adders)
	private Sim1_OR[] or; // OR gate found in end of full-adder (Used for passing carried bits to next
							// adder)
	// Helper boolean for carryIn
	public boolean carryIn; // stores current carryIn bit.

	public Sim1_ADD() {
		/*
		 * Initializes all variables for the ADD class. Each input and output, as well
		 * as the logic gates of the adders, are created as 32 bit arrays of RussWires.
		 * The carryIn bit is set to false, as no carrying occurs at LSB for addition
		 * (in this case).
		 */
		a = new RussWire[32];
		b = new RussWire[32];
		sum = new RussWire[32];
		xorH1 = new Sim1_XOR[32];
		xorH2 = new Sim1_XOR[32];
		andH1 = new Sim1_AND[32];
		andH2 = new Sim1_AND[32];
		or = new Sim1_OR[32];
		carryIn = false;

		// Creates a new RussWire in each bit of the 32 bit Arrays so that all bits
		// represent 1 RussWire component. CarryOut and overflow are only used once,
		// so they are single components.
		for (int i = 0; i < 32; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			sum[i] = new RussWire();
			xorH1[i] = new Sim1_XOR();
			xorH2[i] = new Sim1_XOR();
			andH1[i] = new Sim1_AND();
			andH2[i] = new Sim1_AND();
			or[i] = new Sim1_OR();
		}
		carryOut = new RussWire();
		overflow = new RussWire();
	}
}
