package de.nerogar.game;

import java.util.ArrayList;

import de.nerogar.game.entity.Entity;
import de.nerogar.game.entity.enemy.*;

public class Wave {
	public int wave;
	private Map map;

	public Wave(Map map) {
		this.map = map;
		wave = 0;
	}

	public void update(float time) {
		if (waveComplete(map.getEntities())) {
			wave++;
			spawnEnemies();
			System.out.println("wave " + wave + " started.");
		}
	}

	private boolean waveComplete(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			if (entity.faction == Entity.FACTION_MOB) return false;
		}
		return true;
	}

	private Vector getRandomSpawnLocation() {
		return map.getEnemySpawnLocations()[(int) (Math.random() * map.getEnemySpawnLocations().length)].clone();
	}

	private void spawnEnemies() {

		for (int i = 0; i < 10; i++) {
			map.spawnEntity(new EnemyNekro(map, getRandomSpawnLocation()));
		}

		for (int i = 0; i < 10; i++) {
			map.spawnEntity(new EnemyBigSkeleton(map, getRandomSpawnLocation()));
		}

		for (int i = 0; i < 10; i++) {
			map.spawnEntity(new EnemyGhost(map, getRandomSpawnLocation()));
		}
	}
}
