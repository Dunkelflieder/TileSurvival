package de.nerogar.game.sound;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Vector;

public class SoundManager {

	public static SoundManager instance = new SoundManager();
	private static final int maxSources = 16;
	private static Sound[] sourceSpots;
	private static String[] preLoadedFiles = new String[] {};

	private static Vector lastListenerPosition = new Vector(0, 0);
	private static float lastUpdateTime = System.nanoTime() / 1000000000;
	public static final int CAMERA_HEIGHT = 10;
	
	static {
		try {
			AL.create();
		} catch (LWJGLException e) {
			System.out.println("Could not create OpenAL (Sound) Context!");
			e.printStackTrace();
		}
		System.out.println(".ogg sound extension available: " + ALHelper.initVorbisExtension());
		sourceSpots = new Sound[maxSources];
	}

	public static Sound create(String filename, Vector position) {
		return create(filename, Sound.PRIORITY_MODERATE, position, new Vector(0, 0), false, true, 1f, 1f);
	}

	public static Sound create(String[] filename, Vector position) {
		return create(filename, Sound.PRIORITY_MODERATE, position, new Vector(0, 0), false, true, 1f, 1f);
	}

	public static Sound create(String filename, Vector position, boolean looping, float gain, float pitch) {
		return create(filename, Sound.PRIORITY_MODERATE, position, new Vector(0, 0), looping, true, gain, pitch);
	}

	public static Sound create(String[] filename, Vector position, boolean looping, float gain, float pitch) {
		return create(filename, Sound.PRIORITY_MODERATE, position, new Vector(0, 0), looping, true, gain, pitch);
	}

	public static Sound create(String filename, int priority, Vector position, Vector velocity, boolean looping, boolean destroyWhenDone, float gain, float pitch) {
		return create(new String[] { filename }, priority, position, velocity, looping, destroyWhenDone, gain, pitch);
	}

	public static Sound create(String[] filenames, int priority, Vector position, Vector velocity, boolean looping, boolean destroyWhenDone, float gain, float pitch) {
		int spot = getFreeSpot(priority);
		if (spot == -1) {
			System.out.println("No free source spot for playing " + filenames);
		} else {
			int sourceID = ALHelper.genSources();
			try {
				//ALBuffer[] buffers = new ALBuffer[filenames.length];
				sourceSpots[spot] = new Sound(sourceID, ALBufferBank.getSounds(filenames), position, velocity, looping, destroyWhenDone, gain, pitch);
			} catch (OpenALException | IOException | LWJGLException e) {
				sourceSpots[spot] = null;
				e.printStackTrace();
			}
		}
		if (spot != -1)
			return sourceSpots[spot];
		return null;
	}

	public static int getFreeSpot(int priority) {
		int spot = -1;
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] == null) {
				spot = i;
				break;
			} else {
				if (sourceSpots[i].priority < priority) {
					if (spot == -1)
						spot = i;
					else if (sourceSpots[i].priority < sourceSpots[spot].priority)
						spot = i;
				}
			}
		}
		return spot;
	}

	public static void preLoadSounds() {
		for (String filename : preLoadedFiles) {
			try {
				ALBufferBank.addSound(filename);
			} catch (OpenALException | IOException | LWJGLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setListener(Vector position, Vector velocity, float[] orientationAt, float[] orientationUp) {
		ALHelper.setListener(position, velocity, orientationAt, orientationUp);
	}

	public static void update() {
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] != null) {
				if (sourceSpots[i].isStopped() && sourceSpots[i].isDestroyedWhenDone()) {
					sourceSpots[i].markDeleted();
					sourceSpots[i].destroy();
					sourceSpots[i] = null;
				} else {
					sourceSpots[i].update();
				}
			}
		}
	}

	public static void clear() {
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] != null) {
				sourceSpots[i].destroy();
				sourceSpots[i] = null;
			}
		}
	}

	public static void recalculateListener(Vector position) {

		float time = (float) System.nanoTime() / 1000000000;
		float elapsedTime = time - lastUpdateTime;
		if (elapsedTime > 0) {
			Vector elapsedPosition = position.subtracted(lastListenerPosition);
			Vector velocity = elapsedPosition.multiply(1 / elapsedTime);
			lastUpdateTime = time;
			lastListenerPosition.set(position);
			SoundManager.setListener(position, velocity, new float[] { 0, 0, -1 }, new float[] { 0, -1, 0 });
		}

	}

	public static Sound getSource(int i) {
		if (i < 0 || i >= maxSources)
			return null;
		return sourceSpots[i];
	}

	public static void shutdown() {
		clear();
		AL.destroy();
	}

}
