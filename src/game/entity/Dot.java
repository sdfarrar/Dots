package game.entity;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;

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
		color = Color.white;
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
				float dx = mouse.getDx()*0.1f;
				float dy = mouse.getDy()*0.1f;
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
			velocity = velocity.scale(-1);
		}else if(position.x>windowWidth){
			position.x=windowWidth;
			velocity = velocity.scale(-1);
		}else if(position.y<0){
			position.y = 0;
			velocity = velocity.scale(-1);
		}else if(position.y>windowHeight){
			position.y=windowHeight;
			velocity = velocity.scale(-1);
		}
	}
	
	public void reset(){
		position = originalPosition;
		velocity = new Vector2f();
	}
}
