package game.core;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

import silvertiger.tutorial.lwjgl.core.Game;
import graphics.GameRenderer;
import graphics.Window;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

public abstract class AbstractGame {
	public static final int TARGET_FPS = 120;
    public static final int TARGET_UPS = 30;
    
	protected Timer timer;
	protected Window window;
	protected GameRenderer renderer;
	
	private GLFWErrorCallback errorCallback;
	private boolean running;
	
	public AbstractGame(){
		timer = new Timer();
		renderer = new GameRenderer();		
	}
	
	public void start(){
		init();
		gameloop();
		dispose();
	}
	
	public void init(){
		errorCallback = Callbacks.errorCallbackPrint();
		glfwSetErrorCallback(errorCallback);
		
		
		window = new Window(800, 600, "Dots", false);
		timer.init();
		renderer.init();
		running = true;
	}
	
	public abstract void gameloop();
	public abstract void input();
	
	public void dispose(){
		window.destroy();
		renderer.dispose();
		glfwTerminate();
		errorCallback.release();
	}
	
	/**
     * Synchronizes the game at specified frames per second.
     *
     * @param fps Frames per second
     */
    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             CPU but you will loose some accuracy because Thread.sleep(1) could
             sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractGame.class.getName()).log(Level.SEVERE, null, ex);
            }

            now = timer.getTime();
        }
    }
	
	public boolean isRunning(){
		return running;
	}
	
	
}
