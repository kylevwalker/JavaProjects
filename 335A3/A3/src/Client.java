import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * @author Ryan Pecha
 */





// This class represents a single client instance
public class Client {
	
	
	
	
	
	// this client
	private static final int MAX_CONNECTION_ATTEMPTS = 5;
	private ProgramState state;
	private MainMenu mainMenu;
	private StreamCommand_InitialGameData scIGD;
	private HashMap<Integer, Integer> idScoreMap;
	
	// client-server communication
	private DataInputStream in;
	private DataOutputStream out;
	
	// server attributes
	private String hostAddress;
	private int socketAddress;
	private Socket curSocket;
	private boolean isConnected;
	private boolean restartConnect;
	private boolean serverAttributesSet;

	// thread locking
	private final Lock lock;
	private final Condition lockCondition;
	
	// defaults
	// 127.0.0.1
	// 59896
	
	
	
	// Client/main
	public Client() {
		System.out.println("CLIENT > STARTED");
    	
		
		
		
		
		// initialization
		this.state = new ProgramState();
		this.mainMenu = new MainMenu(this);
		this.isConnected = false;
		this.restartConnect = false;
		
		this.lock = new ReentrantLock();
		this.lockCondition = lock.newCondition();
		
		
	    
		
		
		//  client run loop
		while (true) {
			
			
			
			
			
			// waiting for ClientUI to call connectToServer
			serverAttributesSet = false;
		    lock.lock();
		    try {
		    	System.out.println("CLIENT > WAITING FOR SERVER DATA > SERVER ADDRESS <STR>");
		    	System.out.println("CLIENT > WAITING FOR SERVER DATA > SOCKET ADDRESS <INT>");
		    	while (!serverAttributesSet & !restartConnect) {		    		
		    		lockCondition.await();
		    		restartConnect = false;
		    	}
			} 
		    catch (InterruptedException e) {
				e.printStackTrace();
				lock.unlock();
			}
			
		    
		    
		    
			
			// mainMenu state - server connection attempt loop
			int currentConnectionAttempts = 0;
			boolean successfulConnection = false;
			while ( (currentConnectionAttempts < MAX_CONNECTION_ATTEMPTS) && (!successfulConnection) ) {
				
				
				
				
				
				// opening a socket at the local host
				try (var clientSocket = new Socket(hostAddress,socketAddress)) {
					curSocket = clientSocket;
					successfulConnection = true;
					isConnected = true;
					
					// status update
					System.out.println("CLIENT > HOST > CONNECTED > " + hostAddress);
					System.out.println("CLIENT > SOCKET > CONNECTED > " + socketAddress);
					
					// input
					in = new DataInputStream(clientSocket.getInputStream());
					System.out.println("CLIENT > RECOGNIZED IN STREAM");
					
					// output
			    	out = new DataOutputStream(clientSocket.getOutputStream());
			    	System.out.println("CLIENT > RECOGNIZED OUT STREAM");
			    	
			    	
			    	
			    	
			    	
			    	// valid connection loop
			    	System.out.println("CLIENT > STARTING VALID CONNECTION LOOP");
			    	while (true) {

			    		
			    		
			    		
			    		
			    		// wait for ClientUI to call readyUp
		    			while (this.state.getState() == ProgramState.STATE.MainMenu && this.isConnected) {
		    				
		    				// waiting for readyToPlay to be set and this thread unlocked
		    			    lock.lock();
		    			    try {
		    			    	System.out.println("CLIENT > WAITING FOR CLIENT TO READY UP");
	    			    		lockCondition.await();
		    				}
		    			    catch (InterruptedException e) {
		    					e.printStackTrace();
		    					lock.unlock();
		    				}
		    				
		    				// client-server disconnects will happen via the ClientUI,
		    				// and will automatically break out of this via the try-clientSocket
		    				
		    			}
			    		
			    		
			    		
			    		
			    		
			    		// wait for the server to send the StartGame command
			    		if (this.state.getState() == ProgramState.STATE.ReadyToPlay) {
			    			System.out.println("CLIENT > STARTING READY TO PLAY LOOP");
			    			
			    			// waiting for StartGame from server
			    			while (this.state.getState() == ProgramState.STATE.ReadyToPlay) {
			    				
			    				String input = in.readUTF();
			    				
			    				if (input.equals("StartGame")) {
			    					
			    					// reading initial game data from server
			    					scIGD = new StreamCommand_InitialGameData();
			    					scIGD.readData(in);
			    					idScoreMap = scIGD.idScoreMap;
			    					System.out.println("CLIENT > MAP WAS DECIDED: " + scIGD.mapName);
			    					
			    					// entering InGame state
			    					this.setStateInGame();
			    				}
			    							    				
			    				// client-server disconnects will happen via the ClientUI ,
			    				// and will automatically break out of this via the try-clientSocket
			    				
			    			}
			    		}
			    		
			    		
			    		
			    		
			    		
			    		// client game run loop
			    		if (this.state.getState() == ProgramState.STATE.InGame) {
			    			System.out.println("CLIENT > STARTING InGame LOOP");
			    			
			    			// All projectiles to render
			    			ArrayList<ClientProjectile> projectiles = new ArrayList<ClientProjectile>();

			    			// creating a new ClientUI and pass in the initialGameData
			    			ClientUI clientUI = new ClientUI(this, scIGD.mapName, scIGD.getClientTanks(), projectiles);
			    			
			    			// Command to read in gameData from server
			    			StreamCommand_GameData scGD = new StreamCommand_GameData();
			    			scGD.setClientTanks(scIGD.getClientTanks());			    			
			    			scGD.setClientProjectiles(projectiles);
			    			
			    			// server command tags for InGame mode
			    			String gameDataTag = "GameData";
			    			String endGameTag = "EndGame";
			    			
			    			// starting the gameUI using threaded ClientUI
			    			Thread clientUIRunThread = new Thread(clientUI);
			    			clientUIRunThread.start();			    			
			    			
			    			// Starting the InGame loop
			    			while (this.state.getState() == ProgramState.STATE.InGame) {
			    				
			    				// wait for data to reach the inStream
			    				String input = in.readUTF();
			    				
			    				// checking if the data is gameData
			    				if (gameDataTag.equals(input)) {
			    					scGD.readData(in);
			    					//clientUI.update();
			    				}
			    				
			    				// if the data is gameEnd command then we set out state to MainMenu and inform the server
			    				else if (endGameTag.equals(input)) {
			    					clientUI.close();
			    					this.setStateMainMenu();
			    				}
			    							    				
			    				// client-server disconnects will happen via the ClientUI 
			    				// and will automatically break out of this via the try-clientSocket
			    				
			    			}
			    			
			    		}
			    		
			    		
			    		
			    		
			    		
			    		// exiting if disconnected from the server
			    		if (!this.isConnected) {
			    			break;
			    		}
			    		
			    		
			    		
			    		
			    		
			    		// END OF valid connection loop
			    	}
			    	
			    	
			    	
			    	// END OF server connection
				}
			    
				
				
				
				
		    	// client has disconnected from the server or could not connect to the server socket
				catch (Exception e){
					this.isConnected = false;
					this.restartConnect = false;
					
					// incrementing connection attempt count
					currentConnectionAttempts += 1;
					System.out.println("CLIENT > CLIENT TO SERVER SOCKET CONNECTION FAILED > ATTEMPT # " + currentConnectionAttempts);
					
					// printing error after error of successful connection
					if (successfulConnection) {
						System.out.println("CLIENT > CLIENT TO SERVER SOCKET CONNECTION FAIL TYPE > SUCCESSFUL CONNECTION BROKEN");
						e.printStackTrace();
					}
					
					// printing error after reaching the max connection attempt count
					if (currentConnectionAttempts == MAX_CONNECTION_ATTEMPTS) {
						System.out.println("CLIENT > CLIENT TO SERVER SOCKET CONNECTION FAIL TYPE  > HIT MAX CONNECTION ATTEMPT COUNT > " + MAX_CONNECTION_ATTEMPTS);
						e.printStackTrace();
					}
					
				}

				
				
				
				
				// END OF connection attempt loop
			}
			
			
			
			
			
			System.out.println("CLIENT > RESTARTING CLIENT RUN LOOP");
			// END OF Client run loop
		}
		
		
		
		
				
		// END OF Client/main
	}
	
	
	
	
	
	
	
	// internal methods for setting client programState
	// and notifying the server along with important data
	
	// state MainMenu
	private void setStateMainMenu() throws IOException {
		System.out.println("CLIENT > SET STATE > MainMenu");
		this.setState(ProgramState.STATE.MainMenu);
		mainMenu.openMenu();
	}
	
	// state ReadyToPlay
	private void setStateReadyToPlay() throws IOException {
		System.out.println("CLIENT > SET STATE > ReadyToPlay");
		this.setState(ProgramState.STATE.ReadyToPlay);
	}
	
	// state InGame
	private void setStateInGame() throws IOException {
		System.out.println("CLIENT > SET STATE > InGame");
		this.setState(ProgramState.STATE.InGame);
		mainMenu.closeMenu();
	}
	
	// set any State and notify server
	private void setState(ProgramState.STATE newState) throws IOException {
		this.state.setState(newState);
		this.out.writeUTF(newState.toString());
	}
	
	
	
	
	
	// public methods
	
	// connected or disconnect and reconnect if already connected
	public void connectToServer(String serverAddress, String portAddress) {
		System.out.println("CLIENT > CONNECTING TO > " + serverAddress + " AT PORT > " + portAddress);
		
		// if not in menu then cannot connect to new server
		if (this.state.getState() != ProgramState.STATE.MainMenu) {
			System.out.println("CLIENT > CANNOT CONNECT TO NEW SERVER > ALREADY IN READY TO PLAY STATE");
			return;
		}
		
		// if already connected then disconnect and reconnect
		if (this.isConnected) {
			this.disconnectFromServer();
			this.restartConnect = true; 
		}
		
		// try to convert MainMenu server data to actual server location info
		try {			
			this.hostAddress = serverAddress;
			this.socketAddress = Integer.valueOf(portAddress);
			serverAttributesSet = true;
			System.out.println("CLIENT > PULLED SERVER ADDRESS <STR>");
	    	System.out.println("CLIENT > PULLED SOCKET ADDRESS <INT>");
		}
		
		// failure to convert server data
		catch (Exception e) {
			System.out.println("CLIENT > INCORRECT SERVER ADDRESS DATA TYPE ( EXPECTED <STR> ) > RETURNING TO DATA LISTEN");
			System.out.println("CLIENT > INCORRECT SERVER SOCKET DATA TYPE ( EXPECTED <INT> ) > RETURNING TO DATA LISTEN");
			return;
		}
		
		// unblocking this client
		this.unblock();
        
	}
	
	
	
	
	
	// disconnect from the server if already connected
	private void disconnectFromServer() {
		if (this.isConnected) {		
			System.out.println("CLIENT > DISCONNECTING FROM SERVER");
			try {
				this.curSocket.close();
				this.in.close();
				this.out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.isConnected = false;
	}
	
	
	
	
	
	// ReadyUp and notify the server
	public void readyUp(String tankType, String r, String g, String b, String gameMode, String mapFile) {
		System.out.println("CLIENT > TRYING TO READY UP");
		
		// cannot ready up if not connected to server
		if (!this.isConnected) {
			System.out.println("CLIENT > FAILED TO READY UP > NOT CONNECTED TO SERVER");
			return;
		}
		
		// trying to set readyToPlay state
		try {
			this.setStateReadyToPlay();
			System.out.println("CLIENT > READIED UP");
		} 
		// failure to set
		catch (IOException e) {
			System.out.println("CLIENT > FAILED TO READY UP");
			e.printStackTrace();
			return;
		}
		
		// unblockiung to allow for progression to next state
		this.unblock();
		
		// sending our playerData to the server
		StreamCommand_PlayerData scPD = new StreamCommand_PlayerData(mapFile, gameMode, tankType, r, g, b);
		try {
			scPD.writeData(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	
	
	
	
	// undoing our readyToPlay state and notifying the server
	public void undoReadyUp() {
		System.out.println("CLIENT > TRYING TO UNREADY");
		
		try {
			this.setStateMainMenu();
			System.out.println("CLIENT > UNREADIED");
		} 
		
		// failure to readyUp
		catch (IOException e) {
			System.out.println("CLIENT > FAILED TO UNREADY");
			e.printStackTrace();
			return;
		}
		
	}
	
	
	
	
	
	// Unblocking this threaded client to allow for state progression
	private void unblock() {
		System.out.println("CLIENT > UNBLOCKING");
		lock.lock();
        try {
        	lockCondition.signalAll();
        } 
        finally {
            lock.unlock();
        }
	}
	
	
	
	
	
	// 
	public void pushUserInput(HashSet<Integer> keySet) throws IOException {
		StreamCommand_UserInput scUI = new StreamCommand_UserInput(keySet);
		scUI.writeData(out);
	}
	
	
	
	
	
	public Set<Integer> getPlayerIDs(){
		return this.idScoreMap.keySet();
	}
	public Integer getPlayerScore(Integer tankId){
		// put in ID, get out score
		return idScoreMap.get(tankId);
	}
	
	
	
	
	
	// END OF Client
}




