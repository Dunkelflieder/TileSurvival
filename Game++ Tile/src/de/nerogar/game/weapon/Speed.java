package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Speed extends Weapon {

	public float MAX_RESTORE_DISTANCE = 15.0f;

	public Speed(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 20);
		textureID = 8 * 3 + 2;
	}

	@Override
	public void start(Vector target) {
		for (Entity entity : owner.map.getEntities()) {
			if (entity.faction == owner.faction && !(entity instanceof EntityWeapon)) {
				float entityDist = owner.getCenter().subtract(entity.getCenter()).getValue();
				if (entityDist <= MAX_RESTORE_DISTANCE) {
					entity.speedmult = 1.8f;
					entity.speedmultTime = damage;
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
