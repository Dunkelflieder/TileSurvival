package de.nerogar.game.graphics.gui;

import de.nerogar.game.Game;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class GIngame extends Gui {

	private GEStatusBar barHealth;
	private GEStatusBar barEnergy;
	private GEButton buttonTest;
	private Sound testsound = new Sound(SoundCategory.MUSIC, "music1.ogg");

	public GIngame() {
		super(false);

		float posY = Game.game.HEIGHT - 100;

		barHealth = new GEStatusBar(new Vector(10, posY), 0);
		barEnergy = new GEStatusBar(new Vector(10, posY + 20f), 1);
		buttonTest = new GEButton(new Vector(0, 0), new Vector(Map.TILE_RENDER_SIZE*2f, Map.TILE_RENDER_SIZE*0.5f), "Musik an");

		addGuiElements(barHealth, barEnergy, buttonTest);

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonTest.getId()) {
			if (!testsound.isPlaying()) {
				testsound.play();
			} else {
				testsound.pause();
			}
		}
	}

	@Override
	public void update() {
		super.update();
		EntityPlayer player = Game.game.map.getPlayer();
		barHealth.setPosition((float) player.health / player.maxHealth);
		barEnergy.setPosition((float) player.energy / player.maxEnergy);

		if (!testsound.isPlaying()) {
			buttonTest.setText("Musik an");
		} else {
			buttonTest.setText("Musik aus");
		}
	}

	@Override
	public void keyPressed(char key) {
		if (key == 27) {
			GuiBank.selectGui(GuiBank.GUI_ESCMENU);
		}
	}
}
