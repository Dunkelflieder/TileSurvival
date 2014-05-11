package de.nerogar.game.weapon;

import de.nerogar.game.entity.*;

public class FireBlast extends Weapon {

	public FireBlast(EntityPlayer player, int damage, float cooldown) {
		super(player, damage, cooldown);
		textureID = 1;
	}

	@Override
	public void start(float targetX, float targetY) {
		EntityExplosion explosion = new EntityExplosion(player, player.map, player.posX, player.posY, 5, damage);
		player.map.spawnEntity(explosion);
	}

	@Override
	public int getEnergyCost() {
		return 20;
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
