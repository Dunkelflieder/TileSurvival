package de.nerogar.game.entity;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.InputHandler;
import de.nerogar.game.Map;
import de.nerogar.game.weapon.*;

public class EntityPlayer extends Entity {

	public ArrayList<Weapon> weapons;
	public int selectedWeapon;
	public int energy;
	public int maxEnergy;
	private float nextEnergyRestore;

	public EntityPlayer(Map map, float posX, float posY) {
		super(map, posX, posY, 100);
		weapons = new ArrayList<Weapon>();
		weapons.add(new Fireball(this, 3, 1.0f));
		weapons.add(new SlowDownArea(this, 0, 2.0f));
		weapons.add(new FireBlast(this, 10, 2.0f));
		maxEnergy = 100;
		energy = maxEnergy;
		moveSpeed = 3.0f;
	}

	@Override
	public void update(float time) {
		super.update(time);

		//float sprinting = 1.0f;

		//if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) sprinting *= 2.0f;

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveX(moveSpeed * time * speedmult);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveX(-moveSpeed * time * speedmult);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveY(-moveSpeed * time * speedmult);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveY(moveSpeed * time * speedmult);
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
			float targetX = ((float) Mouse.getX()) / Map.TILE_RENDER_SIZE + map.getOffsX();
			float targetY = (float) (Display.getHeight() - Mouse.getY()) / Map.TILE_RENDER_SIZE + map.getOffsY();

			Weapon weapon = weapons.get(selectedWeapon);

			if (weapon.cooldown <= 0f && energy >= weapon.getEnergyCost()) {
				weapons.get(selectedWeapon).start(targetX, targetY);
				energy -= weapons.get(selectedWeapon).getEnergyCost();
				weapon.cooldown = weapon.maxCooldown;
			}
		}

		if (InputHandler.isMouseButtonPressed(1)) {
			selectedWeapon = (selectedWeapon + 1) % weapons.size();
		}

		if (nextEnergyRestore <= 0 && energy < maxEnergy) {
			nextEnergyRestore = 0.5f;
			energy += 1;
		}
		nextEnergyRestore -= time;

	}

	public Weapon getSelectedWeapon() {
		return weapons.get(selectedWeapon);
	}

	@Override
	public void onDie() {
		//TODO game end
	}

}
