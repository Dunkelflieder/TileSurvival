package de.nerogar.game.sound;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Game;
import de.nerogar.game.Vector;
import de.nerogar.game.network.PacketSound;

public class SoundManager {

	private static ALSource[] sources;
	// Fill this with filenames and call preLoadSounds() somewhere at the start if you want to load sounds before first use
	private static String[] preLoadedFiles = new String[] {};

	private static Vector lastListenerPosition = new Vector(0, 0);
	private static float lastUpdateTime = System.nanoTime() / 1000000000;
	public static final int LISTENER_HEIGHT = 2; // since this game is 2D, the listener is considered hovering LISTENER_HEIGHT meters above the world
	public static final float DEFAULT_ROLLOFF_FACTOR = 0.2f; // how fast sounds get silent with increasing distance (0 = always same volume)
	public static boolean alCreated = false; // just a flag determining if the AL context has already been created

	static {
		// Create OpenAL context, if not already done
		// Other classes MIGHT have static sound objects
		// and since they read in a buffer, they cause a early AL context creation 
		SoundManager.createAL();

		// Checks if the local implementation of OpenAL supports direct .ogg input
		// This sound API converts ogg vorbis files to PCM data via JOrbis anyway,
		// because OpenAL dropped the support on this
		//System.out.println(".ogg sound extension available: " + ALHelper.initVorbisExtension());

		// Gets some attributes from the current sound device
		ALHelper.readDeviceAttributes();

		// Create ALSource objects for all channels the sound device supports.
		// Each ALSource object is linked to a source created on the sound device
		sources = new ALSource[ALHelper.MONO_SOURCES];
		for (int i = 0; i < sources.length; i++) {
			sources[i] = new ALSource();
		}
	}

	/** creates the OpenAL Context, if it isn't already created */
	public static void createAL() {
		if (SoundManager.alCreated) return;
		try {
			AL.create(); // quick and easy way to initialize OpenAL with the default audio device
			alCreated = true;
		} catch (LWJGLException e) {
			System.out.println("Could not create OpenAL (Sound) Context!");
			e.printStackTrace();
		}
	}

	/** Reads in all files marked for preloading. Call this at a point you want your programm to preload files */
	public static void preLoadSounds() {
		for (String filename : preLoadedFiles) {
			try {
				ALBufferBank.addSound(filename);
			} catch (OpenALException | IOException | LWJGLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the OpenAL Listener
	 * 
	 * @param position
	 *            Position of the listener
	 * @param velocity
	 *            Velocity of the listener
	 * @param orientationAt
	 *            direction, the listener is looking at
	 * @param orientationUp
	 *            direction, the listener's upside is pointing at
	 */
	public static void setListener(float[] position, float[] velocity, float[] orientationAt, float[] orientationUp) {
		ALHelper.setListener(position, velocity, orientationAt, orientationUp);
	}

	public static void update() {
		for (int i = 0; i < sources.length; i++) {
			if (sources[i] != null) {
				sources[i].update();
			}
		}
	}

	/** Frees all sources on the sound device */
	private static void clear() {
		for (int i = 0; i < sources.length; i++) {
			if (sources[i] != null) {
				sources[i].destroy();
				sources[i] = null;
			}
		}
	}

	/**
	 * This function is the lazy alternative to setListener. It assumes the listener's "eyes" are looking towards z+ with the "head" pointing towards y-. It also calculates the listener's velocity on its own by delta position
	 */
	public static void recalculateListener(Vector position) {

		float time = (float) System.nanoTime() / 1000000000;
		float elapsedTime = time - lastUpdateTime;
		if (elapsedTime > 0) {
			Vector elapsedPosition = position.subtracted(lastListenerPosition);
			Vector velocity = elapsedPosition.multiply(1 / elapsedTime);
			lastUpdateTime = time;
			lastListenerPosition.set(position);
			SoundManager.setListener(new float[] { position.getX(), position.getY(), LISTENER_HEIGHT }, new float[] { velocity.getX(), velocity.getY(), 0 }, new float[] { 0, 0, 1 }, new float[] { 0, -1, 0 });
		}

	}

	/**
	 * Returns an available source to play a sound back. If no free source or source with finished playback was found, it kicks out sounds with lower or equal priorities
	 * 
	 * @param priority
	 *            Priority of the new sound
	 * @return ALSource the sound can be played through, or null if no source is available
	 */
	public static ALSource getFreeSource(int priority) {
		ALSource newSource = null;
		for (ALSource source : sources) {
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
		}
		// If the code reached here, a source with lower or equal priority (the lowest available) is considered the new source
		newSource.uncouple();
		return newSource;
	}

	/** Clears everything and shuts down the OpenAL Context */
	public static void shutdown() {
		ALBufferBank.clear();
		clear();
		AL.destroy();
	}

	public static void updateGain(SoundCategory category) {
		for (int i = 0; i < sources.length; i++) {
			if (sources[i] != null) {
				sources[i].updateGain(category);
			}
		}
	}

	public static void broadcastNetworkSound(Vector pos, String... sound) {

		Sound activateSound = new Sound(SoundCategory.EFFECT, sound);
		activateSound.setPosition(pos);
		activateSound.randomizePitch(0.4f);
		activateSound.play();

		PacketSound packet = new PacketSound();
		packet.sound = sound;
		packet.pos = pos;

		if (Game.game.map.isServerWorld()) {
			Game.game.server.broadcastData(packet);
			playNetworkSound(pos, sound);
		} else {
			Game.game.client.sendPacket(packet);
		}

	}

	public static void playNetworkSound(Vector pos, String... sound) {
		Sound activateSound = new Sound(SoundCategory.EFFECT, sound);
		activateSound.setPosition(pos);
		activateSound.randomizePitch(0.4f);
		activateSound.play();
	}

}
