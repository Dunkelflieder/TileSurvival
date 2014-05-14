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

public class GEStatusBar extends GuiElement {

	private float position = 1f;

	private static final float TEX_OFFSET = 7f;
	private float pixelMult = Map.TILE_RENDER_SIZE / Map.TILE_PIXEL_COUNT;

	private float texturePos;

	public GEStatusBar(Vector position, int texturePos) {
		super(position, new Vector(Map.TILE_RENDER_SIZE * 4f, 0), false);
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
		float pixelHeight = 7f;
		float textureHeight = pixelHeight / Map.TEXTURE_SIZE;

		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f(getPos().getX(), getPos().getY(), -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f * position, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * texturePos);
		glVertex3f(getPos().getX() + getSize().getX() * position, getPos().getY(), -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f * position, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f(getPos().getX() + getSize().getX() * position, getPos().getY() + pixelHeight * pixelMult, -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + textureHeight * (texturePos + 1));
		glVertex3f(getPos().getX(), getPos().getY() + pixelHeight * pixelMult, -1f);

		glEnd();
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

}
