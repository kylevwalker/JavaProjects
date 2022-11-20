import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * The Map grid converts a 32 by 32 bitmap into a grid of wallTiles with colliders used in creating the level walls.
 * @author Kyle Walker
 *
 */
public class MapGrid {
	
	protected File imageFile;
	static final int GRID_SIZE = 32;
	protected WallTile[][] wallGrid = new WallTile[GRID_SIZE][GRID_SIZE];
	protected BufferedImage mapImage;
	protected Vector2[]spawnLocations = {new Vector2(80,80), new Vector2(880,880), new Vector2(880,80), new Vector2(80,800)};
	
	/**
	 * Constructs a map grid using a file path string from the menu settings. If the file name is valid, then it will generate
	 * the walls.
	 * @param fileName String for the filepath of the bitmap.
	 */
	MapGrid(String fileName) {
		try {
			imageFile = new File(fileName);
			final BufferedImage image = ImageIO.read(imageFile);
			mapImage = image;
			
			// Iterates through bitmap and adds walls to grid where white pixels exist
			for (int y = 0; y < image.getHeight(); y++) {
	            for (int x = 0; x < image.getWidth(); x++) {
	                final int clr = image.getRGB(x, y);
	                if (clr == -1) {
	                	wallGrid[y][x] = new WallTile(x, y);
	                }
	            }
			}
			
		} catch (IOException e) {
			System.out.println("ERROR: File not found");
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws all of the wallgrids within the map.
	 * @param g 
	 */
	protected void draw(Graphics g) {
		for (int y=0; y<GRID_SIZE; y++) {
			for(int x=0; x<GRID_SIZE; x++) {
				if (wallGrid[y][x] != null) {
					wallGrid[y][x].draw(g);
				}
			}
		}
	}
	
	/**
	 * Returns the 3 adjacent horizontal tiles at a given location, used in calculating collisions.
	 * @param position	position to check for wall tiles within grid
	 * @return adjacentTiles
	 */
	public WallTile[] getHorizontalTilesAtPosition(Vector2 position) {
		WallTile[] adjacentTiles = new WallTile[3];
		int xIndex = (int)position.getX() / WallTile.TILE_SIZE;
		int yIndex = (int)position.getY() / WallTile.TILE_SIZE;
		if (yIndex> 0 && yIndex < 31 && xIndex < 32) {
			
			adjacentTiles[1] = wallGrid[yIndex][xIndex];
			// Only returns colliders when the center one is present. This allows tanks to slide across walls instead of stopping.
			if (adjacentTiles[1] != null) {
				adjacentTiles[0] = wallGrid[yIndex - 1][xIndex];
				adjacentTiles[2] = wallGrid[yIndex + 1][xIndex];
			}
		}
		return adjacentTiles;
	}
	
	/**
	 * Returns the 3 adjacent vertical tiles at a given location, used in calculating collisions.
	 * @param position	position to check for wall tiles within grid
	 * @return adjacentTiles
	 */
	public WallTile[] getVerticalTilesAtPosition(Vector2 position) {
		WallTile[] adjacentTiles = new WallTile[3];
		int xIndex = (int)position.getX() / WallTile.TILE_SIZE;
		int yIndex = (int)position.getY() / WallTile.TILE_SIZE;
		if (xIndex> 0 && xIndex < 31 && yIndex < 32) {
			
			adjacentTiles[1] = wallGrid[yIndex][xIndex];
			// Only returns colliders when the center one is present. This allows tanks to slide across walls instead of stopping.
			if (adjacentTiles[1] != null) {
				adjacentTiles[0] = wallGrid[yIndex][xIndex - 1];
				adjacentTiles[2] = wallGrid[yIndex][xIndex + 1];
			}
		}
		return adjacentTiles;
	}
	
	/**
	 * Returns the spawn location for each tank from the 4 available spawners of the map
	 * @param index	index for which spawner to access
	 * @return	spawnLocations[index]
	 */
	public Vector2 getSpawnLocation(int index) {
		if (index>3 || index < 0) {
			return null;
		}
		return spawnLocations[index];
	}
	
	/**
	 * Returns the image bitmap file used by the Map, used in previewing map in menu.
	 * @return mapImage
	 */
	public BufferedImage getMapImage() {
		return mapImage;
	}
	
}


