package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntitySlowdownArea extends Entity {

	private final float MAX_LIFFETIME = 0.1f;
	private float lifeTime;
	private float radius;

	private Entity sender;

	public EntitySlowdownArea(Entity sender, Map map, float posX, float posY, float radius, int damage) {
		super(map, posX, posY, 0);
		lifeTime = MAX_LIFFETIME;
		this.radius = radius;
		this.sender = sender;

		this.health = damage;
		textureID = 16 * 15 + 1;
		width = radius * 2f;
		height = radius * 2f;
		this.posX -= radius;
		this.posY -= radius;

		for (Entity entity : map.getEntities()) {
			if (intersects(entity.getCenter()) && entity != this.sender) {
				entity.speedmult = 0.1f;
				entity.speedmultTime = 6f;
			}
		}
	}

	public boolean intersects(Vector entityPos) {
		return getCenter().subtract(entityPos).getValue() <= radius;
	}

	@Override
	public void update(float time) {
		lifeTime -= time;
		if (lifeTime < 0) {
			kill();
		}
	}

	@Override
	public void onDie() {

	}
}
