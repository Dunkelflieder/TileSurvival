package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemyRat extends EntityEnemy {

	private ArrayList<Position> path;
	private int pathProgress;

	public EnemyRat(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 20, 1f);
		moveSpeed = 3.0f;
		textureID = 16;
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

		if (intersects(target)) {
			damageEntity(target, 1);
			if (Math.random() < 0.2) {
				target.poison = 2;
				target.poisonTime = 5f;
			}

		}
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}
