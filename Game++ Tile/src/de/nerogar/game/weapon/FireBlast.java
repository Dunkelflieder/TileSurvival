package de.nerogar.game.weapon;

import de.nerogar.game.entity.*;

public class FireBlast extends Weapon {

	public FireBlast(EntityPlayer player, int damage, float cooldown) {
		super(player, damage, cooldown);
		textureID = 1;
	}

	@Override
	public void start(float targetX, float targetY) {
		if (cooldown > 0f) return;
		cooldown = maxCooldown;

		EntityExplosion explosion = new EntityExplosion(player.map, player.posX, player.posY, 5, damage);
		//EntityFireball fireball = new EntityFireball(player.map, player.posX, player.posY, targetX, targetY);
		player.map.spawnEntity(explosion);
	}

}
