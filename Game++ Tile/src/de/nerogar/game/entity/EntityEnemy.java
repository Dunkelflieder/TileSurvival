package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

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
}
