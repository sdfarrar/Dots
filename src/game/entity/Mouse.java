package game.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import graphics.GameRenderer;

import java.nio.IntBuffer;

import math.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse extends Entity{
	private Vector2f deltaPosition;
	private boolean pressed;
	private GLFWCursorPosCallback mousePosCallback;
	
	private boolean moved;

	public Mouse(float x, float y, float radius) {
		super(x, y, radius, radius);
		moved = false;
		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
        int height = heightBuffer.get();
        //int width = widthBuffer.get();
        glfwSetCursorPosCallback(id, mousePosCallback = new GLFWCursorPosCallback(){
        	@Override
        	public void invoke(long window, double xpos, double ypos) {
        		previousPosition = new Vector2f(position.x, position.y);
        		position.x=(float)xpos;
        		position.y=(float) (height-ypos); // should invert y axis for cursor on the screen

        		moved=true;
        	}
        });

	}
	
	public void init(){
		
	}

	@Override
	public void input() {
		long window = GLFW.glfwGetCurrentContext();
		pressed = (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS);
		if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS){
			width = height += 2.5;
		}else if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS){
			if(width>15){
				width = height -= 2.5;
			}else{
				System.out.println("Minimum radius of " + width + " reached.");
			}
		}
		
		
	}

	@Override
	public void update(float delta) {
		if(moved){
			deltaPosition = previousPosition.subtract(position);
			moved = false;
		}else{
			deltaPosition = new Vector2f();
		}
	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
		Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
		float x = interpolatedPosition.x;
		float y = interpolatedPosition.y;
		renderer.drawCircle(x, y, width, color);
	}

	@Override
	public void dispose() {
		mousePosCallback.release();
	}
	
	public float getDx(){
		return deltaPosition.x;
	}
	
	public float getDy(){
		return deltaPosition.y;
	}
	
	public boolean isPressed(){
		return pressed;
	}

}
