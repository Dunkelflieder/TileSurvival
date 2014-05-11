package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class Heal extends Weapon {

	public Heal(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 20);
		textureID = 3;
	}

	@Override
	public void start(Vector target) {
		owner.health = Math.min(owner.health + damage, owner.maxHealth);
	}

	@Override
	public boolean canActivate() {
		return owner.health != owner.maxEnergy;
	}

}
