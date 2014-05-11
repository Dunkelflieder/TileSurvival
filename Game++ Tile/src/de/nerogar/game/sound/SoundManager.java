package de.nerogar.game.sound;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Vector;

public class SoundManager {

	public static SoundManager instance = new SoundManager();
	private static ALSource[] sourceSpots;
	private static String[] preLoadedFiles = new String[] {};

	private static Vector lastListenerPosition = new Vector(0, 0);
	private static float lastUpdateTime = System.nanoTime() / 1000000000;
	public static final int CAMERA_HEIGHT = 2;
	public static boolean alCreated = false;

	static {
		if (!SoundManager.alCreated) SoundManager.createAL();
		System.out.println(".ogg sound extension available: " + ALHelper.initVorbisExtension());
		ALHelper.readDeviceAttributes();
		sourceSpots = new ALSource[ALHelper.ALC_MONO_SOURCES];
		for (int i = 0; i < sourceSpots.length; i++) {
			sourceSpots[i] = new ALSource();
		}
	}
	
	public static void createAL() {
		try {
			AL.create();
			alCreated = true;
		} catch (LWJGLException e) {
			System.out.println("Could not create OpenAL (Sound) Context!");
			e.printStackTrace();
		}
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
					sourceSpots[i].update();
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
			SoundManager.setListener(position, velocity, new float[] { 0, 0, 1 }, new float[] { 0, -1, 0 });
		}

	}

	public static ALSource getSource(int i) {
		if (i < 0 || i >= sourceSpots.length)
			return null;
		return sourceSpots[i];
	}

	public static ALSource getFreeSource(int priority) {
		ALSource newSource = null;
		for (ALSource source : sourceSpots) {
			if (source.isUncoupled()) {
				return source;
			} else if (source.isStopped()) {
				source.uncouple();
				return source;
			} else if (source.priority <= priority) {
				priority = source.priority;
				newSource = source;
			}
		}
		if (newSource == null) {
			System.out.println("OpenAL ERROR: All Sources are occupied. No free source for Priority " + priority + " was found!");
			return null;
		};
		newSource.uncouple();
		return newSource;
	}

	public static void shutdown() {
		clear();
		AL.destroy();
	}

}
