package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemyBigSkeleton extends EntityEnemy {

	private Entity targetPlayer;
	private ArrayList<Position> path;
	private int pathProgress;

	private float nextRandomUpdate = 0f;

	public EnemyBigSkeleton(Map map, Vector pos) {
		super(map, pos, new Vector(1.5f), 100, 1f);
		moveSpeed = 1.0f;
		textureID = 16 * 4;
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

			if (targetPlayer.pos.subtracted(pos).getValue() < 5f) path = null;
		}

		if (path != null && pathProgress < path.size() - 1) {
			Vector dir = path.get(pathProgress + 1).toVector().subtracted(pos);
			float targetDist = dir.getValue();
			dir.setValue(moveSpeed * time);

			if (targetDist < dir.getValue()) {
				pathProgress++;
			}

			move(dir.multiplied(speedmult));
		} else if (path == null && targetPlayer != null) {
			Vector dir = targetPlayer.pos.subtracted(pos);
			dir.setValue(moveSpeed * time);

			move(dir.multiplied(speedmult));
		}

		if (intersects(targetPlayer)) {
			damageEntity(targetPlayer, 10);
		}
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}

		for (int i = 0; i < 3; i++) {
			map.spawnEntity(new EnemySkeleton(map, pos.clone()));
		}
	}
}
