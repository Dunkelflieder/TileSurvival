package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class GuardTower extends Weapon {

	public GuardTower(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 8 * 1 + 0;
	}

	@Override
	public void start(Vector target) {
		EntityGuardTower guardTowerEntity = new EntityGuardTower(owner, owner.map, target, damage);
		owner.map.spawnEntity(guardTowerEntity);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
