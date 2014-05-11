package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Fireball extends Weapon {

	public Fireball(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 0;
	}

	@Override
	public void start(Vector target) {
		EntityFireball fireball = new EntityFireball(owner, owner.map, owner.pos.clone(), target, damage);
		owner.map.spawnEntity(fireball);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
