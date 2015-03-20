package game.core;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

import game.entity.Dot;
import game.entity.Mouse;
import graphics.GameRenderer;
import graphics.Window;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;

public abstract class AbstractGame {
	public static final int TARGET_FPS = 120;
    public static final int TARGET_UPS = 30;
    
	protected Timer timer;
	protected Window window;
	protected GameRenderer renderer;
	protected boolean running;
	
	private GLFWErrorCallback errorCallback;
	
	
	
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
		
		// Initialize glfw
		if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }
		
		window = new Window(800, 600, "Dots", false);
		timer.init();
		renderer.init();
		
//		mouse = new Mouse(0, 0, 25);
//		mouse.init();
//		dot = new Dot(300, 200, 50, 100);
//		dot.init();
//		
//		dot2 = new Dot(400, 400, 125, 75);
//		dot2.init();
		initGameObjects();
		
		running = true;
	}
	
	public abstract void initGameObjects();
	public abstract void updateGameObjects(float delta);
	public abstract void renderGameObjects(float alpha);
	public abstract void disposeGameObjects();
	public abstract void gameloop();
	public abstract void input();
	public abstract void renderText();
	
	public void update(){
		update(1f / TARGET_UPS);
	}
	
	public void update(float delta){
//		mouse.update(delta);
//		dot.update(delta);
//		dot2.update(delta);
		updateGameObjects(delta);
	}	
	
	public void render(){
		render(1f);
	}
	
	public void render(float alpha){
		renderer.clear();
		renderer.begin();
		//mouse.renderCircle(alpha);
		renderGameObjects(alpha);
//		dot.render(renderer, alpha);
//		dot2.render(renderer, alpha);
//		mouse.render(renderer, alpha);
		renderer.end();
	}	
	
	public void dispose(){
		window.destroy();
		renderer.dispose();
//		mouse.dispose();
//		dot.dispose();
//		dot2.dispose();
		disposeGameObjects();
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
