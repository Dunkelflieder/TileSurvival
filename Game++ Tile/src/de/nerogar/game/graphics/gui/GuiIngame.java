package de.nerogar.game.graphics.gui;

import de.nerogar.game.Game;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class GuiIngame extends Gui {

	private GuiStatusBar barHealth;
	private GuiStatusBar barEnergy;
	private GuiButton buttonTest;
	private Sound testsound = new Sound(SoundCategory.MUSIC, "music1.ogg");

	public GuiIngame() {

		barHealth = new GuiStatusBar(new Vector(0.5f, 10f), 0);
		barEnergy = new GuiStatusBar(new Vector(0.5f, 10.2f), 1);
		buttonTest = new GuiButton(new Vector(0.5f, 0.5f), new Vector(4, 1), "Musik an");

		addGuiElements(barHealth, barEnergy, buttonTest);

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
		EntityPlayer player = Game.game.map.getPlayer();
		barHealth.setPosition((float) player.health / player.maxHealth);
		barEnergy.setPosition((float) player.energy / player.maxEnergy);
	}

}
