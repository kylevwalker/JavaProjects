import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

public class StreamCommand_UserInput extends StreamCommand {
	
	private HashSet<Integer> activeKeyCodes;
	
	public StreamCommand_UserInput() {
		activeKeyCodes = new HashSet<Integer>();
	}
	
	public StreamCommand_UserInput(HashSet<Integer> activeKeyCodes) {
		this.activeKeyCodes = activeKeyCodes;
	}

	@Override
	public void writeData(DataOutputStream out) throws IOException {
		int size = activeKeyCodes.size();
		out.writeUTF("UserInput");
		out.writeInt(size);
		for (Integer key : activeKeyCodes) {
			out.writeInt(key);
		}
		out.flush();
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		activeKeyCodes.clear();
		int inputSize = in.readInt();
		for (int i = 0; i < inputSize; i++) {
			activeKeyCodes.add(in.readInt());
		}
	}
	
	public HashSet<Integer> getActiveKeyCodes() {
		return this.activeKeyCodes;
	}
}
