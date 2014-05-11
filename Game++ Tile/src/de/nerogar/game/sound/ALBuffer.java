package de.nerogar.game.sound;

public class ALBuffer {

	private int size;
	private int bufferID;
	private int channels;

	public ALBuffer(int bufferID, int size, int channels) {
		this.bufferID = bufferID;
		this.size = size;
		this.channels = channels;
	}

	public void destroy() {
		ALHelper.destroyBuffer(bufferID);
	}

	public int getSize() {
		return size;
	}

	public int getChannels() {
		return channels;
	}

	public int getBufferID() {
		return bufferID;
	}

	@Override
	public String toString() {
		return "ALBuffer(size: " + size + ", bufferID: " + bufferID + ", channels: " + channels + ")";
	}
}
