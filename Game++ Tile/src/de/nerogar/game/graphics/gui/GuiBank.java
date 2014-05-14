package de.nerogar.game.graphics.gui;
import de.nerogar.game.graphics.gui.GIngame;

public class GuiBank {

	public static final Gui GUI_NONE = new GNone();
	public static final Gui GUI_TITLE = new GTitle();
	public static final Gui GUI_LOBBY_HOST = new GLobbyHost();
	public static final Gui GUI_LOBBY_CLIENT = new GLobbyClient();
	public static final Gui GUI_INGAME = new GIngame();
	public static final Gui GUI_SETTINGS = new GSettings();
	public static final Gui GUI_ESCMENU = new GEscMenu();
	
	private static Gui selectedGui = GUI_TITLE;
	private static boolean interceptsInput; // buffering 1 frame for gui changes

	public static void selectGui(Gui gui) {
		selectedGui = gui;
	}

	public static void update() {
		interceptsInput = selectedGui.interceptsInput();
		selectedGui.update();
	}

	public static void render() {
		selectedGui.render();
	}
	
	public static boolean interceptsInput() {
		return interceptsInput;
	}

}
