package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Taunt extends Weapon {

	public float MAX_RESTORE_DISTANCE = 10.0f;

	public Taunt(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 30);
		textureID = 8 * 0 + 2;
	}

	@Override
	public void start(Vector target) {
		for (Entity entity : owner.map.getEntities()) {
			if (!(entity instanceof EntityWeapon)) {
				float entityDist = owner.getCenter().subtract(entity.getCenter()).getValue();
				if (entityDist <= MAX_RESTORE_DISTANCE) {
					if (entity instanceof EntityEnemy) {
						EntityEnemy enemy = (EntityEnemy) entity;
						enemy.target = owner;
						enemy.nextRandomUpdate = damage;
						enemy.recalcPath();
					}
				}
			}
		}
		owner.health = Math.min(owner.health + damage, owner.maxHealth);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

	@Override
	public void processEffect(Entity target) {

	}

}
