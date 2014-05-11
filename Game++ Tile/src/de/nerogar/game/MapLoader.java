package de.nerogar.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapLoader {

	private static int getID(int color) {
		switch (color & 0xffffff) {
		case 0x00a000:
			return Map.FLOOR.id;
		case 0x000000:
			return Map.ROCK.id;
		case 0x006000:
			return Map.TREE.id;
		case 0x008080:
			return Map.TORCH.id;
		case 0x000080:
			return Map.CHEST.id;
		case 0x0000ff:
			return Map.DOOR.id;
		case 0x00ffff:
			return Map.FLOOR.id;//spawn
		case 0xffaa00:
			return Map.FLOOR.id;

		default:
			return Map.FLOOR.id;
		}
	}

	private static boolean isSpawn(int color) {
		return (color & 0x00ffff) == 0xffff;
	}

	public static Map loadMap(String filename) {
		BufferedImage image;
		int[] pixels;
		int width;
		int height;

		float playerX = 0f;
		float playerY = 0f;

		try {
			image = ImageIO.read(new File("res/" + filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);

		Map map = new Map();
		int mapSize = width;
		int[] tileIDs = new int[mapSize * mapSize];

		for (int i = 0; i < pixels.length; i++) {
			tileIDs[i] = getID(pixels[i]);

			if (isSpawn(pixels[i])) {
				playerX = i % mapSize;
				playerY = i / mapSize;
			}
		}

		map.load(tileIDs, mapSize, playerX, playerY);
		return map;
	}
}
