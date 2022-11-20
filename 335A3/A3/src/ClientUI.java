import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Represents the Game window used by each client to see the changes made from the server. Displays the map and all tanks,
 * as well as any projectiles that are created. Also contains a scoreboard on the side showing how many wins each player has
 * per server session.
 * @author Kyle Walker
 *
 */
public class ClientUI extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int GAME_RES_X = 1200;
	static final int GAME_RES_Y = 960;
	static final double GAME_FPS = 30.0f;
	static final Dimension GAME_RES = new Dimension(GAME_RES_X, GAME_RES_Y);
	private JFrame frame;
	
	Client client;
	private PlayerInput inputManager;
	MapGrid lvlGrid;
	private ArrayList<ClientTank> visibleTanks;
	private ArrayList<ClientProjectile> visibleProjectiles;
	private Set<Integer> tankIds = new HashSet<Integer>();
	
	/**
	 * Constructs new client UI window and stores client Tank and projectile information from server.
	 * @param client	Client corresponding to this UI
	 * @param mapName	the name of the map to be drawn
	 * @param clientTanks	arrayList of all client Tanks to draw
	 * @param clientProjectiles	arrayList of all projectiles to draw
	 */
	ClientUI(Client client, String mapName, ArrayList<ClientTank> clientTanks, ArrayList<ClientProjectile> clientProjectiles) {
		this.client = client;
		lvlGrid = new MapGrid(mapName);
		visibleTanks = clientTanks;
		visibleProjectiles = clientProjectiles;
		
		// Get the playerIDs from client
		tankIds = client.getPlayerIDs();

		frame = new JFrame("XTANK");

		this.setPreferredSize(GAME_RES);
		this.setBackground(Color.black);

		this.setFocusable(true);
		inputManager = new PlayerInput(client);
		this.addKeyListener(inputManager);
		
		// Open frame
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.black);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	
	/**
	 * Repaints the Panel when called, used in UI run loop
	 */
	public void update() {
		repaint();
	}

	/**
	 * Stores the visuals of the client panel as an image and renders when called, then draws all components.
	 */
	public void paint(Graphics g) {
		Image image = createImage(GAME_RES.width, GAME_RES.height);
		Graphics graphics = image.getGraphics();
		draw(graphics);
		g.setColor(Color.white);
		g.drawImage(image, 0, 0, this);
	}
	
	/**
	 * Draws all components currently present in the scene, and draws a scoreboard with player info.
	 * @param g graphics component
	 */
	public void draw(Graphics g) {
		// Draw map tiles
		if (lvlGrid != null) {
			lvlGrid.draw(g);
		}
		
		// Draw all tanks
		for (int i = 0; i< visibleTanks.size(); i++) {
			ClientTank tank = visibleTanks.get(i);
			tank.draw(g);
		}
		
		// Draw all projectiles
		ListIterator<ClientProjectile> projIterator = visibleProjectiles.listIterator();
		while (projIterator.hasNext()) {
			projIterator.next().draw(g);
		}
		
		// Draw Scoreboard
		for (Integer i: tankIds) {
			int score = client.getPlayerScore(i);
			g.drawString("Player " + i + " Score: " + score, GAME_RES_X-200, 30*i + 30);
		}
	}
	
	/**
	 * Closes client frame and returns to menu.
	 */
	public void close() {
		frame.setVisible(false);
	}
	
	/**
	 * Creates an update loop with a fixed framerate to call the update method once per frame.
	 */
	@Override
	public void run() {
		long prevTime = System.nanoTime();
		double tickRate = GAME_FPS;
		double ns = 1000000000 / tickRate;
		double delta = 0;
		while (true) {
			long curTime = System.nanoTime();
			delta += (curTime - prevTime) / ns;
			prevTime = curTime;
			if (delta >= 1) {
				update();
				delta--;
			}
			//deltaTime = delta;
		}
	}
}
