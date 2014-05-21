package de.nerogar.game.entity;

import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class EntitySpear extends EntityProjectile {

	private Sound activateSound = new Sound(SoundCategory.EFFECT, "throw1.ogg", "throw2.ogg");
	
	public EntitySpear(Map map, Vector pos) {
		super(map, pos, 8);
		dimension = new Vector(1f);
		init();
	}

	public EntitySpear(Entity sender, Map map, Vector pos, Vector target, int damage) {
		super(sender, map, pos, target, damage, 3, 30);
		init();
	}

	@Override
	protected void init() {
		textureID = 16 * 13;
		light = new Light(new Vector(), 2, 0.8f);
		turnable = true;
		
		activateSound.setPosition(pos);
		activateSound.randomizeBuffer();
		activateSound.randomizePitch(0.4f);
		activateSound.play();
	}

}
