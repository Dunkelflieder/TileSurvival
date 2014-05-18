package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.*;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.graphics.TextureBank;

public abstract class Entity {

	public static final int DIR_UP = 0;
	public static final int DIR_RIGHT = 1;
	public static final int DIR_DOWN = 2;
	public static final int DIR_LEFT = 3;
	private int facingDir;
	private boolean turnable;

	public static int MAX_ID;
	public int id;

	public static final int FACTION_PLAYER = 1;
	public static final int FACTION_MOB = 2;
	public int faction;

	public Map map;

	public Vector pos;
	public Vector serverPos;

	public Vector dimension;

	public Light light;

	public float moveSpeed;
	public int maxHealth;
	public int health;
	public boolean resistDamage;
	public boolean ignoreWalls;

	public float speedmult;
	public float speedmultTime;

	public int poison;
	public float poisonTime;
	public float poisonCooldown;

	public int energy;
	public int maxEnergy;

	public boolean removed;
	public int textureID = 0;

	public static float textureSize = 512f;
	public static float tileTextureSize = 1f / textureSize * 32f;
	public static int tilesOnTexture = 16;

	public Entity(Map map, Vector pos, Vector dimension, int health, boolean turnable) {
		id = getNewID();

		this.map = map;
		this.pos = pos;
		this.serverPos = pos.clone();
		this.dimension = dimension;
		this.maxHealth = health;
		this.health = health;
		this.turnable = turnable;
		this.speedmult = 1.0f;
	}

	public void moveX(float distance) {
		if (!map.isColliding(pos.clone().addX(distance), dimension, ignoreWalls)) {
			pos.addX(distance);
			facingDir = (distance > 0) ? DIR_RIGHT : DIR_LEFT;
		}
	}

	public void moveY(float distance) {
		if (!map.isColliding(pos.clone().addY(distance), dimension, ignoreWalls)) {
			pos.addY(distance);
			facingDir = (distance > 0) ? DIR_DOWN : DIR_UP;
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

		if (poisonTime < 0) poison = 0;
		poisonTime -= time;
		poisonCooldown -= time;
		if (poisonCooldown < 0) {
			poisonCooldown = 0.5f;
			damage(poison);
		}
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

		int tId = turnable ? textureID + facingDir : textureID;
		int tilePosX = tId % tilesOnTexture;
		int tilePosY = tId / tilesOnTexture;

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

	public void renderAfterShader() {

	}

}
