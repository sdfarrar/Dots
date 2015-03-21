package game.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import graphics.GameRenderer;
import graphics.opengl.Shader;
import graphics.opengl.ShaderProgram;
import graphics.opengl.VertexArrayObject;
import graphics.opengl.VertexBufferObject;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import math.Matrix4f;
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

        		//deltaPosition = previousPosition.subtract(position);

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
			deltaPosition = new Vector2f(previousPosition.x-position.x, previousPosition.y-position.y);
			System.out.println(deltaPosition);
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
