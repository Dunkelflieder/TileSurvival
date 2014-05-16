package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.RenderHelper;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.Entity;
import de.nerogar.game.graphics.TextureBank;

public class GEImage extends GuiElement {

	private Vector texOffset;
	private Vector texSize;
	private String texName;

	protected GEImage(Vector pos, Vector size, String texName, Vector texOffset, Vector texSize) {
		super(pos, size, true);
		this.texName = texName;
		this.texOffset = texOffset;
		this.texSize = texSize;
	}

	@Override
	public void render() {
		TextureBank.instance.bindTexture(texName);
		RenderHelper.enableAlphaMask();

		glBegin(GL_QUADS);

		glTexCoord2f(Entity.tileTextureSize * texOffset.getX(), Entity.tileTextureSize * texOffset.getY());
		glVertex3f(getPos().getX(), getPos().getY(), -1f);

		glTexCoord2f(Entity.tileTextureSize * (texOffset.getX() + texSize.getX()), Entity.tileTextureSize * texOffset.getY());
		glVertex3f(getPos().getX() + getSize().getX(), getPos().getY(), -1f);

		glTexCoord2f(Entity.tileTextureSize * (texOffset.getX() + texSize.getX()), Entity.tileTextureSize * (texOffset.getY() + texSize.getY()));
		glVertex3f(getPos().getX() + getSize().getX(), getPos().getY() + getSize().getY(), -1f);

		glTexCoord2f(Entity.tileTextureSize * texOffset.getX(), Entity.tileTextureSize * (texOffset.getY() + texSize.getY()));
		glVertex3f(getPos().getX(), getPos().getY() + getSize().getY(), -1f);

		glEnd();
	}

}
