package de.nerogar.game.weapon;

import de.nerogar.game.entity.*;

public class Heal extends Weapon {

	public Heal(EntityPlayer player, int damage, float cooldown) {
		super(player, damage, cooldown);
		textureID = 3;
	}

	@Override
	public void start(float targetX, float targetY) {
		player.health = Math.min(player.health + damage, player.maxHealth);
	}

	@Override
	public int getEnergyCost() {
		return 20;
	}

	@Override
	public boolean canActivate() {
		return player.health != player.maxEnergy;
	}

}
