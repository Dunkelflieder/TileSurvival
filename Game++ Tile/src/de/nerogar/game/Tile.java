package de.nerogar.game;

public class Tile {

	public final int id;
	public final boolean collide;
	public final int cost;

	public Tile(int id, boolean collide, int cost) {
		this.id = id;
		this.collide = collide;
		this.cost = cost;
	}
	
	public Tile(int id, boolean collide) {
		this.id = id;
		this.collide = collide;
		this.cost = -1;
	}
}
