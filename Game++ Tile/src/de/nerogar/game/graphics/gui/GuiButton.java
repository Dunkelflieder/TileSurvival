package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.TextureBank;

public class GuiButton extends GuiElement {

	private String text;

	private static final float TEX_OFFSET = 6f;
	private float pixelHeight = 32f;
	private float textureHeight = pixelHeight / Map.TEXTURE_SIZE;
	private float texturePos = 0;

	protected GuiButton(Vector pos, Vector size, String text) {
		super(pos, size);
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void render() {
		TextureBank.instance.bindTexture("gui.png");
		
		float posX = getPos().getX();
		float posY = getPos().getY();
		float sizeX = getSize().getX();
		float sizeY = getSize().getY();

		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f(posX * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f((posX + 4f) * Map.TILE_RENDER_SIZE, posY * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f((posX + 4f) * Map.TILE_RENDER_SIZE, (posY + (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f(posX * Map.TILE_RENDER_SIZE, (posY + (pixelHeight / Map.TILE_PIXEL_COUNT)) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();

		FontRenderer.drawString(text, posX * Map.TILE_RENDER_SIZE + 16, posY * Map.TILE_RENDER_SIZE, sizeX * Map.TILE_RENDER_SIZE - 32, sizeY * Map.TILE_RENDER_SIZE, FontRenderer.CENTERED);
	}

}
