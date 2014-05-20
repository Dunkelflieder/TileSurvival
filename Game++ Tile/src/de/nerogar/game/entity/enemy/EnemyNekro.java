package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemyNekro extends EntityEnemy {

	private ArrayList<Position> path;
	private int pathProgress;

	private float nextSpawn;

	public EnemyNekro(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 20, 1f);
		moveSpeed = 3.0f;
		textureID = 16 * 3;

		target = getTarget();
	}

	private EntityBone getTarget() {
		ArrayList<EntityBone> bones = new ArrayList<>();
		for (Entity entity : map.getEntities()) {
			if (entity instanceof EntityBone) {
				bones.add((EntityBone) entity);
			}
		}

		if (bones.size() > 0)
			return bones.get((int) (Math.random() * bones.size()));
		else
			return null;
	}

	@Override
	public void recalcPath() {
		path = Pathfinder.getPath(map, getCenter().toPosition(), target.getCenter().toPosition());
		if (path == null) {
			System.out.println("no path, target player");
			target = map.getRandomPlayer();
			path = Pathfinder.getPath(map, getCenter().toPosition(), target.getCenter().toPosition());
		}
		pathProgress = -1;
	}

	@Override
	public void update(float time) {
		super.update(time);

		if (nextRandomUpdate < 0f) {
			if (target == null) return;
			recalcPath();

			nextRandomUpdate = (float) (Math.random() * 10.0);
			System.out.println("random update, next in " + nextRandomUpdate);
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
			if (nextSpawn < 0) {
				map.spawnEntity(new EnemySkeleton(map, target.pos.clone()));
				nextSpawn = (float) (10f * Math.random() + 15f);
			}
			nextSpawn -= time;
		}
	}

	@Override
	public void onDie() {
		if (Math.random() < 0.3) {
			map.spawnEntity(new EntityEnergyDrop(map, getCenter()));
		}
	}
}
