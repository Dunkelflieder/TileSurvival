package de.nerogar.game.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Vector;

public class ALSource {

	private final int sourceID;
	private ALBuffer buffer = null;
	public int priority = Sound.PRIORITY_MODERATE;
	private int byteOffset;
	private float offset;
	private int state;

	private Sound sound;

	public ALSource() {
		this.sourceID = ALHelper.genSources();
		ALHelper.setRolloffFactor(sourceID, SoundManager.DEFAULT_ROLLOFF_FACTOR);
	}

	public void uncouple() {
		sound.setUncoupled();
		sound = null;
	}
	
	public void couple(Sound sound) {
		this.sound = sound;
	}

	public boolean isUncoupled() {
		return sound == null;
	}

	public void update() {
		if (isUncoupled())
			return;
		try {
			byteOffset = ALHelper.getByteOffset(sourceID);
			state = ALHelper.getSourceState(sourceID);
		} catch (OpenALException e) {
			e.printStackTrace();
			System.out.println("error fetching offset for Source-ID " + sourceID);
		}
		offset = (float) byteOffset / buffer.getSize();
		if (isStopped() && sound.isLooping()) {
			// TODO distinguish between multi-buffer handling
			sound.play();
		}
	}

	public void play() {
		ALHelper.play(sourceID);
	}

	public void stop() {
		ALHelper.stop(sourceID);
	}

	public void pause() {
		ALHelper.pause(sourceID);
	}

	public boolean isStopped() {
		return (state == AL10.AL_STOPPED);
	}

	public boolean isPaused() {
		return (state == AL10.AL_PAUSED);
	}

	public boolean isPlaying() {
		return (state == AL10.AL_PLAYING);
	}

	public void destroy() {
		ALHelper.destroySource(sourceID);
	}

	public int getSourceID() {
		return sourceID;
	}

	public float getOffset() {
		return offset;
	}

	public void setBuffer(ALBuffer buffer) {
		this.buffer = buffer;
		ALHelper.bindBufferToSource(buffer.getBufferID(), sourceID);
	}

	public ALBuffer getBuffer() {
		return buffer;
	}

	public void setPosition(Vector position) {
		ALHelper.setPosition(sourceID, position);
	}

	public void setVelocity(Vector velocity) {
		ALHelper.setVelocity(sourceID, velocity);
	}

	public void setGain(float gain) {
		ALHelper.setGain(sourceID, gain);
	}

	public void setPitch(float pitch) {
		ALHelper.setPitch(sourceID, pitch);
	}

	public void setOffset(float offset) {
		ALHelper.setOffset(sourceID, buffer.getSize(), offset);
	}

	public void setLooping(boolean looping) {
		ALHelper.setLooping(sourceID, looping);
	}

	public boolean equals(Object o) {
		if (!(o instanceof ALSource))
			return false;
		return ((ALSource) o).getSourceID() == sourceID;
	}

}
