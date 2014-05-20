package de.nerogar.game.weapon;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import de.nerogar.game.*;
import de.nerogar.game.entity.Entity;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.graphics.TextureBank;

public abstract class Weapon {

	public Entity owner;
	public int damage;
	public float maxCooldown;
	public float cooldown;
	public int energyCost;

	public int textureID;

	public static float renderSize = Map.TILE_RENDER_SIZE;

	public Weapon(Entity owner, int damage, float cooldown, int energyCost) {
		this.owner = owner;
		this.damage = damage;
		this.maxCooldown = cooldown;
		this.energyCost = energyCost;
	}

	public abstract void start(Vector target);

	public abstract boolean canActivate();

	public void update(float time) {
		cooldown -= time;
	}

	public abstract void processEffect(Entity target);

	public void render(float posX, float posY) {
		RenderHelper.enableAlphaMask();
		TextureBank.instance.bindTexture("gui.png");

		EntityPlayer player = owner.map.getPlayer();

		int tilePosX = textureID % (int) Map.TILES_ON_TEXTURE;
		int tilePosY = textureID / (int) Map.TILES_ON_TEXTURE;

		float cooldown = (this.cooldown < 0) ? 1 : 1 - this.cooldown / this.maxCooldown;

		glBegin(GL_QUADS);

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX, posY, -1f);
		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX + renderSize, posY, -1f);
		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX + renderSize, posY + renderSize, -1f);
		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX, posY + renderSize, -1f);

		tilePosX = (int) Map.TILES_ON_TEXTURE - 1;
		tilePosY = (int) Map.TILES_ON_TEXTURE - 1;
		if (player.getSelectedWeapon() == this) tilePosX--;

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX, posY, -1f);
		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX + renderSize, posY, -1f);
		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX + renderSize, posY + renderSize, -1f);
		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f(posX, posY + renderSize, -1f);

		glEnd();

		// cooldown
		glDisable(GL11.GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		glColor4f(0f, 0f, 0f, 0.7f);
		glVertex3f(posX, posY + renderSize * cooldown, -1);
		glVertex3f(posX + renderSize, posY + renderSize * cooldown, -1);
		glVertex3f(posX + renderSize, posY + renderSize, -1);
		glVertex3f(posX, posY + renderSize, -1);
		glEnd();

		glColor3f(1.0f, 1.0f, 1.0f);

		glEnable(GL11.GL_TEXTURE_2D);

	}

}
