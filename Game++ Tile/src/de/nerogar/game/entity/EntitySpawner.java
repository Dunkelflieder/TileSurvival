package de.nerogar.game.entity;

import java.util.HashMap;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.enemy.*;

public class EntitySpawner {

	private static HashMap<Class<? extends Entity>, Integer> networkIDs;

	public static Entity spawnEntity(Map map, Vector pos, int spawnID) {
		switch (spawnID) {
		case 0:
			return new EntityPlayer(map, pos);

			//weapon
		case 101:
			return new EntityFireball(map, pos);
		case 102:
			return new EntityExplosion(map, pos);
		case 103:
			return new EntitySlowdownArea(map, pos);
		case 104:
			return new EntitySpear(map, pos);
		case 105:
			return new EntityGuardTower(map, pos);
		case 106:
			return new EntityIceSpike(map, pos);
		case 107:
			return new EntityTeamRestore(map, pos);
		case 108:
			return new EntityTrap(map, pos);

			//enemy
		case 201:
			return new EntitySmallEnemy(map, pos);
		case 202:
			return new EnemyGhost(map, pos);
		case 203:
			return new EnemyDump(map, pos);
		case 204:
			return new EnemySkeleton(map, pos);
		case 205:
			return new EnemyRat(map, pos);
		case 206:
			return new EnemyBigSkeleton(map, pos);

			//
		case 301:
			return new EntityEnergyDrop(map, pos);

		}
		return null;
	}

	public static int getSpawnID(Entity entity) {
		return networkIDs.get(entity.getClass());
	}

	static {
		networkIDs = new HashMap<Class<? extends Entity>, Integer>();

		//player
		networkIDs.put(EntityPlayer.class, 0);

		//weapon
		networkIDs.put(EntityFireball.class, 101);
		networkIDs.put(EntityExplosion.class, 102);
		networkIDs.put(EntitySlowdownArea.class, 103);
		networkIDs.put(EntitySpear.class, 104);
		networkIDs.put(EntityGuardTower.class, 105);
		networkIDs.put(EntityIceSpike.class, 106);
		networkIDs.put(EntityTeamRestore.class, 107);
		networkIDs.put(EntityTrap.class, 108);

		//enemy
		networkIDs.put(EntitySmallEnemy.class, 201);
		networkIDs.put(EnemyGhost.class, 202);
		networkIDs.put(EnemyDump.class, 203);
		networkIDs.put(EnemySkeleton.class, 204);
		networkIDs.put(EnemyRat.class, 205);
		networkIDs.put(EnemyBigSkeleton.class, 206);

		//
		networkIDs.put(EntityEnergyDrop.class, 301);
	}
}
