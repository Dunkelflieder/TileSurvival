package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Warrior extends PlayerClass {

	public Warrior(EntityPlayer player) {
		super(player, 100, 100, 3.0f, 12);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new Trap(player, 5, 3.0f);
		weaponUpgrades[0][1] = new Trap(player, 5, 3.0f);
		weaponUpgrades[0][2] = new Trap(player, 5, 3.0f);

		weaponUpgrades[1][0] = new Spear(player, 5, 3.0f);
		weaponUpgrades[1][1] = new Spear(player, 5, 3.0f);
		weaponUpgrades[1][2] = new Spear(player, 5, 3.0f);

		weaponUpgrades[2][0] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][1] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][2] = new SlowDownArea(player, 5, 3.0f);
	}
}
