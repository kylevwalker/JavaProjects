import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashSet;

/**
 * A Keyadapter that listens for client input and pushes currently active keys to the server.
 * @author KyleWalker
 *
 */
public class PlayerInput extends KeyAdapter {
	private HashSet<Integer> activeKeyCodes = new HashSet<Integer>();
	private Client client;
	
	/**
	 * Constructs PlayerInput with reference to client.
	 * @param client
	 */
	PlayerInput(Client client){
		this.client = client;
	}
	
	/**
	 * When a key is pressed, add it to active keys and push to server.
	 */
	public void keyPressed(KeyEvent e) {
		activeKeyCodes.add(e.getKeyCode());
		try {
			client.pushUserInput(activeKeyCodes);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	/**
	 * When a key is released, remove it from acitve keys and tell server.
	 */
	public void keyReleased(KeyEvent e) {
		activeKeyCodes.remove(e.getKeyCode());
		try {
			client.pushUserInput(activeKeyCodes);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Returns the active keys being pressed this frame.
	 * @return
	 */
	public HashSet<Integer> getActiveKeyCodes(){
		return activeKeyCodes;
	}

}
