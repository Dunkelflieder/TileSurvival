package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.*;

import de.nerogar.game.Map;
import de.nerogar.game.RenderHelper;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.TextureBank;

public class GEButton extends GuiElement {

	private String text;

	private static final float TEX_OFFSET = 6f;

	protected GEButton(Vector pos, Vector size, String text) {
		super(pos, size, false);
		this.text = text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void render() {
		TextureBank.instance.bindTexture("gui.png");
		RenderHelper.enableAlphaMask();

		glBegin(GL_QUADS);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(getPos().getX(), getPos().getY(), -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * TEX_OFFSET);
		glVertex3f(getPos().getX() + getSize().getX(), getPos().getY(), -1f);

		glTexCoord2f(Map.TILE_TEXTURE_SIZE * 4f, Map.TILE_TEXTURE_SIZE * (TEX_OFFSET + 1));
		glVertex3f(getPos().getX() + getSize().getX(), getPos().getY() + getSize().getY(), -1f);

		glTexCoord2f(0, Map.TILE_TEXTURE_SIZE * (TEX_OFFSET + 1));
		glVertex3f(getPos().getX(), getPos().getY() + getSize().getY(), -1f);

		glEnd();

		FontRenderer.drawString(text, getPos().getX() + 16, getPos().getY() + 16, getSize().getX() - 32, getSize().getY() - 32, FontRenderer.CENTERED);

		if (isDisabled()) {

			glColor4f(0, 0, 0, 0.7f);
			glDisable(GL_TEXTURE_2D);
			glBegin(GL_QUADS);
			glVertex3f(getPos().getX(), getPos().getY(), -1f);
			glVertex3f(getPos().getX() + getSize().getX(), getPos().getY(), -1f);
			glVertex3f(getPos().getX() + getSize().getX(), getPos().getY() + getSize().getY(), -1f);
			glVertex3f(getPos().getX(), getPos().getY() + getSize().getY(), -1f);
			glEnd();
			glEnable(GL_TEXTURE_2D);
			glColor3f(1, 1, 1);

		}

	}

}
