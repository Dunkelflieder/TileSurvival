package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class TeamRestore extends Weapon {

	public TeamRestore(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 40);
		textureID = 8 * 1 + 1;
	}

	@Override
	public void start(Vector target) {
		EntityTeamRestore teamRestoreEntity = new EntityTeamRestore(owner, owner.map, target, damage);
		owner.map.spawnEntity(teamRestoreEntity);
	}

	@Override
	public boolean canActivate() {
		int existingCount = 0;
		for (Entity entity : owner.map.getEntities()) {
			if (entity instanceof EntityTeamRestore && ((EntityWeapon) entity).sender == owner){
				existingCount++;
			}
		}

		return existingCount < 5;
	}

	@Override
	public void processEffect(Entity target) {
		target.health = Math.min(target.health + damage, target.maxHealth);
		target.energy = Math.min(target.energy + damage, target.maxEnergy);
	}

}
