import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Client Main Menu UI used for setting up player and match settings and connecting to servers. Allows each client
 * to choose tank type and color, which map and gamemode to vote for, and what server and port address to connect to.
 * When multiple clients are connected and press the ready button, the game will try to begin. Also keeps track of
 * @author Kyle Walker
 *
 */
public class MainMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private Dimension menuSize = new Dimension(ClientUI.GAME_RES.width - 400, ClientUI.GAME_RES.height - 600);
	
	private Client client;
	
	private Color playerColor;
	private String playerTankType;
	
	private String gameMode;
	private String mapFile;
	
	private String[] availableMaps;
	
	private String serverAddress;
	private String portNumber;
	
	// GUI Components
	private JLabel colorPreview;
	private JLabel mapPreview;

	private JTextField serverAddressField;
	private JTextField serverPortField;

	/**
	 * Constructs the Main Menu UI as a new frame. Each of the settings containers are created then added to the 
	 * frame. 
	 * @param client The client corresponding to this Main Menu instance, whom it is created for.
	 */
	MainMenu(Client client) {
		
		this.client = client;
		
		frame = new JFrame("XTANK-MENU");
		// Store maps for combo choices
		availableMaps = new String[]{"./src/Lvl1BMP.bmp", "./src/Lvl2BMP.bmp", "./src/Lvl3BMP.bmp"};

		// Set Panel
		this.setPreferredSize(menuSize);
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Create each settings tab
		Container playerSettingsContainer = createPlayerSettingsUI();
		Container gameSettingsContainer = createGameSettingsUI();
		Container serverSettingsContainer = createServerSettingsUI();

		this.add(playerSettingsContainer);
		this.add(gameSettingsContainer);
		this.add(serverSettingsContainer);
		
		// Add panel to frame with fixed settings.
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.black);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	
	/**
	 * Opens menu frame and makes it visible.
	 */
	public void openMenu() {
		frame.setVisible(true);
	}
	
	/**
	 * Closes the frame by making it invisible and uninteractable
	 */
	public void closeMenu() {
		frame.setVisible(false);
	}
	

	/**
	 * Creates and returns the player settings section of the menu UI including Tank Type dropdown and Color
	 * RGB sliders with color preview.
	 * @return  Initialized player settings container for menu UI
	 */
	private Container createPlayerSettingsUI() {
		// Player Settings Section -------------------------------
		Container playerSettingsContainer = new Container();
		
		// Arranges player Settings in row
		FlowLayout playerSettingsLayout = new FlowLayout();
		playerSettingsLayout.setHgap(25);
		playerSettingsContainer.setLayout(playerSettingsLayout);
		
		// Tank type label and combo arranged vertically
		Container tankTypesContainer = new Container();
		tankTypesContainer.setLayout(new BoxLayout(tankTypesContainer, BoxLayout.Y_AXIS));
		// tank types label
		JLabel tankTypesLabel = new JLabel("Tank Type");
		tankTypesLabel.setForeground(Color.WHITE);
		tankTypesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Tank types combo
		String[] tankTypes = { "Standard", "Heavy", "Light" };
		JComboBox<String> tankTypeCombo = new JComboBox<String>(tankTypes);
		tankTypeCombo.addActionListener(new TankChangeListener());
		playerTankType = tankTypeCombo.getSelectedItem().toString();

		tankTypesContainer.add(tankTypesLabel);
		tankTypesContainer.add(tankTypeCombo);

		// RGB Sliders
		Container colorSlidersContainer = new Container();
		colorSlidersContainer.setLayout(new BoxLayout(colorSlidersContainer, BoxLayout.Y_AXIS));
		
		JLabel tankColorLabel = new JLabel("Tank Color");
		tankColorLabel.setForeground(Color.WHITE);
		tankColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		Container rContainer = new Container();
		rContainer.setLayout(new FlowLayout());
		Container gContainer = new Container();
		gContainer.setLayout(new FlowLayout());
		Container bContainer = new Container();
		bContainer.setLayout(new FlowLayout());

		JLabel rLabel = new JLabel("R");
		rLabel.setForeground(Color.WHITE);
		JLabel gLabel = new JLabel("G");
		gLabel.setForeground(Color.WHITE);
		JLabel bLabel = new JLabel("B");
		bLabel.setForeground(Color.WHITE);

		final int rgbMin = 32;
		final int rgbMax = 200;
		JSlider rSlider = new JSlider(JSlider.HORIZONTAL, rgbMin, rgbMax, 50);
		JSlider gSlider = new JSlider(JSlider.HORIZONTAL, rgbMin, rgbMax, 100);
		JSlider bSlider = new JSlider(JSlider.HORIZONTAL, rgbMin, rgbMax, 32);
		
		rSlider.addChangeListener(new RSliderListener());
		gSlider.addChangeListener(new GSliderListener());
		bSlider.addChangeListener(new BSliderListener());

		rContainer.add(rLabel);
		rContainer.add(rSlider);
		gContainer.add(gLabel);
		gContainer.add(gSlider);
		bContainer.add(bLabel);
		bContainer.add(bSlider);

		colorSlidersContainer.add(tankColorLabel);
		colorSlidersContainer.add(rContainer);
		colorSlidersContainer.add(gContainer);
		colorSlidersContainer.add(bContainer);
		
		// Color Preview 
		playerColor = new Color(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
		colorPreview = new JLabel("");
		updateColorGraphics();
		
		playerSettingsContainer.add(tankTypesContainer);
		playerSettingsContainer.add(colorSlidersContainer);
		playerSettingsContainer.add(colorPreview);
		
		return playerSettingsContainer;
	}
	

	/**
	 * Creates and returns the game settings section of the menu UI including Game Mode vote dropdown, Map Vote 
	 * dropdown and a map preview icon.
	 * @return  Initialized game settings container for menu UI
	 */
	private Container createGameSettingsUI() {
		Container gameSettingsContainer = new Container();
		FlowLayout gameSettingsLayout = new FlowLayout();
		gameSettingsLayout.setHgap(25);
		gameSettingsContainer.setLayout(gameSettingsLayout);
		// Game Modes combo
		Container gameModeContainer = new Container();
		gameModeContainer.setLayout(new BoxLayout(gameModeContainer, BoxLayout.Y_AXIS));
		JLabel gameModeLabel = new JLabel("Game Mode Vote");
		gameModeLabel.setForeground(Color.WHITE);
		gameModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		String[] gameModes = {"Standard", "DoubleHP", "Instakill"};
		JComboBox<String> gameModeVoteCombo = new JComboBox<String>(gameModes);
		gameModeVoteCombo.addActionListener(new ModeChangeListener());
		gameMode = gameModeVoteCombo.getSelectedItem().toString();
		
		gameModeContainer.add(gameModeLabel);
		gameModeContainer.add(gameModeVoteCombo);
		// Game Maps dropdown
		Container gameMapContainer = new Container();
		gameMapContainer.setLayout(new BoxLayout(gameMapContainer, BoxLayout.Y_AXIS));
		JLabel gameMapLabel = new JLabel("Game Map Vote");
		gameMapLabel.setForeground(Color.WHITE);
		gameMapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		String[] gameMaps = {"Tunnels", "Turbine", "Stadium"};
		JComboBox<String> mapVoteCombo = new JComboBox<String>(gameMaps);
		mapVoteCombo.addActionListener(new MapChangeListener());
		mapFile = availableMaps[mapVoteCombo.getSelectedIndex()];
		mapPreview = new JLabel("");
		updateMapPreviewGraphics();
		
		gameMapContainer.add(gameMapLabel);
		gameMapContainer.add(mapVoteCombo);
		
		gameSettingsContainer.add(gameModeContainer);
		gameSettingsContainer.add(gameMapContainer);
		gameSettingsContainer.add(mapPreview);
		
		return gameSettingsContainer;
	}
	
	/**
	 * Creates and returns the server settings section of menu UI including server address text field, port address field,
	 * connect button and readyUp checkBox.
	 * @return	Initialized server settings container for menu UI
	 */
	private Container createServerSettingsUI() {
		// Server settings container
		Container serverSettingsContainer = new Container();
		FlowLayout serverSettingsLayout = new FlowLayout();
		serverSettingsLayout.setHgap(25);
		serverSettingsContainer.setLayout(serverSettingsLayout);
		
		// Server address field
		Container serverAddressContainer = new Container();
		serverAddressContainer.setLayout(new BoxLayout(serverAddressContainer, BoxLayout.Y_AXIS));
		JLabel serverAddressLabel = new JLabel("Server Address");
		serverAddressLabel.setForeground(Color.WHITE);
		serverAddressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		serverAddressField = new JTextField(12);
		serverAddressField.setText("127.0.0.1");		// Autofill
		
		serverAddressContainer.add(serverAddressLabel);
		serverAddressContainer.add(serverAddressField);
		
		Container portContainer = new Container();
		portContainer.setLayout(new BoxLayout(portContainer, BoxLayout.Y_AXIS));
		
		JLabel portLabel = new JLabel("Port");
		portLabel.setForeground(Color.WHITE);
		portLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		serverPortField = new JTextField(9);
		serverPortField.setText("59896");			// Autofill
		
		portContainer.add(portLabel);
		portContainer.add(serverPortField);
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ConnectButtonListener());
		
		JCheckBox readyCheck = new JCheckBox("READY");
		readyCheck.addActionListener(new ReadyButtonListener());
		
		
		serverSettingsContainer.add(serverAddressContainer);
		serverSettingsContainer.add(portContainer);
		serverSettingsContainer.add(connectButton);
		serverSettingsContainer.add(readyCheck);
		
		return serverSettingsContainer;
	}

	/**
	 * Updates the realtime color preview display whenever RGB slider values are changed.
	 */
	public void updateColorGraphics() {
		BufferedImage colorImg = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = colorImg.createGraphics();
	    graphics.setPaint(playerColor);
	    graphics.fillRect (0, 0, 60, 60);
		ImageIcon colorIcon = new ImageIcon(colorImg);
		colorPreview.setIcon(colorIcon);
	}
	
	/**
	 * Updates the map preview Icon whenever the chosen map is changed
	 */
	public void updateMapPreviewGraphics() {
		File imageFile = new File(mapFile);
		BufferedImage mapImg;
		try {
			mapImg = ImageIO.read(imageFile);
			ImageIcon mapIcon = new ImageIcon(mapImg);
			mapPreview.setIcon(mapIcon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Records the tank type based on changes made to the tank type combo
	 */
	class TankChangeListener implements ActionListener{
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> combo = (JComboBox<String>)e.getSource();
			playerTankType = combo.getSelectedItem().toString();
		}
	}
	
	/**
	 * Updates player color and refreshes preview to match changes made to R slider
	 */
	class RSliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider slider = (JSlider)e.getSource();
	        playerColor = new Color(slider.getValue(), playerColor.getGreen(), playerColor.getBlue());
	        updateColorGraphics();
	    }
	}
	
	/**
	 * Updates player color and refreshes preview to match changes made to G slider
	 */
	class GSliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider slider = (JSlider)e.getSource();
	        playerColor = new Color(playerColor.getRed(), slider.getValue(), playerColor.getBlue());
	        updateColorGraphics();
	    }
	}
	
	/**
	 * Updates player color and refreshes preview to match changes made to B slider
	 */
	class BSliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider slider = (JSlider)e.getSource();
	        playerColor = new Color(playerColor.getRed(), playerColor.getGreen(), slider.getValue());
	        updateColorGraphics();
	    }
	}
	
	/**
	 * Updates game mode to match changes made to gamemode selection combo
	 */
	class ModeChangeListener implements ActionListener{
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> combo = (JComboBox<String>)e.getSource();
			gameMode = combo.getSelectedItem().toString();
		}
	}

	/**
	 * Updates map image preview and mapFile name whenever new map is selected from combo
	 *
	 */
	class MapChangeListener implements ActionListener{
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> combo = (JComboBox<String>)e.getSource();
			mapFile = availableMaps[combo.getSelectedIndex()];
			updateMapPreviewGraphics();
		}
	}
	
	/**
	 * Records server and port number and calls client to connect to server whenever connect button is pressed.
	 */
	class ConnectButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			serverAddress = serverAddressField.getText();
			portNumber = serverPortField.getText();
			client.connectToServer(serverAddress, portNumber);
		}
	}
	
	/**
	 * Tells client to either ready up or undo ready whenever the ready checkbox is pressed.
	 * If ready, passes all stored strings of game settings to client for game initialization.
	 */
	class ReadyButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JCheckBox readyBox = (JCheckBox)e.getSource();
			boolean isReady = readyBox.isSelected();
			if (isReady) {
				
				String r = ((Integer)playerColor.getRed()).toString();
				String g = ((Integer)playerColor.getGreen()).toString();
				String b = ((Integer)playerColor.getBlue()).toString();
				System.out.println("CLIENT SETTINGS: " + playerTankType + " " + r + " " + g + " " + b  + " " + gameMode + " " + mapFile);
				client.readyUp(playerTankType, r, g, b, gameMode, mapFile);
			}
			else {
				client.undoReadyUp();
			}
		}
		
	}


}



	
