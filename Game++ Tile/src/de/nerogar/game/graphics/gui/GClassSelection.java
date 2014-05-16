package de.nerogar.game.graphics.gui;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import de.nerogar.game.Game;
import de.nerogar.game.Map;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.playerClass.PlayerClass;
import de.nerogar.game.graphics.TextureBank;
import de.nerogar.game.network.PacketSelectPlayerClass;

public class GClassSelection extends Gui {

	private GEText textTitle;
	private GEButton buttonSelect, buttonCancel;
	private GEImage image1, image2, image3, image4;
	private GEText text1, text2, text3, text4;
	private int playerClass = -1;

	public GClassSelection() {
		super(true);

		float posX = Game.game.WIDTH / 2f;
		float posY = Game.game.HEIGHT / 2f;

		textTitle = new GEText(new Vector(0, posY - 150), new Vector(Game.game.WIDTH, 32), "Choose a player class!");

		buttonSelect = new GEButton(new Vector(posX + 16, posY + 150), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Choose");
		buttonCancel = new GEButton(new Vector(posX - (Map.TILE_RENDER_SIZE * 4 + 16f), posY + 150), new Vector(Map.TILE_RENDER_SIZE * 4, Map.TILE_RENDER_SIZE), "Abort");

		image1 = new GEImage(new Vector(posX - 304, posY - 50), new Vector(128, 128), "entity.png", new Vector(0, 0), new Vector(1, 1));
		image2 = new GEImage(new Vector(posX - 144, posY - 50), new Vector(128, 128), "entity.png", new Vector(0, 1), new Vector(1, 1));
		image3 = new GEImage(new Vector(posX + 16, posY - 50), new Vector(128, 128), "entity.png", new Vector(1, 0), new Vector(1, 1));
		image4 = new GEImage(new Vector(posX + 160, posY - 50), new Vector(128, 128), "entity.png", new Vector(1, 1), new Vector(1, 1));

		text1 = new GEText(new Vector(posX - 304, posY + 86), new Vector(128, 16), "Engineer");
		text2 = new GEText(new Vector(posX - 144, posY + 86), new Vector(128, 16), "Healer");
		text3 = new GEText(new Vector(posX + 16, posY + 86), new Vector(128, 16), "Mage");
		text4 = new GEText(new Vector(posX + 160, posY + 86), new Vector(128, 16), "Warrior");
		
		addGuiElements(textTitle, buttonSelect, buttonCancel, image1, image2, image3, image4, text1, text2, text3, text4);

	}

	@Override
	public void select() {
		setPlayerClass(PlayerClass.ENGINEER);
		selectElement(image1);
	}

	@Override
	public void renderBackground() {

		GEImage image = (GEImage) getSelectedElement();
		float x1 = image.getPos().getX();
		float x2 = x1 + image.getSize().getX();
		float y1 = image.getPos().getY();
		float y2 = y1 + image.getSize().getY();

		TextureBank.instance.bindTexture("title.png");

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, -1f);
		glTexCoord2f(1, 0);
		glVertex3f(Game.game.WIDTH, 0, -1f);
		glTexCoord2f(1, 1);
		glVertex3f(Game.game.WIDTH, Game.game.HEIGHT, -1f);
		glTexCoord2f(0, 1);
		glVertex3f(0, Game.game.HEIGHT, -1f);
		glEnd();

		glDisable(GL_TEXTURE_2D);
		glColor3f(1, 1, 1);
		glLineWidth(16f);
		glBegin(GL_LINES);
		glVertex3f(x1, y1, -1);
		glVertex3f(x2, y1, -1);
		glVertex3f(x2, y1, -1);
		glVertex3f(x2, y2, -1);
		glVertex3f(x2, y2, -1);
		glVertex3f(x1, y2, -1);
		glVertex3f(x1, y2, -1);
		glVertex3f(x1, y1, -1);
		glEnd();
		glEnable(GL_TEXTURE_2D);

	}

	@Override
	public void click(int id, int which) {
		if (id == buttonSelect.getId()) {
			int pClass = -1;
			if (getSelectedElement() == image1) {
				pClass = PlayerClass.ENGINEER;
			} else if (getSelectedElement() == image2) {
				pClass = PlayerClass.HEALER;
			} else if (getSelectedElement() == image3) {
				pClass = PlayerClass.MAGE;
			} else if (getSelectedElement() == image4) {
				pClass = PlayerClass.WARRIOR;
			}
			setPlayerClass(pClass);
			if (referrer == GuiBank.LOBBY_CLIENT) {
				GuiBank.selectGui(GuiBank.LOBBY_CLIENT_CONNECT);
				PacketSelectPlayerClass packet = new PacketSelectPlayerClass();
				packet.playerClass = getPlayerClass();
				Game.game.client.sendPacket(packet);
			} else {
				GuiBank.selectGui(GuiBank.LOBBY_HOST);
			}
		} else if (id == buttonCancel.getId()) {
			if (referrer == GuiBank.LOBBY_CLIENT) {
				Game.game.client.stopClient();
			}
			GuiBank.selectGui(referrer);
		}
	}

	@Override
	public void keyPressed(char key) {
		if (key == 'h') {
			GuiBank.selectGui(GuiBank.LOBBY_HOST);
		} else if (key == 'c') {
			GuiBank.selectGui(GuiBank.LOBBY_CLIENT);
		}
	}

	public int getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(int playerClass) {
		this.playerClass = playerClass;
	}

}
