package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Spear extends Weapon {

	public Spear(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 8 * 0 + 1;
	}

	@Override
	public void start(Vector target) {
		EntitySpear spear = new EntitySpear(owner, owner.map, owner.pos.clone(), target, damage);
		owner.map.spawnEntity(spear);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

	@Override
	public void processEffect(Entity target) {
		target.damage(damage);
	}
}
