package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.Weapon;

public abstract class PlayerClass {

	protected EntityPlayer player;
	protected Weapon[][] weaponUpgrades;
	public Weapon[] weapons;
	public int[] weaponUses;
	public int selectedWeapon;
	public int maxHealth;
	public int maxEnergy;

	public static final int ENGINEER = 1;
	public static final int HEALER = 2;
	public static final int MAGE = 3;
	public static final int WARRIOR = 4;

	public PlayerClass(EntityPlayer player, int maxHealth, int maxEnergy, float moveSpeed, int textureID) {
		weapons = new Weapon[3];
		this.player = player;
		this.maxHealth = maxHealth;
		player.energy = maxEnergy;
		player.maxEnergy = maxEnergy;
		this.maxEnergy = maxEnergy;
		player.health = maxHealth;
		player.maxHealth = maxHealth;
		player.moveSpeed = moveSpeed;
		this.weaponUpgrades = new Weapon[3][3];//3 weapons, 3 level
		weaponUses = new int[3];
		player.textureID = textureID;
		initWeaponsUpgrades();

		setWeaponLevel(0, 0);
		setWeaponLevel(1, 0);
		setWeaponLevel(2, 0);
	}

	protected abstract void initWeaponsUpgrades();

	public void setWeaponLevel(int weaponID, int level) {
		weapons[weaponID] = weaponUpgrades[weaponID][level];
		weapons[weaponID].level = level;
	}

	public Weapon getSelectedWeapon() {
		return weapons[selectedWeapon];
	}

	public void selectWeapon(int selectedWeapon) {
		this.selectedWeapon = selectedWeapon;
	}

	public void selectNextWeapon() {
		selectedWeapon = (selectedWeapon + 1) % weaponUpgrades.length;
	}

	public void updateWeapons(float time) {
		for (Weapon weapon : weapons) {
			weapon.update(time);
		}
	}

	public void activateWeaponDebugTimes() {
		for (Weapon weapon : weapons) {
			weapon.maxCooldown = 0.2f;
			weapon.energyCost = 0;
		}
	}

	public void setCurrentWeaponUsed() {
		weaponUses[selectedWeapon]++;
		if (weaponUses[selectedWeapon] > 0) setWeaponLevel(selectedWeapon, 0);
		if (weaponUses[selectedWeapon] > 10) setWeaponLevel(selectedWeapon, 1);
		if (weaponUses[selectedWeapon] > 50) setWeaponLevel(selectedWeapon, 2);
	}

	public static PlayerClass getInstanceByID(int id, EntityPlayer player) {
		switch (id) {
		case ENGINEER:
			return new Engineer(player);
		case HEALER:
			return new Healer(player);
		case MAGE:
			return new Mage(player);
		case WARRIOR:
			return new Warrior(player);
		}
		return null;
	}

}
