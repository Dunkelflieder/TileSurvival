package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.Map;
import de.nerogar.game.RenderHelper;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.TextureBank;

public class GuiStatusBar extends GuiElement {

	private float position = 1f;

	private static final float TEX_OFFSET = 7f;

	private float pixelHeight = 7f;
	private float textureHeight = pixelHeight / Map.TEXTURE_SIZE;
	private float texturePos;
	
	public GuiStatusBar(Vector position, int texturePos) {
		super(position, new Vector(0, 0));
		this.texturePos = texturePos;
	}

	@Override
	public void render() {
		TextureBank.instance.bindTexture("gui.png");

		RenderHelper.enableAlphaMask();
		renderBar(2f, 1f);
		renderBar(texturePos, position);
	}

	private void renderBar(float texturePos, float position) {
		float posX = getPos().getX();
		float posY = getPos().getY();
		
		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f(posX * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f * position, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f((posX + 4f * position) * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f * position, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f((posX + 4f * position) * Map.TILE_RENDER_SIZE, (posY + (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f(posX * Map.TILE_RENDER_SIZE, (posY + (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

}
