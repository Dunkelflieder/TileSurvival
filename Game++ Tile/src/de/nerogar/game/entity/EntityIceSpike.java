package de.nerogar.game.entity;

import java.util.ArrayList;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntityIceSpike extends EntityWeapon {

	private Vector source;
	private Vector direction;

	private int hitCount;
	private int maxHitcount = 2;
	private ArrayList<Entity> hitEntities;

	private Sound explodeSound;

	public EntityIceSpike(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(1f);
		init();
	}

	public EntityIceSpike(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, new Vector(1f), damage);
		resistDamage = true;

		this.source = pos.clone();

		this.sender = sender;

		moveSpeed = 30.0f;

		direction = target.subtracted(source);
		direction.setValue(moveSpeed);

		hitEntities = new ArrayList<Entity>();

		init();
	}

	private void init() {
		textureID = 16 * 15 + 4;

		light = new Light(new Vector(), 2, 0.8f);

		explodeSound = new Sound(SoundCategory.EFFECT, "ice-die.ogg");
		Sound iceSpawnSound = new Sound(SoundCategory.EFFECT, "ice-start.ogg");
		iceSpawnSound.setPosition(getCenter());
		iceSpawnSound.play();
	}

	@Override
	public void update(float time) {
		super.update(time);

		ArrayList<Entity> intersectingEntities = map.getIntersectingEntities(this);
		for (Entity entity : intersectingEntities) {
			if (canDamage(entity) && intersects(entity) && !hitEntities.contains(entity)) {
				hitCount++;
				hitEntities.add(entity);
				entity.damage(health);
				entity.speedmult = 0.3f;
				entity.speedmultTime = 4f;
			}

			if (hitCount == maxHitcount) {
				kill();
				break;
			}
		}

		if (map.isColliding(direction.multiplied(time).add(pos), dimension, false)) {
			kill();
		} else {
			moveX(direction.getX() * time);
			moveY(direction.getY() * time);
		}
	}

	@Override
	public void onDie() {
		explodeSound.setPosition(getCenter());
		explodeSound.play();
	}
}
