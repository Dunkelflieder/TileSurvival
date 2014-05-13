package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.weapon.Fireball;

public class EntityGuardTower extends EntityWeapon {

	private static final float MAX_FIGHT_DISTANCE = 8.0f;

	private Fireball fireballWeapon;

	public EntityGuardTower(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(1f);
		init();
	}

	public EntityGuardTower(Entity sender, Map map, Vector pos, int damage) {
		super(sender, map, pos, new Vector(1f), damage);
		init();
	}

	private void init() {
		textureID = 16 * 14;
		fireballWeapon = new Fireball(this, 5, 1.0f);
	}

	@Override
	public void update(float time) {
		super.update(time);

		Entity target = null;
		float targetDist = 0f;

		for (Entity entity : map.getEntities()) {
			if (!canDamage(entity)) continue;
			float entityDist = 0;
			if (entity != null) {
				entityDist = getCenter().subtract(entity.getCenter()).getSquaredValue();
			}
			if (target == null || entityDist < targetDist) {
				targetDist = entityDist;
				target = entity;
			}
		}

		if (target != null && getCenter().subtract(target.getCenter()).getValue() <= MAX_FIGHT_DISTANCE) {
			if (fireballWeapon.cooldown <= 0f) {
				fireballWeapon.start(target.getCenter());
				fireballWeapon.cooldown = fireballWeapon.maxCooldown;
			}
		}

		fireballWeapon.update(time);
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}
