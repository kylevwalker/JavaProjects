/* Implementation of a 32-bit adder in C.
 *
 * Author: Kyle Walker
 */

#include "sim1.h"

void execute_add(Sim1Data *obj)
{
	/* Executes addition or subtraction based on two inputs, and an input flag of whether or not subtraction is occurring. Using boolean representations of
	* the logical gates inside a full adder, this function examines each input bit and the current carry-in bit to determine the output bit, producing a 32 bit
	* sum number after completing loop. It also stores flags for if the inputs or sum are non-negative, and sets flags for if carry-out or overflow occurred. 
	* All values are taken and stores within the obj pointer to the Sim1Data found in the header.
	*/

	if((obj->a & 0x80000000) == 0){ 	//if the MSB(0x80000000) is not 1, then a is non-negative
		obj->aNonNeg = 1;
	}
	if((obj->b & 0x80000000) == 0){		//if the MSB(0x80000000) is not 1, then b is non-negative
		obj->bNonNeg = 1;
	}
	
	// Declares temporary current carryIn bit. Sets to 1 when isSubtraction to simulate adding 1 in 2's comp.
	int carryIn = 0; 
	if (obj->isSubtraction){
		carryIn = 1;
	}
	// Creates Logical gate components as booleans used in each iteration of addition loop. 
	int bitA;
	int bitB;
	int bitSum = 1; // Sum bit starts as 1 (0000 .... 0001) then shifts up once for each iteration.
	int XORh1;
	int ANDh1;
	int XORh2;
	int ANDh2;

	int i;
	for(i = 0; i < 32; i++){
		// First finds current bits of A and B inputs using shifting and masking.
		bitA = (obj->a >> i) & 0x1;
		bitB = (obj->b >> i) & 0x1;
		// Sets gates off by default.
		XORh1 = 0;
		ANDh1 = 0;
		XORh2 = 0;
		ANDh2 = 0;
		// If subtraction is enabled, each B Bit will be negated for the purposes of adding.
		if (obj->isSubtraction){
			bitB = !bitB; 
		}
		// XOR operation for first half-adder.
		if(bitA ^ bitB){
			XORh1 = 1;
		}
		// AND operation for first half-adder.
		if(bitA & bitB){
			ANDh1 = 1;
		}
		// XOR operation for second half-adder.
		if(carryIn ^ XORh1){
			XORh2 = 1;
		}
		// AND operation for second half-adder.
		if(carryIn & XORh1){
			ANDh2 = 1;
		}
		// AND operation for storing next carry-in bit (Current carryOut).
		if(ANDh1 | ANDh2){
			carryIn = 1;
		}
		else{
			carryIn = 0;
		}
		// Sets sum using XOR of second half adder.
		if(XORh2){
			obj->sum = obj->sum | bitSum; 	// If adder produces 1, a 1 will be added to the current bit.
		}
		bitSum = bitSum << 1; // Shifts the current bit up one to add to next column. 
	}

	obj->carryOut = carryIn; //Sets carryout to current carryIn after loop finishes, so if it is 1 then carryout occurred.
	
	if((obj->sum & 0x80000000) == 0){		//if the MSB(0x80000000) of the sum is not 1, then sum is non-negative
		obj->sumNonNeg = 1;
	}
	// Check for overflow by comparing input signs with output sign. overflow occurs if the two inputs have matching signs but the output does not.
	
	if (obj->isSubtraction){// If we are subtracting, then we check if aNonNeg != bNonNeg because nonNeg does not account for the fact that b was negated.
		if ((obj->aNonNeg != obj->bNonNeg) && obj->sumNonNeg !=obj->aNonNeg){ 
			obj->overflow = 1;
			}
	}
	else{
		if ((obj->aNonNeg == obj->bNonNeg) && obj->sumNonNeg !=obj->aNonNeg){ 
				obj->overflow = 1;
			}
	}
	
}



