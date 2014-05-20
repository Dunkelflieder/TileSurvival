package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Mage extends PlayerClass {

	public Mage(EntityPlayer player) {
		super(player, 100, 150, 3.0f, 8);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new Fireball(player, 5, 0.5f);
		weaponUpgrades[0][1] = new Fireball(player, 15, 0.4f);
		weaponUpgrades[0][2] = new Fireball(player, 25, 0.3f);

		weaponUpgrades[1][0] = new IceSpikes(player, 2, 1.0f);
		weaponUpgrades[1][1] = new IceSpikes(player, 7, 0.9f);
		weaponUpgrades[1][2] = new IceSpikes(player, 15, 0.8f);

		weaponUpgrades[2][0] = new Trap(player, 5, 3.0f);
		weaponUpgrades[2][1] = new Trap(player, 15, 3.0f);
		weaponUpgrades[2][2] = new Trap(player, 25, 3.0f);
	}
}
