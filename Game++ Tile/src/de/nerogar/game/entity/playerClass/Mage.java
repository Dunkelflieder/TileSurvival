package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Mage extends PlayerClass {

	public Mage(EntityPlayer player) {
		super(player, 100, 100, 3.0f);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new Fireball(player, 5, 3.0f);
		weaponUpgrades[0][1] = new Fireball(player, 5, 3.0f);
		weaponUpgrades[0][2] = new Fireball(player, 5, 3.0f);

		weaponUpgrades[1][0] = new IceSpikes(player, 5, 3.0f);
		weaponUpgrades[1][1] = new IceSpikes(player, 5, 3.0f);
		weaponUpgrades[1][2] = new IceSpikes(player, 5, 3.0f);

		weaponUpgrades[2][0] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][1] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][2] = new SlowDownArea(player, 5, 3.0f);
	}
}
