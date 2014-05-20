package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemyBigSkeleton extends EntityEnemy {

	private ArrayList<Position> path;
	private int pathProgress;

	public EnemyBigSkeleton(Map map, Vector pos) {
		super(map, pos, new Vector(1.5f), 100, 1f);
		moveSpeed = 1.0f;
		textureID = 16 * 4;
	}

	@Override
	public void recalcPath() {
		path = Pathfinder.getPath(map, getCenter().toPosition(), target.getCenter().toPosition());
		if (path == null) {
			target = map.getRandomPlayer();
			path = Pathfinder.getPath(map, getCenter().toPosition(), target.getCenter().toPosition());
		}
		pathProgress = -1;
	}

	@Override
	public void update(float time) {
		super.update(time);

		if (nextRandomUpdate < 0f) {
			target = map.getNearestPlayer(getCenter());
			if (target == null) return;
			recalcPath();

			nextRandomUpdate = (float) (Math.random() * 10.0);

			if (target.pos.subtracted(pos).getValue() < 5f) path = null;
		}

		if (path != null && pathProgress < path.size() - 1) {
			Vector dir = path.get(pathProgress + 1).toVector().subtracted(pos);
			float targetDist = dir.getValue();
			dir.setValue(moveSpeed * time);

			if (targetDist < dir.getValue()) {
				pathProgress++;
			}

			move(dir.multiplied(speedmult));
		} else if (path == null && target != null) {
			Vector dir = target.pos.subtracted(pos);
			dir.setValue(moveSpeed * time);

			move(dir.multiplied(speedmult));
		}

		if (intersects(target)) {
			damageEntity(target, 15);
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
