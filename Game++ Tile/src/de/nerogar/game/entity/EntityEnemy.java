package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public abstract class EntityEnemy extends Entity {

	public float damageCooldown;
	public float maxDamageCooldown;

	public EntityEnemy(Map map, float posX, float posY, int health, float damageCooldown) {
		super(map, posX, posY, health);
		this.maxDamageCooldown = damageCooldown;
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
	public void onDie() {
		Vector center = getCenter();
		map.spawnEntity(new EntityEnergyDrop(map, center.getX(), center.getY()));
	}
}
