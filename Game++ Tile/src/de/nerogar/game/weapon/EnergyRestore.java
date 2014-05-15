package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class EnergyRestore extends Weapon {

	public float MAX_RESTORE_DISTANCE = 5.0f;

	public EnergyRestore(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 20);
		textureID = 8 * 3 + 0;
	}

	@Override
	public void start(Vector target) {
		for (Entity entity : owner.map.getEntities()) {
			if (entity.faction == owner.faction && !(entity instanceof EntityWeapon)) {
				float entityDist = owner.getCenter().subtract(entity.getCenter()).getValue();
				if (entityDist <= MAX_RESTORE_DISTANCE) {
					if (entity.energy < entity.maxEnergy) entity.energy += damage;

					if (entity.energy > entity.maxEnergy) entity.energy = entity.maxEnergy;
				}
			}
		}
		owner.health = Math.min(owner.health + damage, owner.maxHealth);
	}

	@Override
	public boolean canActivate() {
		return owner.health != owner.maxEnergy;
	}

}
