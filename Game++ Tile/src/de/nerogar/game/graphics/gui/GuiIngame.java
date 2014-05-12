package de.nerogar.game.graphics.gui;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.sound.Sound;

public class GuiIngame extends Gui {

	private EntityPlayer player;
	private GuiStatusBar barHealth;
	private GuiStatusBar barEnergy;
	private GuiButton buttonTest;
	private Sound testsound = new Sound("music1.ogg");

	public GuiIngame(EntityPlayer player) {

		this.player = player;

		barHealth = new GuiStatusBar(new Vector(0.5f, 10f), 0);
		barEnergy = new GuiStatusBar(new Vector(0.5f, 10.2f), 1);
		buttonTest = new GuiButton(new Vector(0.5f, 0.5f), new Vector(4, 1), "Musik an");

		addGuiElements(barHealth, barEnergy, buttonTest);

	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void renderBackground() {
		// no background
	}

	@Override
	public void click(int id, int which) {
		if (id == buttonTest.getId()) {
			if (!testsound.isPlaying()) {
				buttonTest.setText("Musik aus");
				testsound.play();
			} else {
				testsound.pause();
				buttonTest.setText("Musik an");
			}
		}
	}

	@Override
	public void update() {
		if (player == null)
			return;
		barHealth.setPosition((float) player.health / player.maxHealth);
		barEnergy.setPosition((float) player.energy / player.maxEnergy);
	}

}
