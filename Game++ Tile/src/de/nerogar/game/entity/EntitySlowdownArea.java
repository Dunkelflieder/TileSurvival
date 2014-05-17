package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntitySlowdownArea extends EntityWeapon {

	private final float MAX_LIFFETIME = 0.1f;
	private float lifeTime;
	private float radius;

	private Entity sender;

	public EntitySlowdownArea(Map map, Vector pos) {
		super(map, pos);
		init();
	}

	public EntitySlowdownArea(Entity sender, Map map, Vector pos, float radius, int damage) {
		super(sender, map, pos, new Vector(radius * 2f), damage);
		resistDamage = true;

		lifeTime = MAX_LIFFETIME;
		this.radius = radius;
		this.sender = sender;

		this.health = damage;
		pos.addX(-radius).addY(-radius);

		for (Entity entity : map.getEntities()) {
			if (intersects(entity.getCenter()) && entity.faction != this.sender.faction) {
				entity.speedmult = 0.1f;
				entity.speedmultTime = 6f;
			}
		}

		init();
	}

	private void init() {
		textureID = 16 * 15 + 1;
	}

	public boolean intersects(Vector entityPos) {
		return getCenter().subtract(entityPos).getValue() <= radius;
	}

	@Override
	public void update(float time) {
		super.update(time);

		lifeTime -= time;
		if (lifeTime < 0) {
			kill();
		}
	}

	@Override
	public void onDie() {

	}
}
