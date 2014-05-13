package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Speer extends Weapon {

	public Speer(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 4;
	}

	@Override
	public void start(Vector target) {
		EntitySpeer speer = new EntitySpeer(owner, owner.map, owner.pos.clone(), target, damage);
		owner.map.spawnEntity(speer);
	}

	@Override
	public boolean canActivate() {
		return true;
	}
}
