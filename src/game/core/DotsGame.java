package game.core;

import game.entity.Dot;
import game.entity.Mouse;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

public class DotsGame extends VariableTimestepGame {
	private Mouse mouse;
	private List<Dot> dots;
	
	
	public DotsGame(){
		super();
		dots = new ArrayList<Dot>();
		
		
	}
	
	@Override
	public void input() {
		mouse.input();
	}

	@Override
	public void renderText() {
//      int height = renderer.getDebugTextHeight("Context");
//      renderer.drawDebugText("FPS: " + timer.getFPS() + " | UPS: " + timer.getUPS(), 5, 5 + height);
	}

	@Override
	public void initGameObjects() {
		mouse = new Mouse(0, 0, 25);
		mouse.init();
		
		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
        int height = heightBuffer.get();
        int width = widthBuffer.get();
        
//        for(float i=2; i<width; i+=4){
//        	for(float j=2; j<height; j+=4){
//        		dots.add(new Dot(i, j, 1, 1));
//        	}
//        }
        
        dots.add(new Dot(width/2, height/2, 2, 2));
	}

	@Override
	public void updateGameObjects(float delta) {
		mouse.update(delta);
//		dot.update(delta);
//		dot2.update(delta);
		dots.forEach((dot) -> dot.update(delta));
	}

	@Override
	public void renderGameObjects(float alpha) {
//		dot.render(renderer, alpha);
//		dot2.render(renderer, alpha);
		dots.forEach((dot) -> dot.render(renderer, alpha));
		mouse.render(renderer, alpha);
	}

	@Override
	public void disposeGameObjects() {
		mouse.dispose();
//		dot.dispose();
//		dot2.dispose();
	}
	
}
