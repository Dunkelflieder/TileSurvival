package de.nerogar.game.graphics;

import static org.lwjgl.opengl.GL11.*;
import de.nerogar.game.Map;

public class StatusBar {
	public float position;

	private float pixelHeight = 7f;
	private float textureHeight = pixelHeight / Map.TEXTURE_SIZE;
	private float texturePos = 0;

	private Map map;
	public float posX;
	public float posY;

	public StatusBar(Map map) {
		this.map = map;
	}

	public void render() {
		TextureBank.instance.bindTexture("gui.png");
		texturePos = 1;

		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * 7f + textureHeight * texturePos);
		glVertex3f(posX * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * 7f + textureHeight * texturePos);
		glVertex3f((posX + 4f) * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * 7f + textureHeight * (texturePos + 1));
		glVertex3f((posX + 4f) * Map.TILE_RENDER_SIZE, (posY + (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * 7f + textureHeight * (texturePos + 1));
		glVertex3f(posX * Map.TILE_RENDER_SIZE, (posY +  (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();

	}
}