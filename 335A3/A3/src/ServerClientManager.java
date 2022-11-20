import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

/*
 * @author Ryan Pecha
 */




// class used by the server to manage an individual client
public class ServerClientManager implements Runnable {

	
	
	
	
	// class variables
	
	// program data
	private Socket clientSocket;
	private Server server;
	protected DataInputStream in;
	protected DataOutputStream out;
	public ProgramState state;
	
	// user data
	protected Integer score;
	protected Integer serverClientID;
	protected String mapName;
	protected String gameMode;
	protected String tankType;
	protected String r;
	protected String g;
	protected String b;
	
	// game data
	protected ServerTank tank;
	public HashSet<Integer> activeKeyCodes;
	
	
	
	
	
    // constructor
	ServerClientManager(Socket socket, Server server, Integer id) {
    	this.clientSocket = socket;
    	this.state = new ProgramState();
    	this.server = server;
    	this.activeKeyCodes = new HashSet<Integer>();
    	this.serverClientID = id;
    	this.score = 0;
    }
    
    
    
	
	
	// running this ClientManager
    @Override
    public void run() {
    	System.out.println("SERVER CLIENT MANAGER > RUNNING > " + clientSocket);
    	
    	

    	
    	
    	// encapsulating errors of client & server connection
        try {
        	
        	
        	
        	
        	
        	// trying to setup data streams
        	
        	// input
        	in = new DataInputStream(clientSocket.getInputStream());
        	System.out.println("SERVER CLIENT MANAGER > RECOGNIZED IN STREAM");
        	
        	// output
        	out = new DataOutputStream(clientSocket.getOutputStream());
        	System.out.println("SERVER CLIENT MANAGER > RECOGNIZED OUT STREAM");

        	
        	
        	// client run loop
        	while (true) {
        		
        		
        		
        		// mainMenu state
        		if (this.state.getState() == ProgramState.STATE.MainMenu) {    			
        			
        			// *** setup mainMenu state ***
        			
        			
        			
        			// mainMenu loop
            		while (this.state.getState() == ProgramState.STATE.MainMenu) {
            			
            			String input = in.readUTF();
            			
            			if (input.equals(ProgramState.STATE.ReadyToPlay.toString())) {
            				
            				this.state.setState(ProgramState.STATE.ReadyToPlay);
            				
            				StreamCommand_PlayerData scPD = new StreamCommand_PlayerData();
            				scPD.readData(in);
            				
            				this.mapName = scPD.mapName;
            				this.gameMode = scPD.gameMode;
            				this.tankType = scPD.tankType;
            				this.r = scPD.r;
            				this.g = scPD.g;
            				this.b = scPD.b;
            				
            				System.out.println(this.mapName);
            				System.out.println(this.tankType);
            				System.out.println(this.gameMode);
            				System.out.println(this.r);
            				System.out.println(this.g);
            				System.out.println(this.b);
            				
            			}
            			            			
            		}
            		
            		
            		
            		// *** cleanup mainMenu state ***
        			
            		
            		
            		// END OF mainMenu state
        		}
        		
        		
        		
        		
        		
        		// readyToPlay state
        		if (this.state.getState() == ProgramState.STATE.ReadyToPlay) {
        			System.out.println("SERVER CLIENT MANAGER > STARTING ReadyToPlay LOOP");
        			
        			// *** setup readyToPlay state ***

        			
        			
        			
        			
        			// readyToPlay loop
        			while (this.state.getState() == ProgramState.STATE.ReadyToPlay) {
            			
        				// waiting for Client to send state update
        				String input = in.readUTF();
        				
        				// checking if state update is MainMenu state
        				if (input.equals(ProgramState.STATE.MainMenu.toString())) {
        					this.state.setState(ProgramState.STATE.MainMenu);
        					this.out.writeUTF("unblock");
        					out.flush();
        				}
        				
        				// checking if state update is InGame state
        				if (input.equals(ProgramState.STATE.InGame.toString())) {
        					this.state.setState(ProgramState.STATE.InGame);
        					server.unblock();
        				}
        				        				        				        				    
        				// END OF readyToPlay loop
            		}
        			
        			
        			
        			// *** cleanup readyToPlay state ***
        			System.out.println("SERVER CLIENT MANAGER > CLEANING AFTER ReadyToPlay LOOP");
        			
        			
        			
        			// END OF readyToPlay state
        		}
        		
        		
        		
        		
        		
        		// inGame state
        		if (this.state.getState() == ProgramState.STATE.InGame) {
        			System.out.println("SERVER CLIENT MANAGER > STARTING InGame LOOP");
        			
        			// *** setup inGame state ***
        			
        			
        			
        			// inGame run loop
        			while (this.state.getState() == ProgramState.STATE.InGame) {
        				
        				// read the user input data from the inStream
        				String input = in.readUTF();
        				StreamCommand_UserInput scUI = new StreamCommand_UserInput();
        				
        				// checking if the input is userInput data
        				if (input.equals("UserInput")) {
        					scUI.readData(in);        		
        					this.activeKeyCodes = scUI.getActiveKeyCodes();
        				}
        				
        				// check if the input is a Client state update to the MainMenu state
        				if (input.equals(ProgramState.STATE.MainMenu.toString())) {
        					this.state.setState(ProgramState.STATE.MainMenu);
        					this.server.unblock();
        				}
        				
        				
        				
        				// END OF inGame run loop
        			}
        			
        			
        			
        			// *** cleanup inGame state ***
        			System.out.println("SERVER CLIENT MANAGER > CLEANING AFTER ReadyToPlay LOOP");
        			
        			// clearing the current userInput
        			this.activeKeyCodes.clear();
        			
        			
        			
        			// END OF inGame state
        		}

        		
        		
        		
        		
        		// exiting ServerClient run loop
        		if (this.state.getState() == ProgramState.STATE.Terminated) {
        			System.out.println("SERVER CLIENT MANAGER > TERMINATED");
        			break;
        		}
        		
        		
        		
        		
        		
        		// *** cleanup server client run loop ***
        		
        		
        		
        		
        		
        		// END OF server client run loop
        	}
        	
        	
        	
        	
        	
        	// END OF server client connection
        }

        
        
        
        
        // the client has disconnected
        catch (Exception e) {
        	// client disconnected with an error
            System.out.println("SERVER CLIENT MANAGER > DISCONNECT ERROR > " + clientSocket);
        } 
        
        finally {
        	// client disconnected successfully
            try { clientSocket.close(); } 
            catch (IOException e) {}
            System.out.println("SERVER CLIENT MANAGER > CLOSED CONNECTION > " + clientSocket);
        }
        
        this.terminate();
        
        
        
        
        
        // END OF ClientManager/run
    }
    
    
    
    
    
    // methods starting with "request" will result in a callback via the inStream
    
    // makes a request to the client to enter InGame state
    public void requestClientStartGame(StreamCommand_InitialGameData streamCommand_InitialGameData) throws IOException {
    	this.out.writeUTF("StartGame");
    	streamCommand_InitialGameData.writeData(out);
    }
    
    // makes a request to the client to enter MainMenu state
    public void requestClientEndGame() throws IOException {
    	this.out.writeUTF("EndGame");
    	out.flush();
    }
    
    // sends the current GameData to the client
    public void pushClientGameData(StreamCommand_GameData streamCommand_GameData) throws IOException {
    	streamCommand_GameData.writeData(out);
    }
    
    
    
    
    
    /**
     * kills this runnable via:
     * closing data streams,
     * closing the clientSocket,
     * and setting state to terminated
     */
    public void terminate() {
    	System.out.println("SERVER CLIENT MANAGER > TERMINATING");
    	
    	this.state.setState(ProgramState.STATE.Terminated);
    	try {
			this.out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			this.in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    
    // END OF ServerClientManager
}








