package de.nerogar.game.weapon;

import de.nerogar.game.entity.EntityFireball;
import de.nerogar.game.entity.EntityPlayer;

public class Fireball extends Weapon {

	public Fireball(EntityPlayer player, int damage, float cooldown) {
		super(player, damage, cooldown);
		textureID = 0;
	}

	@Override
	public void start(float targetX, float targetY) {
		EntityFireball fireball = new EntityFireball(player, player.map, player.posX, player.posY, targetX, targetY, damage);
		player.map.spawnEntity(fireball);
	}

	@Override
	public int getEnergyCost() {
		return 5;
	}

}
