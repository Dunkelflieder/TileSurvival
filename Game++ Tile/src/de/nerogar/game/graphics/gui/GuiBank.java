package de.nerogar.game.graphics.gui;
import de.nerogar.game.graphics.gui.GIngame;

public class GuiBank {

	public static final GNone GUI_NONE = new GNone();
	public static final GTitle GUI_TITLE = new GTitle();
	public static final GLobbyHost GUI_LOBBY_HOST = new GLobbyHost();
	public static final GLobbyClientConnect GUI_LOBBY_CLIENT_CONNECT = new GLobbyClientConnect();
	public static final GLobbyClient GUI_LOBBY_CLIENT = new GLobbyClient();
	public static final GIngame GUI_INGAME = new GIngame();
	public static final GSettings GUI_SETTINGS = new GSettings();
	public static final GEscMenu GUI_ESCMENU = new GEscMenu();
	
	private static Gui selectedGui = GUI_TITLE;
	private static boolean interceptsInput; // buffering 1 frame for gui changes

	public static void selectGui(Gui gui) {
		selectedGui = gui;
		gui.select();
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
