package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class SlowDownArea extends Weapon {

	public SlowDownArea(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 15);
		textureID = 2;
	}

	@Override
	public void start(Vector target) {
		EntitySlowdownArea slowDownEntity = new EntitySlowdownArea(owner, owner.map, owner.pos.clone(), 5, damage);
		owner.map.spawnEntity(slowDownEntity);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
