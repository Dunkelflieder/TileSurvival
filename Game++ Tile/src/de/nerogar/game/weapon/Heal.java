package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Heal extends Weapon {

	public float MAX_RESTORE_DISTANCE = 5.0f;

	public Heal(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 40);
		textureID = 8 * 3 + 0;
	}

	@Override
	public void start(Vector target) {
		for (Entity entity : owner.map.getEntities()) {
			if (entity.faction == owner.faction && !(entity instanceof EntityWeapon)) {
				float entityDist = owner.getCenter().subtract(entity.getCenter()).getValue();
				if (entityDist <= MAX_RESTORE_DISTANCE) {
					if (entity.health < entity.maxHealth) entity.health += damage;

					if (entity.health > entity.maxHealth) entity.health = entity.maxHealth;
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
		target.health = Math.min(target.health + damage, target.maxHealth);
	}

}
