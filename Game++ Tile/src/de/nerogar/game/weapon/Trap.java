package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Trap extends Weapon {

	public Trap(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 30);
		textureID = 8 * 2 + 2;
	}

	@Override
	public void start(Vector target) {
		EntityTrap trapEntity = new EntityTrap(owner, owner.map, target, damage);
		owner.map.spawnEntity(trapEntity);
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
