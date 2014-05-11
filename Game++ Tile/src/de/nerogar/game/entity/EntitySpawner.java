package de.nerogar.game.entity;

import java.util.HashMap;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;

public class EntitySpawner {

	private static HashMap<Class<? extends Entity>, Integer> networkIDs;

	public static Entity spawnEntity(Map map, Vector pos, int spawnID) {
		switch (spawnID) {
		case 0:
			return new EntityPlayer(map, pos);
		case 1:
			return new EntityFireball(map, pos);
		case 2:
			return new EntityExplosion(map, pos);
		case 3:
			return new EntitySmallEnemy(map, pos);
		case 4:
			return new EntityGhost(map, pos);
		case 5:
			return new EntityEnergyDrop(map, pos);
		}

		return null;
	}

	public static int getSpawnID(Entity entity) {
		return networkIDs.get(entity.getClass());
	}

	static {
		networkIDs = new HashMap<Class<? extends Entity>, Integer>();

		networkIDs.put(EntityPlayer.class, 0);
		networkIDs.put(EntityFireball.class, 1);
		networkIDs.put(EntityExplosion.class, 2);
		networkIDs.put(EntitySmallEnemy.class, 3);
		networkIDs.put(EntityGhost.class, 4);
		networkIDs.put(EntityEnergyDrop.class, 5);

	}
}
