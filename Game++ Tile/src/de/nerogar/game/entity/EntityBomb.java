package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;

public class EntityBomb extends EntityWeapon {

	private final float MAX_LIFETIME = 3f;
	private float lifetime;

	public EntityBomb(Map map, Vector pos) {
		super(map, pos);
		init();
	}

	public EntityBomb(Entity sender, Map map, Vector pos, int damage) {
		super(sender, map, pos, new Vector(1.0f), damage);
		resistDamage = true;
		lifetime = MAX_LIFETIME;
		init();
	}

	private void init() {
		textureID = 16 * 15 + 3;

		light = new Light(pos, 3f, 0.5f);
	}

	@Override
	public void update(float time) {
		super.update(time);
		lifetime -= time;
		if (lifetime <= 0f) kill();

	}

	@Override
	public void onDie() {
		map.spawnEntity(new EntityExplosion(sender, map, pos, 5, health));
	}
}
