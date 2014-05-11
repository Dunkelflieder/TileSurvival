package de.nerogar.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.nerogar.game.entity.*;

public class MapLoader {

	private static int getID(int color) {
		switch (color & 0xffff) {
		case 0xa000:
			return Map.GRASS.id;
		case 0x0000:
			return Map.ROCK.id;
		case 0x6000:
			return Map.TREE.id;
		case 0x8080:
			return Map.TORCH.id;
		case 0x0080:
			return Map.CHEST.id;
		case 0x00ff:
			return Map.DOOR.id;
		case 0xffff:
			return Map.GRASS.id;//spawn

		default:
			return Map.GRASS.id;
		}
	}

	private static boolean hasMob(int color) {
		return (color & 0xff0000) != 0x0;
	}

	private static boolean isSpawn(int color) {
		return (color & 0x00ffff) == 0xffff;
	}

	private static Entity getEntity(Map map, int color, float posX, float posY) {
		switch ((color & 0xff0000) >> 16) {
		case 0x20:
			return new EntitySmallEnemy(map, posX, posY);
		default:
			return null;
		}
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
			if (hasMob(pixels[i])) {
				Entity entity = getEntity(map, pixels[i], i % mapSize, i / mapSize);
				map.spawnEntity(entity);
			}
			if (isSpawn(pixels[i])) {
				playerX = i % mapSize;
				playerY = i / mapSize;
			}
		}

		map.load(tileIDs, mapSize, playerX, playerY);
		return map;
	}
}
