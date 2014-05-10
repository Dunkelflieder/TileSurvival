package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityExplosion extends Entity {

	private boolean dealtDamage;
	private final float MAX_LIFFETIME = 0.1f;
	private float lifeTime;
	private float radius;

	public EntityExplosion(Map map, float posX, float posY, float radius, int damage) {
		super(map, posX, posY, 0);
		lifeTime = MAX_LIFFETIME;
		this.radius = radius;

		this.health = damage;
		textureID = 16 * 15 + 1;
		width = radius * 2f;
		height = radius * 2f;
		this.posX -= radius;
		this.posY -= radius;

		light = new Light(0, 0, 20f, 5f);
	}

	public boolean intersects(Vector entityPos) {
		return getCenter().subtract(entityPos).getValue() <= radius;
	}

	@Override
	public void update(float time) {
		light.size = 20f * (lifeTime / MAX_LIFFETIME);

		if (!dealtDamage) {
			for (Entity entity : map.getEntities()) {
				if (intersects(entity.getCenter())) {
					entity.damage(health);
				}
			}

			dealtDamage = true;
		}

		lifeTime -= time;
		if (lifeTime < 0) {
			remove();
		}
	}
}
