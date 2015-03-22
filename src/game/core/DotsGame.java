package game.core;

import static org.lwjgl.glfw.GLFW.*;
import game.entity.Dot;
import game.entity.Mouse;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFW.*;

public class DotsGame extends VariableTimestepGame {
	private Mouse mouse;
	private List<Dot> dots;
	private int gameWidth, gameHeight;
	
	private GLFWKeyCallback keycallback;
	
	private boolean reset;
	private boolean freeze;
	
	public DotsGame(){
		super();
		dots = new ArrayList<Dot>();
		reset = false;
		freeze = false;
	}
	
	public void init(){
		super.init();
		
		long id = glfwGetCurrentContext();
		glfwSetKeyCallback(id, keycallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long id, int key, int scancode, int action, int mods) {
				if(key==GLFW_KEY_R && action==GLFW_PRESS){
					reset = true;
				}
				if(key==GLFW_KEY_F && action==GLFW_PRESS){
					freeze = !freeze;
				}
				if(key==GLFW_KEY_ESCAPE){					
					window.close(id);
				}
			}
		});
	}
	
	@Override
	public void input() {
		mouse.input();
		
//		long window = glfwGetCurrentContext();
//		if(glfwGetKey(window, GLFW_KEY_R)==GLFW_PRESS){
//			reset = true;
//		}
//		if(glfwGetKey(window, GLFW_KEY_F)==GLFW_PRESS){
//			freeze = !freeze;
//			System.out.println("F pressed");
//		}
	}

	@Override
	public void renderText() {
//      int height = renderer.getDebugTextHeight("Context");
//      renderer.drawDebugText("FPS: " + timer.getFPS() + " | UPS: " + timer.getUPS(), 5, 5 + height);
	}

	@Override
	public void initGameObjects() {
		mouse = new Mouse(0, 0, 50);
		mouse.init();
		
		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
        gameHeight = heightBuffer.get();
        gameWidth = widthBuffer.get();
        
        for(float i=1; i<gameWidth; i+=4){
        	for(float j=1; j<gameHeight; j+=4){
        		dots.add(new Dot(i, j, 1, 1));
        	}
        }
//        for(float i=(gameWidth/2)-50; i<(gameWidth/2)+50; i+=2){
//        	for(float j=(gameHeight/2)-50; j<(gameHeight/2)+50; j+=2){
//        		dots.add(new Dot(i, j, 1, 1));
//            }
//        }
	}

	@Override
	public void updateGameObjects(float delta) {
		mouse.update(delta);
		
		if(reset){
			dots.forEach((dot) -> dot.reset());
			reset = false;
		}else{
			dots.forEach((dot) -> {
				if(!freeze){
					dot.update(delta);
				}
				dot.collidesWith(mouse);
				dot.checkCollision(gameWidth, gameHeight, delta);
			});		
		}
	}

	@Override
	public void renderGameObjects(float alpha) {
		dots.forEach((dot) -> dot.render(renderer, alpha));
		mouse.render(renderer, alpha);
	}

	@Override
	public void disposeGameObjects() {
		keycallback.release();
		mouse.dispose();
	}
	
}
