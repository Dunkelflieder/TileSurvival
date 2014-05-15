package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Wall extends Weapon {

	public Wall(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 8 * 0 + 0;
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

	@Override
	public void processEffect(Entity target) {
		// TODO Auto-generated method stub
	}

}
