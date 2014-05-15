package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class IceSpikes extends Weapon {

	public IceSpikes(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 8 * 2 + 1;
	}

	@Override
	public void start(Vector target) {
		for (int i = 0; i < 4; i++) {
			Vector randPos = new Vector((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).multiply(3f);
			EntityIceSpike iceSpike = new EntityIceSpike(owner, owner.map, owner.pos.clone(), target.added(randPos), damage);
			owner.map.spawnEntity(iceSpike);
		}
	}

	@Override
	public boolean canActivate() {
		return true;
	}

	@Override
	public void processEffect(Entity target) {
		target.speedmult = 0.3f;
		target.speedmultTime = 4f;
	}

}
