package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntityFireball extends EntityWeapon {

	private Vector source;
	private Vector target;
	private Vector direction;

	private float hitTime;
	private Entity sender;

	private Sound explodeSound;

	public EntityFireball(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(0.2f);
		init();
	}

	public EntityFireball(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, new Vector(0.2f), damage);
		resistDamage = true;

		this.source = pos.clone();
		this.target = target;

		this.sender = sender;

		moveSpeed = 10.0f;

		direction = target.subtracted(source);
		direction.setValue(moveSpeed);

		hitTime = (target.getX() - source.getX()) / direction.getX();

		init();
	}

	private void init() {
		textureID = 16 * 15;

		light = new Light(new Vector(), 2, 0.8f);
		explodeSound = new Sound(SoundCategory.EFFECT, "smallpuff1.ogg");
		Sound fireball_throw = new Sound(SoundCategory.EFFECT, "fireball_throw1.ogg");
		fireball_throw.setGain(0.2f);
		fireball_throw.setPosition(getCenter());
		fireball_throw.play();
	}

	@Override
	public void update(float time) {
		hitTime -= time;

		if (map.isColliding(direction.multiplied(time).add(pos), dimension, false)) {
			kill();
		} else if (hitTime < 0) {
			pos = target;
			kill();
		} else {
			move(direction.multiplied(time));
		}
	}

	@Override
	public void onDie() {
		map.spawnEntity(new EntityExplosion(sender, map, getCenter(), 2f, health));
		explodeSound.setPosition(getCenter());
		explodeSound.play();
	}
}
