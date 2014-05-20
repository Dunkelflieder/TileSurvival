package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntityTeamRestore extends EntityWeapon {

	private static final float MAX_RESTORE_DISTANCE = 3.0f;

	private float nextRestore;
	private Sound buildSound = new Sound(SoundCategory.EFFECT, "build1.ogg", "build2.ogg", "build3.ogg");

	public EntityTeamRestore(Map map, Vector pos) {
		super(map, pos);
		dimension = new Vector(1f);
		init();
	}

	public EntityTeamRestore(Entity sender, Map map, Vector pos, int damage) {
		super(sender, map, pos, new Vector(1f), damage);
		init();
	}

	private void init() {
		textureID = 16 * 14 + 1;
		resistDamage = false;
		
		buildSound.setPosition(pos);
		buildSound.randomizeBuffer();
		buildSound.play();
	}

	@Override
	public void update(float time) {
		super.update(time);

		nextRestore -= time;

		if (nextRestore <= 0f) {
			nextRestore = 3f;

			for (Entity entity : map.getEntities()) {
				if (entity.faction == faction && !(entity instanceof EntityWeapon)) {
					float entityDist = getCenter().subtract(entity.getCenter()).getValue();
					if (entityDist <= MAX_RESTORE_DISTANCE) {
						if (entity.health < entity.maxHealth) entity.health += health;
						if (entity.energy < entity.maxEnergy) entity.energy += health;

						if (entity.health > entity.maxHealth) entity.health = entity.maxHealth;
						if (entity.energy > entity.maxEnergy) entity.energy = entity.maxEnergy;
					}
				}
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
