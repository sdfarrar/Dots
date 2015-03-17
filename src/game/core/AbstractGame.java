package game.core;

public abstract class AbstractGame {
	
	
	public AbstractGame(){
		
	}
	
	public void start(){
		init();
		gameloop();
		dispose();
	}
	
	public void init(){
		
	}
	
	public abstract void gameloop();
	
	public void dispose(){
		
	}
	
	
}
