package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Mouse;

import de.nerogar.game.InputHandler;
import de.nerogar.game.Map;
import de.nerogar.game.RenderHelper;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.TextureBank;

public class GESlider extends GuiElement {

	private static final float TEX_OFFSET = 5f;

	private boolean dragging = false;
	private float min, max;
	private float position;
	private String text = String.valueOf(position);
	private float sliderWidth = 8f;
	private boolean allowFloat = true;

	protected GESlider(Vector pos, Vector size, String text, float min, float max, float position) {
		super(pos, size, false);
		this.min = min;
		this.max = max;
		this.position = position;
	}

	@Override
	public void render() {
		TextureBank.instance.bindTexture("gui.png");
		RenderHelper.enableAlphaMask();

		float sliderPixelHeight = 32f;
		float sliderTextureHeight = sliderPixelHeight / Map.TEXTURE_SIZE;
		float sliderPixelWidth = 4f;

		float posX = getPos().getX();
		float posY = getPos().getY();
		float sizeX = getSize().getX();
		float sizeY = getSize().getY();

		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(posX, posY, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(posX + sizeX, posY, -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX + sizeX, posY + sizeY, -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX, posY + sizeY, -1f);

		glEnd();

		double scale = (sizeX - sliderWidth * 2f) / (max - min);
		float sliderXPos = (float) (posX + position * scale - min * scale);

		glBegin(GL_QUADS);

		glTexCoord2f(sliderPixelWidth * Map.TILE_TEXTURE_SIZE + sliderWidth / Map.TEXTURE_SIZE, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + sliderTextureHeight);
		glVertex3f(sliderXPos + sliderWidth * 2f, (posY + sizeY), -1f);

		glTexCoord2f(sliderPixelWidth * Map.TILE_TEXTURE_SIZE + sliderWidth / Map.TEXTURE_SIZE, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(sliderXPos + sliderWidth * 2f, posY, -1f);

		glTexCoord2f(sliderPixelWidth * Map.TILE_TEXTURE_SIZE, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(sliderXPos, posY, -1f);

		glTexCoord2f(sliderPixelWidth * Map.TILE_TEXTURE_SIZE, Map.TILE_TEXTURE_SIZE * TEX_OFFSET + sliderTextureHeight);
		glVertex3f(sliderXPos, (posY + sizeY), -1f);

		glEnd();

	}

	@Override
	public void click(int which) {
		if (which == 0)
			dragging = true;
	}

	@Override
	public void update() {
		if (dragging && !InputHandler.isMouseButtonDown(0))
			dragging = false;

		if (dragging) {
			position = ((Mouse.getX() - getPos().getX() - sliderWidth / 2) / (getSize().getX() - sliderWidth) * (max - min) + min);
			if (!allowFloat)
				position = (int) (position + 0.5);
			if (position > max)
				position = max;
			if (position < min)
				position = min;
		}
		if (allowFloat) {
			text = String.valueOf(Math.round(position * 100f) / 100f);
		} else {
			text = String.valueOf((int) position);
		}
	}

	public String getText() {
		return text;
	}

	public float getPosition() {
		return position;
	}

}
