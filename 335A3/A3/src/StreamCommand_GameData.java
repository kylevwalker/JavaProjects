import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StreamCommand_GameData extends StreamCommand {
	
	public int tankCount;
	public int projectileCount;
	
	// data coming out of command - on client
	private ArrayList<ClientTank> clientTanksReference;
	private HashMap<Integer, ClientTank> clientTanks;
	private ArrayList<ClientProjectile> clientProjectilesReference;
	private	HashMap<Integer, ClientProjectile> clientProjectiles;
	
	// data going in to command - on server
	private ArrayList<ServerTank> serverTanks;
	private	ArrayList<ServerProjectile> serverProjectiles; 
	
	public StreamCommand_GameData() {
		
	}
	
	public void setClientTanks(ArrayList<ClientTank> myclientTanks) {
		this.clientTanksReference = myclientTanks;
		this.clientTanks = new HashMap<Integer, ClientTank>();
		for (ClientTank clientTank : myclientTanks) {
			this.clientTanks.put(clientTank.getId(), clientTank);
		}
	}
	
	public void setServerTanks(ArrayList<ServerTank> myServerTanks) {
		this.serverTanks = myServerTanks;
	}
	
	public void setClientProjectiles(ArrayList<ClientProjectile> myProjectiles) {
		clientProjectilesReference = myProjectiles;
		this.clientProjectiles = new HashMap<Integer, ClientProjectile>();
		for (ClientProjectile projectile : myProjectiles) {
			this.clientProjectiles.put(projectile.getIndex(), projectile);
		}
	}
	
	public void setServerProjectiles(ArrayList<ServerProjectile> myProjectiles) {
		this.serverProjectiles = myProjectiles;
	}

	@Override
	public void writeData(DataOutputStream out) throws IOException {
		out.writeUTF("GameData");
		tankCount = serverTanks.size();
		out.writeInt(tankCount);
		for (ServerTank tank : this.serverTanks) {
			out.writeInt(tank.getId());
			out.writeFloat(tank.getTransform().getPosition().getX());
			out.writeFloat(tank.getTransform().getPosition().getY());
			out.writeFloat(tank.getTransform().getRotation());
			boolean destroyed = tank.isDestroyed();
			out.writeBoolean(destroyed);
		}
		
		projectileCount = serverProjectiles.size();
		out.writeInt(projectileCount);
		for (ServerProjectile projectile : serverProjectiles) {
			out.writeInt(projectile.getIndex());
			out.writeBoolean(projectile.isDestroyed());
			out.writeFloat(projectile.getTransform().getPosition().getX());
			out.writeFloat(projectile.getTransform().getPosition().getY());
		}
		out.flush();
		
	}

	@Override
	public void readData(DataInputStream in) throws IOException {
		tankCount = in.readInt();
		for (int i = 0; i < tankCount; i++) {
			int id = in.readInt();
			float x = in.readFloat();
			float y = in.readFloat();
			float rotation = in.readFloat();
			boolean destroyed = in.readBoolean();
			if (destroyed) {				
				this.clientTanksReference.remove(clientTanks.get(id));
				this.clientTanks.remove(id);
			}
			else {
				clientTanks.get(id).transform.setPosition(x, y);
				clientTanks.get(id).transform.setRotation(rotation);
			}
		}
		
		projectileCount = in.readInt();
		for (int i = 0; i < projectileCount; i++) {
			int index = in.readInt();
			boolean destroyed = in.readBoolean();
			float x = in.readFloat();
			float y = in .readFloat();
			if (clientProjectiles.containsKey(index)){				
				ClientProjectile projectile = clientProjectiles.get(index);
				if (destroyed) {
					clientProjectiles.remove(index);
					clientProjectilesReference.remove(projectile);
				}
				else {
					projectile.setPosition(x, y);
				}
			}
			else {
				Vector2 position = new Vector2(x,y);
				ClientProjectile projectile = new ClientProjectile(position);
				projectile.setIndex(index);
				clientProjectiles.put(index, projectile);
				clientProjectilesReference.add(projectile);
			}
		}
		
	}

}
