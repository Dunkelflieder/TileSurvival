package de.nerogar.game.graphics.gui;
import de.nerogar.game.graphics.gui.GIngame;

public class GuiBank {

	public static final GNone NONE = new GNone();
	public static final GTitle TITLE = new GTitle();
	public static final GLobbyHost LOBBY_HOST = new GLobbyHost();
	public static final GLobbyClientConnect LOBBY_CLIENT_CONNECT = new GLobbyClientConnect();
	public static final GLobbyClient LOBBY_CLIENT = new GLobbyClient();
	public static final GIngame INGAME = new GIngame();
	public static final GSettings SETTINGS = new GSettings();
	public static final GEscMenu ESCMENU = new GEscMenu();
	public static final GClassSelection CLASS_SELECTION = new GClassSelection();
	public static final GCredits CREDITS = new GCredits();
	
	private static Gui selectedGui = TITLE;
	private static boolean interceptsInput; // buffering 1 frame for gui changes

	public static void selectGui(Gui gui) {
		gui.referrer = selectedGui;
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
