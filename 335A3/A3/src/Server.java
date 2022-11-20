import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * @author Ryan Pecha
 */





// Server to host game and multiple clients
public class Server {
	
	
	
	
	
	// class variables
	//private static final Integer MAX_SERVER_SOCKET_ADDRESS = 99999;
	private static final int MAX_CLIENT_COUNT = 20;
	private static final int SERVER_SOCKET_LISTENER_TIMEOUT = 1000;
	private ProgramState state;
	private ArrayList<ServerClientManager> serverClientManagers;
	private Integer serverClientIndex;
	
	private final Lock lock;
	private final Condition lockCondition;
	
	
	
	
	
	// Server main
	public Server(int portAddress) throws IOException {
		
		
		
		
		
		// initializing variables
		state = new ProgramState();
		serverClientManagers =  new ArrayList<ServerClientManager>();
		this.lock = new ReentrantLock();
		this.lockCondition = lock.newCondition();
		serverClientIndex = 0;

    	
    	
    	
		
		// grabbing the local host address
		InetAddress serverAddress = InetAddress.getLocalHost();
		System.out.println("SERVER > STARTED > " + serverAddress);
		
		
		
		
		
		// building a pool of threads to run each client manager
        var threadPool = Executors.newFixedThreadPool(MAX_CLIENT_COUNT);
        System.out.println("SERVER > THREAD POOL > STARTED WITH SIZE > " + MAX_CLIENT_COUNT);
		
		
        
        
		
		// opening the server socket
        //Integer socketAddress = new Random().nextInt(MAX_SERVER_SOCKET_ADDRESS + 1);
		int socketAddress = portAddress;
        try (var listener = new ServerSocket(socketAddress)) 
        {
        	listener.setSoTimeout(SERVER_SOCKET_LISTENER_TIMEOUT);
            System.out.println("SERVER > SERVER SOCKET > STARTED > " + socketAddress);
            
            
            
            
            
            // server run loop
            while (true) {
            	
            	
            	
            	
            	
            	// server mainMenu run state
            	if (state.getState() == ProgramState.STATE.MainMenu) {
            		System.out.println("SERVER > STARTING MAIN MENU CONNECTION LOOP");
            		
            		// *** setup main menu state ***
            		
            		
            		
            		// server mainMenu run loop
            		while (state.getState() == ProgramState.STATE.MainMenu) 
            		{	
            			
            			
            			
            			// using a try/catch with listener timeOutException to cycle and check the serverState
            			try {
            				
            				// listening for client and accepting upon connection
            				Socket clientSocket = listener.accept();
            				System.out.println("SERVER > SERVER SOCKET > ACCEPTED CLIENT SOCKET > " + clientSocket);
            				
            				// creating a new manager for the connected client
            				ServerClientManager clientManager = new ServerClientManager(clientSocket, this, serverClientIndex);
            				serverClientManagers.add(clientManager);
            				
            				// running the client manager with thread from the thread pool
            				threadPool.execute(clientManager);
            				System.out.println("SERVER > THREAD POOL > ADDED CLIENT MANAGER > " + clientManager);
            				serverClientIndex += 1;
            				
            			}
            			
            			
            			
            			// listener has timed out
            			catch (SocketTimeoutException e) {
            				
            				// removing disconnected clients
            				for (int i = 0; i < serverClientManagers.size(); i++) {
            					ServerClientManager clientManager = serverClientManagers.get(i);
            					if (clientManager.state.getState() == ProgramState.STATE.Terminated) {
            						serverClientManagers.remove(clientManager);
            						clientManager.terminate();
            						System.out.println("SERVER > REMOVED DISCONNECTED CLIENT AND MANAGER > " + clientManager);
            					}
            				}
            				
            				// if there are no clients connected then we go back to listening
            				if (serverClientManagers.size() == 0) { continue; }
		
            				// checking if all clients are ready to play
            				boolean allClientsReady = true;
            				for (ServerClientManager clientManager : serverClientManagers) {            					
            					if (clientManager.state.getState() != ProgramState.STATE.ReadyToPlay) {
            						allClientsReady = false;
            					}
            				}
            				
            				// if all clients are ready to play then we break out of the listener loop
            				if (allClientsReady) {            					
            					System.out.println("SERVER > ALL CLIENTS READY TO PLAY > STARTING GAME");
            					this.state.setState(ProgramState.STATE.InGame);
            				}
        					            				
            			}
            			
            			
            			
            			// END OF server mainMenu run loop
            		}
        			System.out.println("SERVER > EXITED MAIN MENU CONNECTION LOOP");

            		
            		
            		// *** cleanup mainMenu state ***
            		
            		
            		
            		// END OF server mainMenu run state
            	}
            	
            	
            	
            	
            	
            	// server inGame run state
            	if (this.state.getState() == ProgramState.STATE.InGame) {
            		System.out.println("SERVER > STARTING InGame SETUP");
            		
            		// *** setup inGame state ***
            		            		
            		
            		
            		// calculating the most heavily voted map and gameMode
            		HashMap<String, Integer> mapVotes = new HashMap<String, Integer>();
            		HashMap<String, Integer> gameModeVotes = new HashMap<String, Integer>();
            		for (ServerClientManager clientManager : serverClientManagers) {
            			
            			// map
            			String cmMapName = clientManager.mapName;
            			if (mapVotes.containsKey(cmMapName)) {
            				Integer curVotes = mapVotes.get(cmMapName) + 1;
            				mapVotes.put(cmMapName, curVotes);
            			}
            			else {            				
            				mapVotes.put(cmMapName, 1);
            			}
            			
            			// gameMode
            			String cmGameMode = clientManager.gameMode;
            			if (gameModeVotes.containsKey(cmGameMode)) {
            				Integer curVotes = gameModeVotes.get(cmGameMode) + 1;
            				gameModeVotes.put(cmGameMode, curVotes);
            			}
            			else {            				
            				gameModeVotes.put(cmGameMode, 1);
            			}

            		}
            		
            		// most voted map
            		Integer mostVotes = 0;
            		String mapName = "";
            		for (String curMapName : mapVotes.keySet()) {
            			Integer votes = mapVotes.get(curMapName);
            			if (votes > mostVotes) {
            				mostVotes = votes;
            				mapName = curMapName;
            			}
            		}
            		System.out.println("SERVER > DECIDED MAP > " + mapName);
            		mapVotes.clear();
            		
            		// most voted gameMode
            		mostVotes = 0;
            		String gameMode = "";
            		for (String curGameMode : gameModeVotes.keySet()) {
            			Integer votes = gameModeVotes.get(curGameMode);
            			if (votes > mostVotes) {
            				mostVotes = votes;
            				gameMode = curGameMode;
            			}
            		}
            		System.out.println("SERVER > DECIDED GAMEMODE > " + gameMode);
            		gameModeVotes.clear();
            		
            		// we dont exit if there is only one player to start
            		boolean onePlayer = serverClientManagers.size() == 1;
            		
            		// building all tanks on server
            		int index = 0;
            		ArrayList<ServerTank> tanks = new ArrayList<ServerTank>();
            		for (ServerClientManager clientManager : serverClientManagers) {
		    			
            			ServerTank tank = null;
            			
		    			String tankType = clientManager.tankType;
		    			
		    			int r = Integer.valueOf(clientManager.r);
		    			int g = Integer.valueOf(clientManager.g);
		    			int b = Integer.valueOf(clientManager.b);
		    			Color color = new Color(r,g,b);
		    			
		    			MapGrid mapGrid = new MapGrid(mapName);
		    			
		    			Vector2 spawnLocation = mapGrid.getSpawnLocation(index);
		    			
		    			if (tankType.equals("Heavy")) {
		    				tank = new HeavyTankServer(index, color, mapGrid, tanks, spawnLocation);
		    			}
		    			
		    			if (tankType.equals("Standard")) {
		    				tank = new StandardTankServer(index, color, mapGrid, tanks, spawnLocation);
		    			}
		    			
		    			if (tankType.equals("Light")) {
		    				tank = new LightTankServer(index, color, mapGrid, tanks, spawnLocation);
		    			}
		    			
		    			
		    			// tank influenced by gameMode type
		    			if (gameMode.equals("OneHealth")) {
		    				tank.setHealth(1);
		    			}
		    			
		    			if (gameMode.equals("DoubleHealth")) {
		    				tank.setHealth(tank.getHealth() * 2);
		    			}
		    			
		    			
		    			// 
		    			tanks.add(tank);
		    			clientManager.tank = tank;
		    			
		    			index += 1;
		    		}
            		

            		
            		// sending the startGame command to all clients along with the initialGameData
            		for (ServerClientManager clientManager : serverClientManagers) {
            			StreamCommand_InitialGameData scIGD = new StreamCommand_InitialGameData(mapName, tanks);
            			scIGD.setClientScoresByID(this.getServerClientManagerScoresByID());
            			clientManager.requestClientStartGame(scIGD);
            		}
            		
            		
            		
            		// waiting for all ServerClientManagers to match the inGame state
            		lock.lock();
        		    try {
        		    	while (true) {
        		    		boolean allClientsInGame = true;
        		    		for (ServerClientManager clientManager : serverClientManagers) {
        		    			if (clientManager.state.getState() != ProgramState.STATE.InGame){
        		    				allClientsInGame = false;
        		    			}
        		    		}
        		    		if (allClientsInGame) {
        		    			System.out.println("SERVER > ALL ServerClientManagers MATCH InGame STATE");
        		    			break;
        		    		}
        		    		System.out.println("SERVER > WAITING FOR ADDITIONAL ServerClientManagers TO UNBLOCK");
        		    		lockCondition.await();
        		    	}
        			}
        		    catch (InterruptedException e) {
        				e.printStackTrace();
        				lock.unlock();
        			}
            		

            		
            		// server game run loop
        		    if (this.state.getState() == ProgramState.STATE.InGame) {        		    	
        		    	System.out.println("SERVER > STARTING InGame LOOP");
        		    	
        		    	// each projectile has a unique index
        		    	Integer currentProjectileIndex = 0;
        		    	
        		    	// storing all server projectiles
        		    	ArrayList<ServerProjectile> projectiles = new ArrayList<ServerProjectile>();
        		    	
        		    	// setting up command for GameData
        		    	StreamCommand_GameData scGD = new StreamCommand_GameData();
        		    	scGD.setServerTanks(tanks);
        		    	scGD.setServerProjectiles(projectiles);
        		    	
        		    	// storing timeData for frame compensation
        		    	long curTime = System.nanoTime();
        		    	float timeDelta = 0;
        		    	
        		    	// starting game loop
        		    	while(this.state.getState() == ProgramState.STATE.InGame) {
        		    		        		    		
        		    		// calculating deltaTime
        		    		long newTime = System.nanoTime();
        		    		timeDelta = newTime - curTime;
        		    		timeDelta /= 1_000_000_000;
        		    		curTime = newTime;
        		    		
        		    		// updating tank positions and building new projectiles
        		    		for (ServerClientManager clientManager : serverClientManagers) {
        		    			ServerProjectile projectile = clientManager.tank.handleInput(clientManager.activeKeyCodes, timeDelta);
        		    			
        		    			if (projectile != null) {
        		    				projectile.setIndex(currentProjectileIndex);
        		    				projectiles.add(projectile);
        		    				currentProjectileIndex += 1;
        		    			}
        		    			
        		    		}
        		    		        		    		
        		    		// updating projectile positions
        		    		for (ServerProjectile projectile : projectiles) {
    		    				projectile.move(timeDelta);
        		    		}
        		    		  		    		        	
        		    		// sending gameData to clients via managers
        		    		for (ServerClientManager clientManager : serverClientManagers) {
        		    			clientManager.pushClientGameData(scGD);
        		    		}
        		    		
        		    		// checking for end of game
        		    		int tanksLeft = 0;
        		    		for (ServerClientManager clientManager : serverClientManagers) {
        		    			if (!clientManager.tank.isDestroyed()) {
        		    				tanksLeft += 1;
        		    			}
        		    		}
        		    		// note that we don't exit if we started with only one player
        		    		if (tanksLeft <= 1 && !onePlayer) {
        		    			this.state.setState(ProgramState.STATE.MainMenu);
        		    		}
        		    		
        		    		// removing destroyed projectiles
        		    		projectiles.removeIf(projectile -> projectile.isDestroyed());
        		    		
        		    		// END OF server game run loop
        		    	}
        		    }
            		
            		
            		
            		// *** cleanup inGame state ***
            		System.out.println("SERVER > CLEANING UP GAME PLAY LOOP");
            		
            		
            		
            		// send the gameEnd command to all clients via ServerClientManagers
            		for (ServerClientManager clientManager : serverClientManagers) {
            			if (!clientManager.tank.isDestroyed()) {
            				clientManager.score += 1;
            			}
            			clientManager.requestClientEndGame();
            		}
            		
            		
            		
            		// waiting for all ServerClientManagers to match the MainMenu state
            		lock.lock();
        		    try {
        		    	while (true) {
        		    		boolean allClientsMainMenu = true;
        		    		for (ServerClientManager clientManager : serverClientManagers) {
        		    			if (clientManager.state.getState() != ProgramState.STATE.MainMenu){
        		    				allClientsMainMenu = false;
        		    			}
        		    		}
        		    		if (allClientsMainMenu) {
        		    			System.out.println("SERVER > ALL ServerClientManagers MATCH MainMenu STATE");
        		    			break;
        		    		}
        		    		System.out.println("SERVER > WAITING FOR ADDITIONAL ServerClientManagers TO UNBLOCK");
        		    		lockCondition.await();
        		    	}
        			}
        		    catch (InterruptedException e) {
        				e.printStackTrace();
        				lock.unlock();
        			}
            		
            		
        		    
            		// END OF server inGame run state
            	}
            	
            	
            	
            	
            	
            	// *** cleanup server run loop ***
            	
            	
            	
            	
            	
            	// END OF server run loop
            }
            
            // END OF server socket connection
        }
        
        
        
        
        
        // server was killed
        catch (Exception e){
        	e.printStackTrace();
        	System.out.println("SERVER > SERVER SOCKET > KILLED > " + socketAddress);
        	System.out.println("SERVER > KILLED > " + serverAddress);
        }
	
        
        
        
        
    	threadPool.shutdownNow();
    	for (ServerClientManager clientManager : serverClientManagers) {
    		clientManager.terminate();
    	}
    	// this terminal will persist until all children threads have been terminated
    	System.out.println("SERVER > THREAD POOL > KILLED");
        System.out.println("SERVER > EXITED");
        
        
        
        
        
        // END OF Server/main
	}
	
	
	
	
	
	public void unblock() {
		System.out.println("SERVER > UNBLOCKING");
		lock.lock();
        try {
        	lockCondition.signalAll();
        } 
        finally {
            lock.unlock();
        }
	}
	
	
	
	
	
	public HashMap<Integer,Integer> getServerClientManagerScoresByID(){
		HashMap<Integer,Integer> idScoreMap = new HashMap<Integer,Integer>();
		for (ServerClientManager serverClientManager : serverClientManagers) {
			idScoreMap.put(serverClientManager.serverClientID, serverClientManager.score);
		}
		return idScoreMap;
	}
	
    
	
	
	
    // END OF Server
}






