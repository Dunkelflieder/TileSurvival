package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityExplosion extends Entity {

	private boolean dealtDamage;
	private final float MAX_LIFFETIME = 0.1f;
	private float lifeTime;
	private float radius;

	private Entity sender;

	public EntityExplosion(Map map, Vector pos) {
		super(map, pos, new Vector(0.1f), 0);
	}

	public EntityExplosion(Entity sender, Map map, Vector pos, float radius, int damage) {
		super(map, pos, new Vector(radius * 2f), damage);
		resistDamage = true;
		lifeTime = MAX_LIFFETIME;
		this.radius = radius;
		this.sender = sender;

		textureID = 16 * 15 + 1;
		pos.addX(-radius).addY(-radius);

		light = new Light(new Vector(), 20f, 5f);
	}

	public boolean intersects(Vector entityPos) {
		return getCenter().subtract(entityPos).getValue() <= radius;
	}

	@Override
	public void update(float time) {
		light.size = 20f * (lifeTime / MAX_LIFFETIME);

		if (!dealtDamage) {
			for (Entity entity : map.getEntities()) {
				if (entity != sender && intersects(entity.getCenter())) {
					entity.damage(health);
				}
			}

			dealtDamage = true;
		}

		lifeTime -= time;
		if (lifeTime < 0) {
			kill();
		}
	}

	@Override
	public void onDie() {

	}
}
