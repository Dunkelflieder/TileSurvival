package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class FireBlast extends Weapon {

	public FireBlast(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 20);
		textureID = 8 * 0 + 6;
	}

	@Override
	public void start(Vector target) {
		EntityExplosion explosion = new EntityExplosion(owner, owner.map, owner.getCenter(), 5, damage);
		owner.map.spawnEntity(explosion);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
