package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;

public class TeamRestore extends Weapon {

	public TeamRestore(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 5);
		textureID = 8 * 1 + 2;
	}

	@Override
	public void start(Vector target) {
		EntityTeamRestore teamRestoreEntity = new EntityTeamRestore(owner, owner.map, target, damage);
		owner.map.spawnEntity(teamRestoreEntity);
	}

	@Override
	public boolean canActivate() {
		return true;
	}

}
