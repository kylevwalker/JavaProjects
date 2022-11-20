import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * @author Ryan Pecha
 */



// Command Protocol for initialGameData
public class StreamCommand_InitialGameData extends StreamCommand {
	
	// Command data
	public String mapName;
	public ArrayList<ServerTank> tanks;
	public ArrayList<ClientTank> clientTanks;
	public HashMap<Integer,Integer> idScoreMap;
	
	// For read
	public StreamCommand_InitialGameData() {
		
	}

	// For send
	public StreamCommand_InitialGameData(String mapName, ArrayList<ServerTank> serverTanks) {
		this.mapName = mapName;
		this.tanks = serverTanks;
	}
	
	// setting clientidScoreMap
	public void setClientScoresByID(HashMap<Integer,Integer> idScoreMap) {
		this.idScoreMap = idScoreMap;
	}
	
	// write data protocol
	@Override
	public void writeData(DataOutputStream out) throws IOException {
		// Uses "StartGame" instead of command header
		// sending player fields
		out.writeUTF(this.mapName);
		out.writeInt(tanks.size());
		for (ServerTank tank : this.tanks) {
			out.writeInt(tank.getId());
			out.writeUTF(tank.getType());
			out.writeInt(tank.getR());
			out.writeInt(tank.getG());
			out.writeInt(tank.getB());
			out.writeFloat(tank.getTransform().getPosition().getX());
			out.writeFloat(tank.getTransform().getPosition().getY());
		}
		out.writeInt(idScoreMap.keySet().size());
		// sending player idScoreMap
		for (Integer id : idScoreMap.keySet()) {
			Integer score = idScoreMap.get(id);
			out.writeInt(id);
			out.writeInt(score);
		}
		out.flush();
	}
	
	// read data protocol
	@Override
	public void readData(DataInputStream in) throws IOException {
		// Reading initialGameData
		this.mapName = in.readUTF();
		int tankCount = in.readInt();
		clientTanks = new ArrayList<ClientTank>();
		for (int i = 0; i < tankCount; i++) {
			
			// building all tanks on client side
			int index = in.readInt();
			String type = in.readUTF();
			int r  = in.readInt();
			int g  = in.readInt();
			int b  = in.readInt();
			Color color = new Color(r,g,b);
			float x = in.readFloat();
			float y = in.readFloat();
			Vector2 position = new Vector2(x,y);
			
			ClientTank tank = null;
			
			// tank type
			if (type.equals("Heavy")) {
				tank = new HeavyTankClient(index, color, position);
			}
			if (type.equals("Standard")) {
				tank = new StandardTankClient(index, color, position);
			}
			if (type.equals("Light")) {
				tank = new LightTankClient(index, color, position);
			}
			
			clientTanks.add(tank);
		}
		
		// reading idScoreMap
		idScoreMap = new HashMap<Integer, Integer>();
		int clientCount = in.readInt();
		for (int i = 0; i < clientCount; i++) {
			int id = in.readInt();
			int score = in.readInt();
			idScoreMap.put(id, score);
		}
		
	}
	
	// reader get tanks
	public ArrayList<ClientTank> getClientTanks() {
		return this.clientTanks;
	}
	
}
