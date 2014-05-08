package de.nerogar.game.sound;

import static org.lwjgl.openal.AL10.AL_PAUSED;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_STOPPED;

import org.lwjgl.openal.OpenALException;

import de.nerogar.game.Vector;

public class Sound {

	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_MODERATE = 1;
	public static final int PRIORITY_HIGH = 2;

	private int sourceID;
	private ALBuffer alBuffer;
	public int priority = PRIORITY_LOW;

	private Vector position;
	private Vector velocity;
	private float gain;
	private float pitch;
	private boolean looping;
	private boolean destroyedWhenDone;
	private int byteOffset;
	private float offset;
	private int state;
	
	private boolean deleted = false;
	
	public Sound(int sourceID, ALBuffer alBuffer, Vector position, Vector velocity, boolean looping, boolean destroyedWhenDone, float gain, float pitch) {
		this.sourceID = sourceID;
		this.setALBuffer(alBuffer);
		this.setPosition(position);
		this.setVelocity(velocity);
		this.setLooping(looping);
		this.setDestroyedWhenDone(destroyedWhenDone);
		this.setGain(gain);
		this.setPitch(pitch);
		ALHelper.bindBufferToSource(alBuffer, this);
	}

	public void update() {
		try {
			byteOffset = ALHelper.getByteOffset(this);
			state = ALHelper.getSourceState(this);
		} catch (OpenALException e) {
			e.printStackTrace();
			System.out.println("error fetching offset for Source-ID " + sourceID);
		}
		offset = (float) byteOffset / alBuffer.getSize();
		// TODO
		setVelocity(new Vector(0f, 0f));
	}

	public void play() {
		ALHelper.play(this);
	}

	public void stop() {
		ALHelper.stop(this);
	}

	public void pause() {
		ALHelper.pause(this);
	}

	public boolean isStopped() {
		return (state == AL_STOPPED);
	}

	public boolean isPaused() {
		return (state == AL_PAUSED);
	}

	public boolean isPlaying() {
		return (state == AL_PLAYING);
	}

	public void destroy() {
		ALHelper.destroySource(this);
	}

	public int getSourceID() {
		return sourceID;
	}

	public ALBuffer getAlBuffer() {
		return alBuffer;
	}

	public void setALBuffer(ALBuffer alBuffer) {
		this.alBuffer = alBuffer;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		ALHelper.setPosition(this, position);
		this.position = position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		ALHelper.setVelocity(this, velocity);
		this.velocity = velocity;
	}

	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		ALHelper.setGain(this, gain);
		this.gain = gain;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		ALHelper.setPitch(this, pitch);
		this.pitch = pitch;
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		ALHelper.setOffset(this, offset);
		this.offset = offset;
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		ALHelper.setLooping(this, looping);
		this.looping = looping;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Sound)) return false;
		if (((Sound) o).sourceID == sourceID) return true;
		return false;
	}
	
	public void markDeleted() {
		deleted = true;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public boolean isDestroyedWhenDone() {
		return destroyedWhenDone;
	}

	public void setDestroyedWhenDone(boolean destroyedWhenDone) {
		this.destroyedWhenDone = destroyedWhenDone;
	}

}
