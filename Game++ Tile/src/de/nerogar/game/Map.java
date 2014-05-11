package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import de.nerogar.game.entity.*;
import de.nerogar.game.graphics.*;

public class Map {

	public static final Tile FLOOR = new Tile(0, false);
	public static final Tile ROCK = new Tile(1, true);
	public static final Tile TREE = new Tile(2, true);
	public static final Tile TORCH = new Tile(3, false);
	public static final Tile CHEST = new Tile(4, true);
	public static final Tile OPEN_CHEST = new Tile(5, true);
	public static final Tile DOOR = new Tile(6, false);
	public static final Tile DOOR_OPEN = new Tile(7, false);

	public static final Tile[] TILES = new Tile[] { FLOOR, ROCK, TREE, TORCH, CHEST, OPEN_CHEST, DOOR, DOOR_OPEN };

	public static final float TILE_RENDER_SIZE = 64f;
	public static final float TEXTURE_SIZE = 256f;
	public static final float TILE_TEXTURE_SIZE = 1f / TEXTURE_SIZE * 32f;
	public static final float TILES_ON_TEXTURE = 8f;
	public static final float TILE_PIXEL_COUNT = TEXTURE_SIZE / TILES_ON_TEXTURE;

	private Shader shader;
	private EntityPlayer player;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> newEntities;
	private float playTime;
	private float dayTime;
	private ArrayList<Light> lights;
	private final int MAX_LIGHTS = 100;
	private FloatBuffer lightBufferX;
	private FloatBuffer lightBufferY;
	private FloatBuffer lightBufferSize;
	private FloatBuffer lightBufferIntensity;
	private int[] tileIDs;
	private int size;

	private float offsX;
	private float offsY;
	private float tilesX;
	private float tilesY;

	public Map() {
		shader = new Shader("map");
		initShader();
		player = new EntityPlayer(this, new Vector());
		entities = new ArrayList<Entity>();
		newEntities = new ArrayList<Entity>();
		entities.add(player);
	}

	private void initShader() {
		shader.setVertexShader("res/shaders/map.vert");
		shader.setFragmentShader("res/shaders/map.frag");
		shader.compile();
	}

	public void spawnEntity(Entity entity) {
		newEntities.add(entity);
	}

	public boolean isColliding(Vector pos, Vector dimension) {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() + dimension.getX() >= size || pos.getY() + dimension.getY() >= size) return true;

		for (int i = Math.max((int) pos.getX(), 0); i <= Math.min((int) (pos.getX() + dimension.getX()), size - 1); i++) {
			for (int j = Math.max((int) pos.getY(), 0); j <= Math.min((int) (pos.getY() + dimension.getY()), size - 1); j++) {
				if (TILES[tileIDs[i + j * size]].collide) return true;
			}
		}

		return false;
	}

	public void update(float time) {
		playTime += time;
		float dayLength = 240f;
		dayTime = (float) Math.max(Math.sin(playTime * Math.PI * 2 / dayLength), 0.0f);

		for (int i = entities.size() - 1; i >= 0; i--) {
			if (entities.get(i).removed) {
				entities.remove(i);
			}
		}

		for (Entity entity : newEntities) {
			entities.add(entity);
		}
		newEntities.clear();

		for (Entity entity : entities) {
			entity.update(time);
		}

		offsX = player.pos.getX() - (((Display.getWidth() / TILE_RENDER_SIZE) - player.dimension.getX()) / 2f);
		offsY = player.pos.getY() - (((Display.getHeight() / TILE_RENDER_SIZE) - player.dimension.getY()) / 2f);

		tilesX = (Display.getWidth() / TILE_RENDER_SIZE);
		tilesY = (Display.getHeight() / TILE_RENDER_SIZE);

		offsX = Math.max(0f, Math.min(offsX, size - tilesX));
		offsY = Math.max(0f, Math.min(offsY, size - tilesY));

		calcLightSources();
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
		glUniform1f(glGetUniformLocation(shader.shaderHandle, "dayTime"), dayTime);

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

		for (Entity entity : entities) {
			entity.render();
		}
		shader.deactivate();

	}

	public void setTile(int x, int y, Tile tile) {
		tileIDs[x + y * size] = tile.id;
	}

	public void load(int[] tileIDs, int size, Vector playerPos) {
		this.tileIDs = tileIDs;
		this.size = size;
		player.pos = playerPos;

		for (int i = 0; i < 10; i++)
			spawnEntity(new EntityGhost(this, new Vector(19f, 19f)));
	}

	private void calcLightSources() {
		lights = new ArrayList<Light>();
		for (int i = 0; i < tileIDs.length; i++) {
			if (tileIDs[i] == TORCH.id) {
				lights.add(new Light(new Vector(((float) (i % size)) + 0.5f, ((float) (i / size)) + 0.5f), 5f, 1f));
			}
		}

		for (Entity entity : entities) {
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

	public EntityPlayer getPlayer() {
		return player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
