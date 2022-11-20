import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class StreamCommand {
	public abstract void writeData(DataOutputStream out) throws IOException ;
	public abstract void readData(DataInputStream in) throws IOException ;
}
