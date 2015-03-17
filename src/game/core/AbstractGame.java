package game.core;

import graphics.GameRenderer;
import graphics.Window;

public abstract class AbstractGame {
	
	protected Window window;
	protected GameRenderer renderer;
	
	
	public AbstractGame(){
		window = new Window(800, 600, "Dots", false);
		renderer = new GameRenderer();
	}
	
	public void start(){
		init();
		gameloop();
		dispose();
	}
	
	public void init(){
		renderer.init();
	}
	
	public abstract void gameloop();
	
	public void dispose(){
		window.destroy();
		renderer.dispose();
	}
	
	
}
