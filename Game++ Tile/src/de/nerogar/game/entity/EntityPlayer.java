package de.nerogar.game.entity;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.Map;
import de.nerogar.game.weapon.Fireball;
import de.nerogar.game.weapon.Weapon;

public class EntityPlayer extends Entity {

	private ArrayList<Weapon> weapons;
	private int selectedWeapon;

	public EntityPlayer(Map map, float posX, float posY) {
		super(map, posX, posY);
		weapons = new ArrayList<Weapon>();
		weapons.add(new Fireball(this, 3, 0.3f));
	}

	@Override
	public void update(float time) {
		moveSpeed = 2.5f;

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) moveSpeed *= 2.0f;

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveX(moveSpeed * time);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveX(-moveSpeed * time);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveY(-moveSpeed * time);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveY(moveSpeed * time);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			map.setTile((int) posX, (int) posY, Map.TORCH);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			selectedWeapon = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2) && weapons.size() > 1) {
			selectedWeapon = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3) && weapons.size() > 2) {
			selectedWeapon = 2;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_4) && weapons.size() > 3) {
			selectedWeapon = 3;
		}

		for (Weapon weapon : weapons) {
			weapon.update(time);
		}

		if (Mouse.isButtonDown(0)) {
			float targetX = ((float) Mouse.getX()) / map.getTileSize() + map.getOffsX();
			float targetY = (float) (Display.getHeight() - Mouse.getY()) / map.getTileSize() + map.getOffsY();

			weapons.get(selectedWeapon).start(targetX, targetY);
		}

	}
}
