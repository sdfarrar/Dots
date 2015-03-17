package main;

import org.lwjgl.glfw.GLFW;

import game.core.DotsGame;

public class Main {

	public static void main(String[] args) {
		DotsGame game = new DotsGame();
		try{
			game.start();
		}finally{
			GLFW.glfwTerminate();
		}
	}

}
