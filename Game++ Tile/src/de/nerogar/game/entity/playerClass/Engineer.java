package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Engineer extends PlayerClass {

	public Engineer(EntityPlayer player) {
		super(player, 100, 50, 3.0f);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new GuardTower(player, 5, 3.0f);
		weaponUpgrades[0][1] = new GuardTower(player, 5, 3.0f);
		weaponUpgrades[0][2] = new GuardTower(player, 5, 3.0f);

		weaponUpgrades[1][0] = new TeamRestore(player, 5, 3.0f);
		weaponUpgrades[1][1] = new TeamRestore(player, 5, 3.0f);
		weaponUpgrades[1][2] = new TeamRestore(player, 5, 3.0f);

		weaponUpgrades[2][0] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][1] = new SlowDownArea(player, 5, 3.0f);
		weaponUpgrades[2][2] = new SlowDownArea(player, 5, 3.0f);
	}
}
