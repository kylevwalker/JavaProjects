
// this class implements the current state of a program
public class ProgramState {
	
	// possible program states for a client or server
	public static enum STATE{
		MainMenu,
		ReadyToPlay,
		InGame,
		Terminated
	}
	
	// current state of the server
	private STATE currentState;
	
	// constructor
	public ProgramState() {
		this.currentState = STATE.MainMenu;
	}
	
	// returning the current state
	public STATE getState() {
		return this.currentState;
	}
	
	// setting the new state
	public void setState(STATE state) {
		this.currentState = state;
	}
	
}
