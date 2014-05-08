package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import de.nerogar.game.entity.*;
import de.nerogar.game.graphics.*;

public class Map {

	public static final Tile GRASS = new Tile(0, false);
	public static final Tile ROCK = new Tile(1, true);
	public static final Tile TREE = new Tile(2, true);
	public static final Tile TORCH = new Tile(3, false);

	public static final Tile[] TILES = new Tile[] { GRASS, ROCK, TREE, TORCH };

	private MapShader shader;
	private EntityPlayer player;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> newEntities;
	private ArrayList<Light> lights;
	private String lightString;
	private int[] tileIDs;
	private int size;
	private float tileSize = 64f;
	private float textureSize = 256f;
	private float tileTextureSize = 1f / textureSize * 32f;
	private int tilesOnTexture = 8;

	private float offsX;
	private float offsY;
	private float tilesX;
	private float tilesY;

	public Map() {
		shader = new MapShader("map");
		initShader();
		player = new EntityPlayer(this, 0f, 0f);
		entities = new ArrayList<Entity>();
		newEntities = new ArrayList<Entity>();
		entities.add(player);
	}

	private void initShader() {
		shader.setVertexShader("res/shaders/map.vert");
		shader.setFragmentShader("res/shaders/map.frag");
	}

	public void spawnEntity(Entity entity) {
		newEntities.add(entity);
	}

	public boolean isColliding(float x, float y, float width, float height) {
		for (int i = (int) x; i <= (int) (x + width); i++) {
			for (int j = (int) y; j <= (int) (y + height); j++) {
				if (TILES[tileIDs[i + j * size]].collide) return true;
			}
		}

		return false;
	}

	public void update(float time) {
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

		offsX = player.posX - (((Display.getWidth() / tileSize) - player.width) / 2f);
		offsY = player.posY - (((Display.getHeight() / tileSize) - player.height) / 2f);

		tilesX = (Display.getWidth() / tileSize);
		tilesY = (Display.getHeight() / tileSize);

		offsX = Math.max(0f, Math.min(offsX, size - tilesX));
		offsY = Math.max(0f, Math.min(offsY, size - tilesY));

		calcLightSources();
	}

	public void render() {
		RenderHelper.disableAlpha();
		shader.reloadFiles();
		shader.compile(lightString);

		TextureBank.instance.bindTexture("tiles.png");
		shader.activate();

		glUniform2f(glGetUniformLocation(shader.shaderHandle, "offset"), offsX, offsY);
		glUniform1f(glGetUniformLocation(shader.shaderHandle, "scale"), tileSize);

		float tilesX = (Display.getWidth() / tileSize) + 1f;
		float tilesY = (Display.getHeight() / tileSize) + 1f;

		for (int i = Math.max(0, (int) offsX); i < Math.min(offsX + (tilesX), size); i++) {
			for (int j = Math.max(0, (int) offsY); j < Math.min(offsY + (tilesY), size); j++) {

				int tileID = tileIDs[i + j * size];
				int tilePosX = tileID % tilesOnTexture;
				int tilePosY = tileID / tilesOnTexture;

				glBegin(GL_QUADS);
				glTexCoord2f(tilePosX * tileTextureSize, tilePosY * tileTextureSize);
				glVertex3f((i * tileSize) - (offsX * tileSize), (j * tileSize) - (offsY * tileSize), -1f);

				glTexCoord2f((tilePosX + 1) * tileTextureSize, tilePosY * tileTextureSize);
				glVertex3f((i * tileSize + tileSize) - (offsX * tileSize), (j * tileSize) - (offsY * tileSize), -1f);

				glTexCoord2f((tilePosX + 1) * tileTextureSize, (tilePosY + 1) * tileTextureSize);
				glVertex3f((i * tileSize + tileSize) - (offsX * tileSize), (j * tileSize + tileSize) - (offsY * tileSize), -1f);

				glTexCoord2f(tilePosX * tileTextureSize, (tilePosY + 1) * tileTextureSize);
				glVertex3f((i * tileSize) - (offsX * tileSize), (j * tileSize + tileSize) - (offsY * tileSize), -1f);

				glEnd();
			}
		}

		RenderHelper.enableAlphaMask();

		for (Entity entity : entities) {
			entity.render();
		}
		shader.deactivate();

	}

	public void load(int[] tileIDs, int size) {
		this.tileIDs = tileIDs;
		this.size = size;

	}

	private void calcLightSources() {
		lights = new ArrayList<Light>();
		for (int i = 0; i < tileIDs.length; i++) {
			if (tileIDs[i] == TORCH.id) {
				lights.add(new Light(((float) (i % size)) + 0.5f, ((float) (i / size)) + 0.5f, 5f));
			}
		}

		for (Entity entity : entities) {
			if (entity instanceof EntityFireball) {
				lights.add(new Light(entity.posX, entity.posY, 1f));
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("");
		for (int i = 0; i < lights.size(); i++) {
			if (i < lights.size() - 1) {
				sb.append(lights.get(i).toString()).append("+");
			} else {
				sb.append(lights.get(i).toString());
			}
		}

		lightString = sb.toString();
	}

	public int getSize() {
		return size;
	}

	public float getTileSize() {
		return tileSize;
	}

	public float getOffsX() {
		return offsX;
	}

	public float getOffsY() {
		return offsY;
	}

}
