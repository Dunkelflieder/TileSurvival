package de.nerogar.game.weapon;

import static org.lwjgl.opengl.GL11.*;
import de.nerogar.game.Map;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.graphics.TextureBank;

public abstract class Weapon {

	public EntityPlayer player;
	public int damage;
	public float maxCooldown;
	public float cooldown;

	public int textureID;

	public float posX;
	public float posY;

	public Weapon(EntityPlayer player, int damage, float cooldown) {
		this.player = player;
		this.damage = damage;
		//this.cooldown = cooldown;
		this.maxCooldown = cooldown;
	}

	public abstract void start(float targetX, float targetY);

	public abstract boolean canActivate();

	public void update(float time) {
		cooldown -= time;
	}

	public void render() {
		TextureBank.instance.bindTexture("gui.png");

		int tilePosX = textureID % (int) Map.TILES_ON_TEXTURE;
		int tilePosY = textureID / (int) Map.TILES_ON_TEXTURE;

		float renderSize = 0.5f;

		glBegin(GL_QUADS);

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX) * Map.TILE_RENDER_SIZE, (posY) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX + renderSize) * Map.TILE_RENDER_SIZE, (posY) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX + renderSize) * Map.TILE_RENDER_SIZE, (posY + renderSize) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX) * Map.TILE_RENDER_SIZE, (posY + renderSize) * Map.TILE_RENDER_SIZE, -1f);

		tilePosX = (int) Map.TILES_ON_TEXTURE - 1;
		tilePosY = (int) Map.TILES_ON_TEXTURE - 1;
		if (player.getSelectedWeapon() == this) tilePosX--;

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX) * Map.TILE_RENDER_SIZE, (posY) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, tilePosY * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX + renderSize) * Map.TILE_RENDER_SIZE, (posY) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f((tilePosX + 1) * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX + renderSize) * Map.TILE_RENDER_SIZE, (posY + renderSize) * Map.TILE_RENDER_SIZE, -1f);

		glTexCoord2f(tilePosX * Map.TILE_TEXTURE_SIZE, (tilePosY + 1) * Map.TILE_TEXTURE_SIZE);
		glVertex3f((posX) * Map.TILE_RENDER_SIZE, (posY + renderSize) * Map.TILE_RENDER_SIZE, -1f);

		glEnd();
	}

	public abstract int getEnergyCost();

}
