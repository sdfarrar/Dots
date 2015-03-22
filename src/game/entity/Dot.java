package game.entity;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import math.Vector2f;
import graphics.GameRenderer;
import graphics.opengl.Shader;
import graphics.opengl.ShaderProgram;

public class Dot extends Entity {
	
	private Vector2f velocity;
	private final Vector2f originalPosition;

	public Dot(float x, float y, float width, float height) {
		super(x, y, width, height);
		color = Color.green;
		velocity = new Vector2f();
		originalPosition = new Vector2f(x,y);
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void input() {
		
	}

	public void update(float delta) {
		previousPosition = position;
		position = position.add(velocity);
		
		velocity = velocity.subtract(velocity.scale(delta));		
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
		Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
		float x = interpolatedPosition.x;
		float y = interpolatedPosition.y;
		renderer.drawSquare(x, y, width, height, color);
	}

	/**
	 * Checks for collision with the mouse object
	 * @param mouse
	 */
	public void collidesWith(Mouse mouse){
		if(mouse.isPressed()){
			float center_x = mouse.getX();
			float center_y = mouse.getY();
			float x = position.x;
			float y = position.y;
			float radius = mouse.getWidth();
			if(Math.pow((x - center_x),2) + Math.pow((y - center_y),2) <= Math.pow(radius,2)){
				float distance = position.distance(new Vector2f(center_x, center_y));
				float dx = mouse.getDx()/distance*1.75f;
				float dy = mouse.getDy()/distance*1.75f;
				velocity = velocity.add(new Vector2f(-dx, -dy));
			}
		}
	}
	
	/**
	 * Checks for collision with the edge of the window
	 * @param windowWidth window width
	 * @param windowHeight window height
	 */
	public void checkCollision(int windowWidth, int windowHeight, float delta){
		if(position.x<0){
			position.x = 0;
			float jitter = (new Random().nextInt(999)+9000)/10000f;
			velocity = new Vector2f(-velocity.x*jitter, velocity.y);
		}else if(position.x>windowWidth){
			position.x=windowWidth;
			float jitter = (new Random().nextInt(999)+9000)/10000f;
			velocity = new Vector2f(-velocity.x*jitter, velocity.y);
		}else if(position.y<0){
			position.y = 0;
			float jitter = (new Random().nextInt(999)+9000)/10000f;
			velocity = new Vector2f(velocity.x, -velocity.y*jitter);
		}else if(position.y>windowHeight){
			position.y=windowHeight;
			float jitter = (new Random().nextInt(199)+800)/1000f;
			velocity = new Vector2f(velocity.x, -velocity.y*jitter);
			
		}
	}
	
	public void reset(){
		position = originalPosition;
		velocity = new Vector2f();
	}
}
