package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.*;
import de.nerogar.game.Map;
import de.nerogar.game.graphics.TextureBank;

public abstract class Entity {

	public Map map;

	public float posX;
	public float posY;
	public float width = 1f;
	public float height = 1f;

	public float moveSpeed;
	public int maxHealth;
	public int health;
	public boolean removed;

	public int textureID = 0;

	private float textureSize = 512f;
	private float tileTextureSize = 1f / textureSize * 32f;
	private int tilesOnTexture = 16;

	public Entity(Map map, float posX, float posY, int health) {
		this.map = map;
		this.posX = posX;
		this.posY = posY;
		this.maxHealth = health;
		this.health = health;
	}

	public void moveX(float distance) {
		if (!map.isColliding(posX + distance, posY, width, height)) {
			posX += distance;
		}
	}

	public void moveY(float distance) {
		if (!map.isColliding(posX, posY + distance, width, height)) {
			posY += distance;
		}
	}

	public void remove() {
		removed = true;
	}

	public abstract void update(float time);

	public void render() {
		TextureBank.instance.bindTexture("entity.png");

		int tilePosX = textureID % tilesOnTexture;
		int tilePosY = textureID / tilesOnTexture;

		glBegin(GL_QUADS);

		glTexCoord2f(tilePosX * tileTextureSize, tilePosY * tileTextureSize);
		glVertex3f((posX - map.getOffsX()) * Map.TILE_RENDER_SIZE, (posY - map.getOffsY()) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * tileTextureSize, tilePosY * tileTextureSize);
		glVertex3f((posX - map.getOffsX() + width) * Map.TILE_RENDER_SIZE, (posY - map.getOffsY()) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * tileTextureSize, (tilePosY + 1) * tileTextureSize);
		glVertex3f((posX - map.getOffsX() + width) * Map.TILE_RENDER_SIZE, (posY - map.getOffsY() + height) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(tilePosX * tileTextureSize, (tilePosY + 1) * tileTextureSize);
		glVertex3f((posX - map.getOffsX()) * Map.TILE_RENDER_SIZE, (posY - map.getOffsY() + height) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();
	}
}
