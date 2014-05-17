package de.nerogar.game;

public class Timer {

	private static long time = System.nanoTime();
	
	public static void resetTime() {
		time = System.nanoTime();
	}
	
	public static long getTime() {
		return System.nanoTime()-time;
	}
	
	public static void displayAndReset(String str) {
		System.out.println(str+": "+((System.nanoTime()-time)/1000f)+"µs");
		resetTime();
	}
	
}
