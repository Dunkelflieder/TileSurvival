package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Bomb extends Weapon {
	
	public Bomb(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 20);
		textureID = 8 * 1 + 2;
	}

	@Override
	public void start(Vector target) {
		EntityBomb bombEntity = new EntityBomb(owner, owner.map, target, damage);
		owner.map.spawnEntity(bombEntity);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

	@Override
	public void processEffect(Entity target) {
		//no effect on entities
	}

}
