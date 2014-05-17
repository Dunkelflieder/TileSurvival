package de.nerogar.game.pathfinder;

import java.util.Random;

import de.nerogar.game.Game;
import de.nerogar.game.Map;
import de.nerogar.game.Position;
import de.nerogar.game.RenderHelper;

public class Node {

	public final float cost;
	private Node pointer;
	public Position position;
	private byte state;
	private static Random random = new Random();
	private float costCalc;
	private boolean diagonal;

	public static byte STATE_INIT = 0;
	public static byte STATE_OPEN = 1;
	public static byte STATE_CLOSED = 2;

	public Node(int cost, Node pointer, Position pos) {
		this.cost = cost;
		this.pointer = pointer;
		this.position = pos;
	}

	public float getTotalCost(Position goal) {
		if (costCalc < 0) {
			Position diff = goal.subtracted(position);
			if (pointer == null) {
				costCalc = cost * (isDiagonal() ? 1.41421356f : 1) + diff.getValue() + random.nextFloat();
			} else {
				costCalc = pointer.getTotalCost(goal) + cost * (isDiagonal() ? 1.41421356f : 1) + diff.getValue() + random.nextFloat();
			}
		}
		return costCalc;
	}

	public void render() {
		if (pointer != null) {
			float offsetX = Game.game.map.getOffsX();
			float offsetY = Game.game.map.getOffsY();
			RenderHelper.renderLine((position.getX() - offsetX + 0.5f) * Map.TILE_RENDER_SIZE, (position.getY() - offsetY + 0.5f) * Map.TILE_RENDER_SIZE, (pointer.position.getX() - offsetX + 0.5f) * Map.TILE_RENDER_SIZE, (pointer.position.getY() - offsetY + 0.5f) * Map.TILE_RENDER_SIZE, 5);
			pointer.render();
		}
	}

	@Override
	public int hashCode() {
		return position.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node))
			return false;
		return position.equals(((Node) o).position);
	}

	public void setPointer(Node node) {
		costCalc = -1;
		pointer = node;
	}

	public Node getPointer() {
		return pointer;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public boolean isDiagonal() {
		return diagonal;
	}

	public void setDiagonal(boolean diagonal) {
		this.diagonal = diagonal;
		costCalc = -1;
	}

}
