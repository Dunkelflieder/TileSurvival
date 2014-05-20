package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import de.nerogar.game.entity.*;
import de.nerogar.game.entity.playerClass.PlayerClass;
import de.nerogar.game.graphics.*;
import de.nerogar.game.graphics.gui.FontRenderer;
import de.nerogar.game.graphics.gui.GuiBank;
import de.nerogar.game.network.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class Map {

	//tiles
	public static final Tile FLOOR = new Tile(0, false, 1);
	public static final Tile ROCK = new Tile(1, true);
	public static final Tile TREE = new Tile(2, true);
	public static final Tile TORCH = new Tile(3, false, 1);
	public static final Tile CHEST = new Tile(4, true);
	public static final Tile OPEN_CHEST = new Tile(5, true);
	public static final Tile DOOR = new Tile(6, false, 1);
	public static final Tile DOOR_OPEN = new Tile(7, false, 1);
	//tiles new
	public static final Tile WALL_V = new Tile(8, true);
	public static final Tile WALL_H = new Tile(9, true);
	public static final Tile WALL_TR = new Tile(10, true);
	public static final Tile WALL_TU = new Tile(11, true);
	public static final Tile WALL_TD = new Tile(12, true);
	public static final Tile WALL_TL = new Tile(13, true);
	public static final Tile WALL_RU = new Tile(14, true);
	public static final Tile WALL_RD = new Tile(15, true);
	public static final Tile WALL_LU = new Tile(16, true);
	public static final Tile WALL_LD = new Tile(17, true);
	public static final Tile WALL_CROSS = new Tile(18, true);

	public static final Tile FENCE_V = new Tile(19, true);
	public static final Tile FENCE_H = new Tile(20, true);
	public static final Tile FENCE_TD = new Tile(21, true);
	public static final Tile FENCE_TU = new Tile(22, true);
	public static final Tile FENCE_TR = new Tile(23, true);
	public static final Tile FENCE_DOOR_UL = new Tile(24, false);
	public static final Tile FENCE_DOOR_UR = new Tile(25, false);
	public static final Tile FENCE_DOOR_DL = new Tile(26, false);
	public static final Tile FENCE_DOOR_DR = new Tile(27, false);
	public static final Tile FENCE_DOOR_RU = new Tile(28, false);
	public static final Tile FENCE_DOOR_RD = new Tile(29, false);
	public static final Tile FENCE_DOOR_LD = new Tile(30, false);
	public static final Tile FENCE_DOOR_LU = new Tile(31, false);

	public static final Tile[] TILES = new Tile[] { FLOOR, ROCK, TREE, TORCH, CHEST, OPEN_CHEST, DOOR, DOOR_OPEN, //
			WALL_V, WALL_H, WALL_TR, WALL_TU, WALL_TD, WALL_TL, WALL_RU, WALL_RD, WALL_LU, WALL_LD, WALL_CROSS,//
			FENCE_V, FENCE_H, FENCE_TD, FENCE_TU, FENCE_TR, FENCE_DOOR_UL, FENCE_DOOR_UR, FENCE_DOOR_DL, FENCE_DOOR_DR, FENCE_DOOR_RU, FENCE_DOOR_RD, FENCE_DOOR_LD, FENCE_DOOR_LU };

	//texture
	public static final float TILE_RENDER_SIZE = 64f;
	public static final float TEXTURE_SIZE = 256f;
	public static final float TILE_TEXTURE_SIZE = 1f / TEXTURE_SIZE * 32f;
	public static final float TILES_ON_TEXTURE = 8f;
	public static final float TILE_PIXEL_COUNT = TEXTURE_SIZE / TILES_ON_TEXTURE;

	//attribs
	private Shader shader;
	private EntityPlayer player;
	private int playerID = -1;
	private Vector spawnLocation;
	private Vector[] enemySpawnLocations;
	private HashMap<Integer, Entity> entities;
	private ArrayList<Entity> newEntities;
	private boolean updating;
	private Wave wave;
	private ArrayList<Light> lights;
	private final int MAX_LIGHTS = 100;
	private FloatBuffer lightBufferX;
	private FloatBuffer lightBufferY;
	private FloatBuffer lightBufferSize;
	private FloatBuffer lightBufferIntensity;
	private int[] tileIDs;
	private int size;
	public boolean ready;

	//server
	private int worldType;
	private float nextUpdate;
	public static final int SERVER_WORLD = 0;
	public static final int CLIENT_WORLD = 1;

	private float offsX;
	private float offsY;
	private float tilesX;
	private float tilesY;

	public Map(int worldType) {
		this.worldType = worldType;

		player = new EntityPlayer(this, new Vector());
		shader = new Shader("map");
		initShader();
		entities = new HashMap<Integer, Entity>();
		newEntities = new ArrayList<Entity>();
		wave = new Wave(this);
	}

	private void initShader() {
		shader.setVertexShader("res/shaders/map.vert");
		shader.setFragmentShader("res/shaders/map.frag");
		shader.compile();
	}

	public void spawnEntity(Entity entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (entity.id == playerID) {
				player.setPlayerClass(PlayerClass.getInstanceByID(getPlayer().pClass, player));
				setPlayer(player);
				playerID = -1;
			}
			if (worldType == Map.CLIENT_WORLD) player.setPlayerClass(PlayerClass.getInstanceByID(GuiBank.LOBBY_CLIENT_CONNECT.getPlayerClass(player.id), player));
		}

		if (worldType == SERVER_WORLD) {
			PacketSpawnEntity spawnEntitypacket = new PacketSpawnEntity();
			spawnEntitypacket.entityID = entity.id;
			spawnEntitypacket.spawnID = EntitySpawner.getSpawnID(entity);
			spawnEntitypacket.pos = new float[] { entity.pos.getX(), entity.pos.getY() };

			Game.game.server.broadcastData(spawnEntitypacket);

			if (updating) {
				newEntities.add(entity);
			} else {
				entities.put(entity.id, entity);
			}
		} else {
			entities.put(entity.id, entity);
		}

	}

	public boolean isColliding(Vector pos, Vector dimension, boolean ignoreWalls) {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() + dimension.getX() >= size || pos.getY() + dimension.getY() >= size) return true;
		if (ignoreWalls) return false;

		for (int i = Math.max((int) pos.getX(), 0); i <= Math.min((int) (pos.getX() + dimension.getX()), size - 1); i++) {
			for (int j = Math.max((int) pos.getY(), 0); j <= Math.min((int) (pos.getY() + dimension.getY()), size - 1); j++) {
				if (TILES[tileIDs[i + j * size]].collide) return true;
			}
		}

		return false;
	}

	public void update(float time) {
		if (worldType == SERVER_WORLD) {

			for (Entity entity : getEntities()) {
				if (entity.removed) {
					entities.remove(entity.id);
					PacketDespawnEntity despawnEntityPacket = new PacketDespawnEntity();
					despawnEntityPacket.entityID = entity.id;
					Game.game.server.broadcastData(despawnEntityPacket);
				}
			}

			for (Entity entity : newEntities) {
				entities.put(entity.id, entity);
			}
			newEntities.clear();

			updating = true;
			for (Entity entity : entities.values()) {
				entity.update(time);
				if (entity instanceof EntityPlayer) ((EntityPlayer) entity).updateStats(time);
			}
			updating = false;

			wave.update(time);

			for (Client client : Game.game.server.getClients()) {
				ArrayList<Packet> packets = client.getData(Packet.WORLD_CHANNEL);
				if (packets != null) {
					for (Packet packet : packets) {
						processServerPacket(packet);
					}
				}
			}
		} else {
			for (Entity entity : entities.values()) {
				if (entity != player) {
					entity.interpolatePosition(time);
				}
			}
		}

		if (!GuiBank.interceptsInput()) player.updateInput(time, Game.game.client);
		player.updateStats(time);

		offsX = player.pos.getX() - (((Display.getWidth() / TILE_RENDER_SIZE) - player.dimension.getX()) / 2f);
		offsY = player.pos.getY() - (((Display.getHeight() / TILE_RENDER_SIZE) - player.dimension.getY()) / 2f);

		tilesX = (Display.getWidth() / TILE_RENDER_SIZE);
		tilesY = (Display.getHeight() / TILE_RENDER_SIZE);

		offsX = Math.max(0f, Math.min(offsX, size - tilesX));
		offsY = Math.max(0f, Math.min(offsY, size - tilesY));

		calcLightSources();

		if (worldType == SERVER_WORLD) {
			if (nextUpdate < 0) {
				sendUpdate();
				nextUpdate = 0.1f;
			}
			nextUpdate -= time;
		} else {
			ArrayList<Packet> packets = Game.game.client.getData(Packet.WORLD_CHANNEL);
			if (packets != null) {
				for (Packet packet : packets) {
					processClientPacket(packet);
				}
			}
		}

	}

	private void processServerPacket(Packet packet) {
		if (packet instanceof PacketPlayerPosition) {
			PacketPlayerPosition playerPositionPacket = (PacketPlayerPosition) packet;
			Entity entity = entities.get(playerPositionPacket.playerID);

			if (entity != null) {
				entity.pos.setX(playerPositionPacket.playerPosition[0]);
				entity.pos.setY(playerPositionPacket.playerPosition[1]);
				entity.facingDir = playerPositionPacket.playerDirection;
			}
		} else if (packet instanceof PacketActivateWeapon) {
			PacketActivateWeapon activateWeaponPacket = (PacketActivateWeapon) packet;
			Entity entity = entities.get(activateWeaponPacket.playerID);

			if (entity != null && entity instanceof EntityPlayer) {
				EntityPlayer playerEntity = (EntityPlayer) entity;
				if (playerEntity.getPlayerClass().getSelectedWeapon().energyCost <= playerEntity.energy) {
					playerEntity.energy -= playerEntity.getPlayerClass().getSelectedWeapon().energyCost;
					playerEntity.getPlayerClass().selectWeapon(activateWeaponPacket.selectedWeapon);
					playerEntity.getSelectedWeapon().start(new Vector(activateWeaponPacket.targetPosition[0], activateWeaponPacket.targetPosition[1]));
					playerEntity.getPlayerClass().setCurrentWeaponUsed();
				}
			}

		}
	}

	private void processClientPacket(Packet packet) {
		if (packet instanceof PacketEntityPositions) {
			PacketEntityPositions entityPositionsPacket = (PacketEntityPositions) packet;
			for (int i = 0; i < entityPositionsPacket.entityPositions.length / 2; i++) {
				int id = entityPositionsPacket.entityIDs[i];
				Entity entity = entities.get(id);
				if (entity != null) {
					if (entity != player) {
						entity.serverPos.setX(entityPositionsPacket.entityPositions[i * 2]);
						entity.serverPos.setY(entityPositionsPacket.entityPositions[i * 2 + 1]);
						entity.facingDir = entityPositionsPacket.entityDirections[i];
					}
					entity.moveSpeed = entityPositionsPacket.entityMoveSpeeds[i];
					entity.speedmult = entityPositionsPacket.entitySpeedMults[i];
					//entity.maxHealth = Integer.MAX_VALUE;
					entity.health = (int) (entityPositionsPacket.entityHealths[i] * entity.maxHealth);
					//entity.maxEnergy = Integer.MAX_VALUE;
					entity.energy = (int) (entityPositionsPacket.entityEnergys[i] * entity.maxEnergy);
				}
			}

		} else if (packet instanceof PacketSpawnEntity) {
			PacketSpawnEntity entitySpawnPacket = (PacketSpawnEntity) packet;
			Entity entity = EntitySpawner.spawnEntity(this, new Vector(entitySpawnPacket.pos[0], entitySpawnPacket.pos[1]), entitySpawnPacket.spawnID);
			entity.id = entitySpawnPacket.entityID;
			spawnEntity(entity);
		} else if (packet instanceof PacketDespawnEntity) {
			PacketDespawnEntity entityDespawnPacket = (PacketDespawnEntity) packet;
			entities.remove(entityDespawnPacket.entityID);
		}
	}

	private void sendUpdate() {
		PacketEntityPositions entityPositionsPacket = new PacketEntityPositions();

		float[] entityPositions = new float[entities.size() * 2];
		int[] entityIDs = new int[entities.size()];
		float[] entityMoveSpeeds = new float[entities.size()];
		float[] entitySpeedMults = new float[entities.size()];
		int[] entityDirections = new int[entities.size()];
		float[] entityHealths = new float[entities.size()];
		float[] entityEnergys = new float[entities.size()];

		int i = 0;
		for (Entity entity : entities.values()) {
			entityIDs[i] = entity.id;
			entityPositions[i * 2] = entity.pos.getX();
			entityPositions[i * 2 + 1] = entity.pos.getY();
			entityMoveSpeeds[i] = entity.moveSpeed;
			entitySpeedMults[i] = entity.speedmult;
			entityDirections[i] = entity.facingDir;
			entityHealths[i] = (float) entity.health / entity.maxHealth;
			entityEnergys[i] = (float) entity.energy / entity.maxEnergy;
			i++;
		}

		entityPositionsPacket.entityPositions = entityPositions;
		entityPositionsPacket.entityIDs = entityIDs;
		entityPositionsPacket.entityMoveSpeeds = entityMoveSpeeds;
		entityPositionsPacket.entitySpeedMults = entitySpeedMults;
		entityPositionsPacket.entityDirections = entityDirections;
		entityPositionsPacket.entityHealths = entityHealths;
		entityPositionsPacket.entityEnergys = entityEnergys;

		Game.game.server.broadcastData(entityPositionsPacket);
	}

	public void render() {
		RenderHelper.disableAlpha();
		//shader.reloadFiles();
		//shader.compile();

		TextureBank.instance.bindTexture("tiles.png", 0);
		TextureBank.instance.bindTexture("tiles normal.png", 1);
		shader.activate();

		glUniform2f(glGetUniformLocation(shader.shaderHandle, "offset"), offsX, offsY);
		glUniform1f(glGetUniformLocation(shader.shaderHandle, "scale"), TILE_RENDER_SIZE);
		glUniform1i(glGetUniformLocation(shader.shaderHandle, "colorTex"), 0);
		glUniform1i(glGetUniformLocation(shader.shaderHandle, "normalTex"), 1);

		glUniform1(glGetUniformLocation(shader.shaderHandle, "lightsX"), lightBufferX);
		glUniform1(glGetUniformLocation(shader.shaderHandle, "lightsY"), lightBufferY);
		glUniform1(glGetUniformLocation(shader.shaderHandle, "lightsSize"), lightBufferSize);
		glUniform1(glGetUniformLocation(shader.shaderHandle, "lightsIntensity"), lightBufferIntensity);
		glUniform1i(glGetUniformLocation(shader.shaderHandle, "lightsCount"), lights.size() > MAX_LIGHTS ? MAX_LIGHTS : lights.size());

		float tilesX = (Display.getWidth() / TILE_RENDER_SIZE) + 1f;
		float tilesY = (Display.getHeight() / TILE_RENDER_SIZE) + 1f;

		for (int i = Math.max(0, (int) offsX); i < Math.min(offsX + (tilesX), size); i++) {
			for (int j = Math.max(0, (int) offsY); j < Math.min(offsY + (tilesY), size); j++) {

				int tileID = tileIDs[i + j * size];
				int tilePosX = tileID % (int) TILES_ON_TEXTURE;
				int tilePosY = tileID / (int) TILES_ON_TEXTURE;

				glBegin(GL_QUADS);
				glTexCoord2f(tilePosX * TILE_TEXTURE_SIZE, tilePosY * TILE_TEXTURE_SIZE);
				glVertex3f((i * TILE_RENDER_SIZE) - (offsX * TILE_RENDER_SIZE), (j * TILE_RENDER_SIZE) - (offsY * TILE_RENDER_SIZE), -1f);

				glTexCoord2f((tilePosX + 1) * TILE_TEXTURE_SIZE, tilePosY * TILE_TEXTURE_SIZE);
				glVertex3f((i * TILE_RENDER_SIZE + TILE_RENDER_SIZE) - (offsX * TILE_RENDER_SIZE), (j * TILE_RENDER_SIZE) - (offsY * TILE_RENDER_SIZE), -1f);

				glTexCoord2f((tilePosX + 1) * TILE_TEXTURE_SIZE, (tilePosY + 1) * TILE_TEXTURE_SIZE);
				glVertex3f((i * TILE_RENDER_SIZE + TILE_RENDER_SIZE) - (offsX * TILE_RENDER_SIZE), (j * TILE_RENDER_SIZE + TILE_RENDER_SIZE) - (offsY * TILE_RENDER_SIZE), -1f);

				glTexCoord2f(tilePosX * TILE_TEXTURE_SIZE, (tilePosY + 1) * TILE_TEXTURE_SIZE);
				glVertex3f((i * TILE_RENDER_SIZE) - (offsX * TILE_RENDER_SIZE), (j * TILE_RENDER_SIZE + TILE_RENDER_SIZE) - (offsY * TILE_RENDER_SIZE), -1f);

				glEnd();
			}
		}

		RenderHelper.enableAlphaMask();
		for (Entity entity : entities.values()) {
			if (!(entity instanceof EntityPlayer || entity instanceof EntityEnemy)) entity.render();
		}
		for (Entity entity : entities.values()) {
			if (entity instanceof EntityPlayer || entity instanceof EntityEnemy) entity.render();
		}
		RenderHelper.disableAlpha();

		shader.deactivate();

		// render additionals without shader
		for (Entity entity : entities.values()) {
			entity.renderAfterShader();
		}

		glColor3f(1, 0, 0);
		FontRenderer.drawString("Wave: " + getWave(), 32f, 32f, Game.game.WIDTH - 64f, 16f, FontRenderer.LEFT);
		glColor3f(1, 1, 1);

	}

	public void setTile(int x, int y, Tile tile) {
		tileIDs[x + y * size] = tile.id;
	}

	public Tile getTileAt(int x, int y) {
		if (x < 0 || y < 0 || x >= size || y >= size) return null;
		return getTile(tileIDs[x + y * size]);
	}

	public Tile getTile(int id) {
		for (Tile t : TILES)
			if (t.id == id) return t;
		return null;
	}

	public void load(int[] tileIDs, int size, Vector spawnLocation, Vector[] enemySpawnLocations) {
		this.tileIDs = tileIDs;
		this.size = size;
		this.spawnLocation = spawnLocation;
		this.enemySpawnLocations = enemySpawnLocations;

		Pathfinder.init(this);

		for (int i = 0; i < 10; i++) {
			//spawnEntity(new EnemySkeleton(this, new Vector((float) (19f + Math.random() * 15f), (float) (19f + Math.random() * 15f))));
			//spawnEntity(new EnemyRat(this, new Vector((float) (19f + Math.random() * 15f), (float) (19f + Math.random() * 15f))));
			//spawnEntity(new EnemyNekro(this, new Vector((float) (19f + Math.random() * 15f), (float) (19f + Math.random() * 15f))));
			//spawnEntity(new EnemyBigSkeleton(this, new Vector((float) (19f + Math.random() * 15f), (float) (19f + Math.random() * 15f))));
		}
	}

	public void initPlayer(int playerID) {
		EntityPlayer player = (EntityPlayer) entities.get(playerID);
		if (player == null) {
			this.playerID = playerID;
		} else {
			setPlayer(player);
		}
		ready = true;
	}

	private void setPlayer(EntityPlayer player) {
		this.player = player;
		player.pos = getSpawnLocation();
	}

	private void calcLightSources() {
		lights = new ArrayList<Light>();
		for (int i = 0; i < tileIDs.length; i++) {
			if (tileIDs[i] == TORCH.id) {
				lights.add(new Light(new Vector(((float) (i % size)) + 0.5f, ((float) (i / size)) + 0.5f), 5f, 1f));
			}
		}

		for (Entity entity : entities.values()) {
			if (entity.light != null) {
				lights.add(new Light(entity.getCenter(), entity.light.size, entity.light.intensity));
			}
		}

		float[] lightBufferXArray = new float[MAX_LIGHTS];
		float[] lightBufferYArray = new float[MAX_LIGHTS];
		float[] lightBufferSizeArray = new float[MAX_LIGHTS];
		float[] lightBufferIntensityArray = new float[MAX_LIGHTS];

		int index = 0;

		for (Light light : lights) {
			if (light.inArea(offsX, offsY, (Display.getWidth() / TILE_RENDER_SIZE), (Display.getHeight() / TILE_RENDER_SIZE))) {
				lightBufferXArray[index] = light.pos.getX();
				lightBufferYArray[index] = light.pos.getY();
				lightBufferSizeArray[index] = light.size;
				lightBufferIntensityArray[index] = light.intensity;
				index++;
			}
			if (index >= MAX_LIGHTS) break;
		}

		//System.out.println("lights:" + index);

		lightBufferX = BufferUtils.createFloatBuffer(lightBufferXArray.length);
		lightBufferX.put(lightBufferXArray);
		lightBufferX.flip();

		lightBufferY = BufferUtils.createFloatBuffer(lightBufferYArray.length);
		lightBufferY.put(lightBufferYArray);
		lightBufferY.flip();

		lightBufferSize = BufferUtils.createFloatBuffer(lightBufferSizeArray.length);
		lightBufferSize.put(lightBufferSizeArray);
		lightBufferSize.flip();

		lightBufferIntensity = BufferUtils.createFloatBuffer(lightBufferIntensityArray.length);
		lightBufferIntensity.put(lightBufferIntensityArray);
		lightBufferIntensity.flip();
	}

	public int getSize() {
		return size;
	}

	public float getOffsX() {
		return offsX;
	}

	public float getOffsY() {
		return offsY;
	}

	public Vector getSpawnLocation() {
		return spawnLocation.clone();
	}

	public Vector[] getEnemySpawnLocations() {
		return enemySpawnLocations;
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public ArrayList<Entity> getPlayers() {
		ArrayList<Entity> players = new ArrayList<Entity>();
		for (Entity entity : entities.values()) {
			if (entity.faction == Entity.FACTION_PLAYER) {
				players.add(entity);
			}
		}

		return players;
	}

	public Entity getNearestPlayer(Vector pos) {
		Entity retPlayer = null;
		float dist = Float.MAX_VALUE;
		ArrayList<Entity> players = getPlayers();
		for (int i = 0; i < players.size(); i++) {
			float newDist = players.get(i).pos.subtracted(pos).getSquaredValue();
			if (newDist < dist) {
				retPlayer = players.get(i);
				dist = newDist;
			}
		}
		return retPlayer;
	}

	public Entity getRandomPlayer() {
		ArrayList<Entity> players = getPlayers();
		int index = (int) (Math.random() * players.size());
		return players.get(index);
	}

	public ArrayList<Entity> getEntities() {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		entityList.addAll(entities.values());
		return entityList;
	}

	public ArrayList<Entity> getIntersectingEntities(Entity entity) {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		for (Entity targetEntity : entities.values()) {
			if (entity != targetEntity && entity.intersects(targetEntity)) entityList.add(targetEntity);
		}
		return entityList;
	}

	public int getWave() {
		return wave.wave;
	}

}
