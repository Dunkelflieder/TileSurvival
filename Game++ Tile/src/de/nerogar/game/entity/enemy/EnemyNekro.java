package de.nerogar.game.entity.enemy;

import java.util.ArrayList;

import de.nerogar.game.*;
import de.nerogar.game.entity.*;
import de.nerogar.game.pathfinder.Pathfinder;

public class EnemyNekro extends EntityEnemy {

	private Entity targetBones;
	private ArrayList<Position> path;
	private int pathProgress;

	private float nextRandomUpdate = 0f;

	private float nextSpawn;

	public EnemyNekro(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 50, 1f);
		moveSpeed = 3.0f;
		textureID = 16;

		targetBones = getTarget();
	}

	private EntityBone getTarget() {
		ArrayList<EntityBone> bones = new ArrayList<>();
		for (Entity entity : map.getEntities()) {
			if (entity instanceof EntityBone) {
				bones.add((EntityBone) entity);
			}
		}

		if (bones.size() > 0) return bones.get((int) (Math.random() * bones.size()));
		else return null;
	}

	@Override
	public void update(float time) {
		super.update(time);
		nextRandomUpdate -= time;

		if (nextRandomUpdate < 0f) {
			if (targetBones == null) return;
			path = Pathfinder.getPath(map, getCenter().toPosition(), targetBones.getCenter().toPosition());
			if (path == null) {
				System.out.println("no path, target player");
				targetBones = map.getRandomPlayer();
				path = Pathfinder.getPath(map, getCenter().toPosition(), targetBones.getCenter().toPosition());
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

		if (intersects(targetBones)) {
			if (nextSpawn < 0) {
				map.spawnEntity(new EnemySkeleton(map, targetBones.pos.clone()));
				nextSpawn = (float) (10f * Math.random() + 5f);
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
