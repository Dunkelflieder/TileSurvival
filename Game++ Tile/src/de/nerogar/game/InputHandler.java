package de.nerogar.game;

import java.util.ArrayList;

import org.lwjgl.input.*;

public class InputHandler {
	private static ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedKeysNext = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedMouseButtonsNext = new ArrayList<Integer>();
	private static ArrayList<Integer> onPressedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> onReleasedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Key> registeredKeys = new ArrayList<Key>();

	public static void update(Game game) {
		updateOns(game);
		pressedKeys.clear();
		pressedKeys.addAll(pressedKeysNext);
		pressedMouseButtons.clear();
		pressedMouseButtons.addAll(pressedMouseButtonsNext);
	}

	public static void updateOns(Game game) {
		onPressedMouseButtons.clear();
		for (Integer button : pressedMouseButtonsNext) {
			if (pressedMouseButtons.indexOf(button) == -1) onPressedMouseButtons.add(button);
		}
		onReleasedMouseButtons.clear();
		for (Integer button : pressedMouseButtons) {
			if (pressedMouseButtonsNext.indexOf(button) == -1) onReleasedMouseButtons.add(button);
		}
	}

	//update keyboard buttons

	public static void registerKey(String name, int button) {
		if (registeredKeys.indexOf(name) == -1) {
			registeredKeys.add(new Key(name, button));
		}
	}

	public static boolean isKeyDown(String name) {
		int index = -1;

		for (int i = 0; i < registeredKeys.size(); i++) {
			if (registeredKeys.get(i).name.equals(name)) index = i;
		}
		if (index != -1) return isKeyDown(registeredKeys.get(index).button);
		return false;
	}

	public static boolean isKeyPressed(String name) {
		int index = -1;

		for (int i = 0; i < registeredKeys.size(); i++) {
			if (registeredKeys.get(i).name.equals(name)) index = i;
		}
		if (index != -1) return isKeyPressed(registeredKeys.get(index).button);
		return false;
	}

	public static boolean isKeyReleased(String name) {
		int index = -1;

		for (int i = 0; i < registeredKeys.size(); i++) {
			if (registeredKeys.get(i).name.equals(name)) index = i;
		}
		if (index != -1) return isKeyReleased(registeredKeys.get(index).button);
		return false;
	}

	public static boolean isKeyDown(int key) {
		boolean state = Keyboard.isKeyDown(key);
		updateKey(key, state);
		return state;
	}

	public static boolean isKeyPressed(int key) {
		boolean state = Keyboard.isKeyDown(key);
		boolean lastState = pressedKeys.indexOf(key) == -1;
		pressedKeys.add(key);
		updateKey(key, state);
		return state && lastState;
	}

	public static boolean isKeyReleased(int key) {
		boolean state = Keyboard.isKeyDown(key);
		boolean lastState = pressedKeys.indexOf(key) == -1;
		pressedKeys.add(key);
		updateKey(key, state);
		return !state && !lastState;
	}

	public static char getPressedKey() {
		int key = 0;
		boolean lastState = true;
		while (Keyboard.next()) {
			key = Keyboard.getEventCharacter();
		}
		return (char) (lastState ? key : 0);
	}

	private static void updateKey(int key, boolean state) {
		if (state) addKey(key);
		else removeKey(key);
	}

	private static void addKey(int key) {
		if (pressedKeysNext.indexOf(key) == -1) pressedKeysNext.add(key);
	}

	private static void removeKey(int key) {
		if (pressedKeysNext.indexOf(key) != -1) pressedKeysNext.remove(pressedKeysNext.indexOf(key));
	}

	//update mouse buttons

	public static boolean isMouseButtonDown(int button) {
		boolean state = Mouse.isButtonDown(button);
		updateMouseButton(button, state);
		return state;

	}

	/*public static boolean isMouseButtonPressed(int button) {
	    boolean state = Mouse.isButtonDown(button);
	    boolean lastState = pressedMouseButtons.indexOf(button) != -1;
	    //if (state && !lastState) pressedMouseButtons.add(button);
	    updateMouseButton(button, state);
	    return state && !lastState;
	}

	public static boolean isMouseButtonReleased(int button) {//geht nicht :(
		boolean state = Mouse.isButtonDown(button);
		boolean lastState = pressedMouseButtons.indexOf(button) != -1;
		//if (!state && lastState) pressedMouseButtons.remove(pressedMouseButtons.indexOf(button));
		//updateMouseButton(button, state);
		return !state && lastState;
	}*/

	public static boolean isMouseButtonPressed(int button) {
		boolean state = Mouse.isButtonDown(button);
		//if (state) pressedMouseButtons.add(button);
		updateMouseButton(button, state);
		return onPressedMouseButtons.indexOf(button) != -1;
	}

	public static boolean isMouseButtonReleased(int button) {
		boolean state = Mouse.isButtonDown(button);
		//boolean lastState = pressedMouseButtons.indexOf(button) != -1;
		//if (!state && lastState) pressedMouseButtons.remove(pressedMouseButtons.indexOf(button));
		updateMouseButton(button, state);
		return onReleasedMouseButtons.indexOf(button) != -1;
	}

	private static void updateMouseButton(int key, boolean state) {
		if (state) addMouseButton(key);
		else removeMouseButton(key);
	}

	private static void addMouseButton(int key) {
		if (pressedMouseButtonsNext.indexOf(key) == -1) pressedMouseButtonsNext.add(key);
	}

	private static void removeMouseButton(int key) {
		if (pressedMouseButtonsNext.indexOf(key) != -1) pressedMouseButtonsNext.remove(pressedMouseButtonsNext.indexOf(key));
	}

	public static class Key {
		public String name;
		public int button;

		public Key(String name, int button) {
			this.name = name;
			this.button = button;
		}

		public boolean equals(Object o) {
			if (o instanceof Key) {
				if (((Key) o).name.equals(name)) return true;
			}
			return false;
		}
	}

	public static class GamepadButton {
		public String name;
		public String button;
		public float deadZone;

		public GamepadButton(String name, String button, float deadZone) {
			this.name = name;
			this.button = button;
			this.deadZone = deadZone;
		}

		public boolean equals(GamepadButton o) {
			if (o instanceof GamepadButton) {
				if (((GamepadButton) o).name.equals(name)) return true;
			}
			return false;
		}
	}
}
