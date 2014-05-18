package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemySkeleton extends EntityEnemy {

	private Entity targetPlayer;
	private ArrayList<Position> path;
	private int pathProgress;

	private float nextRandomUpdate = 0f;

	public EnemySkeleton(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 20, 1f);
		moveSpeed = 2.0f;
		textureID = 16;
	}

	@Override
	public void update(float time) {
		super.update(time);
		nextRandomUpdate -= time;

		if (nextRandomUpdate < 0f) {
			targetPlayer = map.getNearestPlayer(getCenter());
			if (targetPlayer == null) return;
			path = Pathfinder.getPath(map, getCenter().toPosition(), targetPlayer.getCenter().toPosition());
			if (path == null) {
				targetPlayer = map.getRandomPlayer();
				path = Pathfinder.getPath(map, getCenter().toPosition(), targetPlayer.getCenter().toPosition());
			}
			pathProgress = -1;
			nextRandomUpdate = (float) (Math.random() * 10.0);
		}

		if (path != null && pathProgress < path.size() - 1) {
			Vector dir = path.get(pathProgress + 1).toVector().subtracted(pos);
			float targetDist = dir.getValue();
			dir.setValue(moveSpeed * time);

			if (targetDist < dir.getValue()) {
				pathProgress++;
			}

			move(dir.multiplied(speedmult));
		}

		if (intersects(targetPlayer)) {
			damageEntity(targetPlayer, 3);
		}
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}
