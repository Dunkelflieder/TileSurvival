package de.nerogar.game.weapon;

import de.nerogar.game.entity.EntityPlayer;

public abstract class Weapon {

	public EntityPlayer player;
	public int damage;
	public float maxCooldown;
	public float cooldown;

	public int textureID;

	public Weapon(EntityPlayer player, int damage, float cooldown) {
		this.player = player;
		this.damage = damage;
		this.cooldown = cooldown;
	}

	public abstract void start(float targetX, float targetY);

	public void update(float time) {
		cooldown -= time;
	}

}
