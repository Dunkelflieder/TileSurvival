package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntityExplosion extends EntityWeapon {

	private boolean dealtDamage;
	private final float MAX_LIFFETIME = 0.1f;
	private float lifeTime;
	private float radius;

	//private Weapon weapon;

	public EntityExplosion(Map map, Vector pos) {
		super(map, pos);
		init();
	}

	//public EntityExplosion(Entity sender, Map map, Vector pos, float radius, int damageWeapon weapon) {
	public EntityExplosion(Entity sender, Map map, Vector pos, float radius, int damage) {
		super(sender, map, pos, new Vector(radius * 2f), damage);
		resistDamage = true;
		lifeTime = MAX_LIFFETIME;
		this.radius = radius;
		this.sender = sender;
		//this.weapon = weapon;

		pos.addX(-radius).addY(-radius);
		init();
	}

	private void init() {
		textureID = 16 * 15 + 1;
		light = new Light(new Vector(), 20f, 5f);

		Sound explodeSound = new Sound(SoundCategory.EFFECT, "smallpuff1.ogg");
		explodeSound.setPosition(getCenter());
		explodeSound.play();
	}

	public boolean intersects(Vector entityPos) {
		return getCenter().subtract(entityPos).getValue() <= radius;
	}

	@Override
	public void update(float time) {
		light.size = 20f * (lifeTime / MAX_LIFFETIME);

		if (!dealtDamage) {
			for (Entity entity : map.getEntities()) {
				if (canDamage(entity) && intersects(entity.getCenter())) {
					entity.damage(health);
					//weapon.processEffect(entity);
				}
			}

			dealtDamage = true;
		}

		lifeTime -= time;
		if (lifeTime < 0) {
			kill();
		}
	}

	@Override
	public void onDie() {

	}
}
