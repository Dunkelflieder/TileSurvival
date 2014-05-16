package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.*;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.graphics.TextureBank;

public abstract class Entity {

	public static final int DIR_UP = 0;
	public static final int DIR_DOWN = 1;
	public static final int DIR_LEFT = 2;
	public static final int DIR_RIGHT = 4;

	public static int MAX_ID;
	public int id;

	public static final int FACTION_PLAYER = 1;
	public static final int FACTION_MOB = 2;
	public int faction;

	public Map map;

	public Vector pos;
	public Vector serverPos;
	public int facingDir;

	public Vector dimension;

	public Light light;

	public float moveSpeed;
	public int maxHealth;
	public int health;
	public boolean resistDamage;
	public boolean ignoreWalls;

	public float speedmult;
	public float speedmultTime;

	public int energy;
	public int maxEnergy;

	public boolean removed;
	public int textureID = 0;

	public static float textureSize = 512f;
	public static float tileTextureSize = 1f / textureSize * 32f;
	public static int tilesOnTexture = 16;

	public Entity(Map map, Vector pos, Vector dimension, int health) {
		id = getNewID();

		this.map = map;
		this.pos = pos;
		this.serverPos = pos.clone();
		this.dimension = dimension;
		this.maxHealth = health;
		this.health = health;
		this.speedmult = 1.0f;
	}

	public void moveX(float distance) {
		if (!map.isColliding(pos.clone().addX(distance), dimension, ignoreWalls)) {
			pos.addX(distance);
		}
	}

	public void moveY(float distance) {
		if (!map.isColliding(pos.clone().addY(distance), dimension, ignoreWalls)) {
			pos.addY(distance);
		}
	}

	public boolean intersects(Vector entityPos) {
		if (entityPos.getX() < pos.getX() || entityPos.getY() < pos.getY() || entityPos.getX() > pos.getX() + dimension.getX() || entityPos.getY() > pos.getY() + dimension.getY()) return false;
		return true;
	}

	public boolean intersects(Entity entity) {
		if (entity.pos.getX() + entity.dimension.getX() < pos.getX() || entity.pos.getY() + entity.dimension.getY() < pos.getY() || entity.pos.getX() > pos.getX() + dimension.getX() || entity.pos.getY() > pos.getY() + dimension.getY()) return false;
		return true;
	}

	public void damage(int damage) {
		if (resistDamage) return;
		health -= damage;
	}

	public void kill() {
		removed = true;
		onDie();
	}

	public abstract void onDie();

	public void update(float time) {
		if (health <= 0) kill();
		if (speedmultTime < 0) speedmult = 1.0f;
		speedmultTime -= time;
	}

	public void interpolatePosition(float time) {
		Vector dir = serverPos.subtracted(pos);
		if (dir.getSquaredValue() > 0.02) {
			dir.setValue(moveSpeed * time * speedmult);
			pos.add(dir);
		} else {
			pos.set(serverPos);
		}
	}

	public void render() {
		TextureBank.instance.bindTexture("entity.png", 0);
		TextureBank.instance.bindTexture("entity normal.png", 1);

		int tilePosX = textureID % tilesOnTexture;
		int tilePosY = textureID / tilesOnTexture;

		glBegin(GL_QUADS);

		glTexCoord2f(tilePosX * tileTextureSize, tilePosY * tileTextureSize);
		glVertex3f((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * tileTextureSize, tilePosY * tileTextureSize);
		glVertex3f((pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * tileTextureSize, (tilePosY + 1) * tileTextureSize);
		glVertex3f((pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY() + dimension.getY()) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(tilePosX * tileTextureSize, (tilePosY + 1) * tileTextureSize);
		glVertex3f((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY() + dimension.getY()) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();
	}

	public Vector getCenter() {
		return dimension.multiplied(0.5f).add(pos);
	}

	private int getNewID() {
		MAX_ID++;
		return MAX_ID;
	}
}
