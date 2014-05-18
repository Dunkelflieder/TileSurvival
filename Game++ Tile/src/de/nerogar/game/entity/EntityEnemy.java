package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.RenderHelper;
import de.nerogar.game.Vector;
import static org.lwjgl.opengl.GL11.*;

public abstract class EntityEnemy extends Entity {

	public float damageCooldown;
	public float maxDamageCooldown;

	public EntityEnemy(Map map, Vector pos, Vector dimension, int health, float damageCooldown) {
		super(map, pos, dimension, health);
		this.maxDamageCooldown = damageCooldown;
		faction = FACTION_MOB;
	}

	public void damageEntity(Entity target, int damage) {
		if (damageCooldown < 0) {
			damageCooldown = maxDamageCooldown;
			target.damage(damage);
		}
	}

	@Override
	public void update(float time) {
		super.update(time);
		damageCooldown -= time;
	}

	@Override
	public void renderAfterShader() {

		float healthPercent = (float) health / maxHealth;
		glColor3f(0, 0.1f, 0.1f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(0.5f, 0, 0);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX() * healthPercent) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(1, 1, 1);

	}
}
