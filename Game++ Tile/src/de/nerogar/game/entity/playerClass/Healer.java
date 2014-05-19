package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Healer extends PlayerClass {

	public Healer(EntityPlayer player) {
		super(player, 100, 100, 3.0f, 4);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new Heal(player, 20, 3.0f);
		weaponUpgrades[0][1] = new Heal(player, 40, 3.0f);
		weaponUpgrades[0][2] = new Heal(player, 60, 3.0f);

		weaponUpgrades[1][0] = new EnergyRestore(player, 15, 3.0f);
		weaponUpgrades[1][1] = new EnergyRestore(player, 25, 3.0f);
		weaponUpgrades[1][2] = new EnergyRestore(player, 35, 2.0f);

		weaponUpgrades[2][0] = new Speed(player, 5, 5.0f);
		weaponUpgrades[2][1] = new Speed(player, 10, 10.0f);
		weaponUpgrades[2][2] = new Speed(player, 15, 15.0f);
	}
}
