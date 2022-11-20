import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * @author Ryan Pecha
 */



// Stream Command protocol for PlayerData
public class StreamCommand_PlayerData extends StreamCommand {
	
	public String mapName;
	public String gameMode;
	public String tankType;
	public String r;
	public String g;
	public String b;
	
	public StreamCommand_PlayerData() {
		
	};
	
	public StreamCommand_PlayerData(
			
			String mapName,
			String gameMode,
			String tankType,
			String r,
			String g,
			String b
			
			) {
		
		this.mapName = mapName;
		this.gameMode = gameMode;
		this.tankType = tankType;
		this.r = r;
		this.g = g;
		this.b = b;
		
	}

	@Override
	public void writeData(DataOutputStream out) throws IOException {
		out.writeUTF(this.mapName);
		out.writeUTF(this.gameMode);
		out.writeUTF(this.tankType);
		out.writeUTF(this.r);
		out.writeUTF(this.g);
		out.writeUTF(this.b);
		out.flush();
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		this.mapName = in.readUTF();
		this.gameMode = in.readUTF();
		this.tankType = in.readUTF();
		this.r = in.readUTF();
		this.g = in.readUTF();
		this.b = in.readUTF();
	}

}
