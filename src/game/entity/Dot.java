package game.entity;

import graphics.GameRenderer;

import java.awt.Color;
import java.util.Random;

import math.Vector2f;

public class Dot extends Entity {

	private Vector2f velocity;
	private final Vector2f originalPosition;

	public Dot(float x, float y, float width, float height) {
		this(x, y, width, height, new Vector2f());
	}
	
	public Dot(float x, float y, float width, float height, Vector2f initialVelocity){
		super(x, y, width, height);
		color = Color.green;
		velocity = initialVelocity;
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
		render(renderer, alpha, false);		
	}
	
	public void render(GameRenderer renderer, float alpha, boolean frozen) {
		Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
		float x = interpolatedPosition.x;
		float y = interpolatedPosition.y;
		float endX = x+1;
		float endY = y+1;
		if(!frozen){
			float length = velocity.length();
			length = (length<1) ? 1 : length;
			double radians = Math.atan2(velocity.y, velocity.x);
			endX = (float) (length*Math.cos(radians) + x);
			endY = (float) (length*Math.sin(radians) + y);
		}
		renderer.drawLine(x, y, endX, endY, color);
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
	
	public void influencedBy(GravityWell well, int gravityType) {
		float center_x = well.getX();
		float center_y = well.getY();
		float x = position.x;
		float y = position.y;
		float radius = well.getWidth();		
		if(Math.pow((x - center_x),2) + Math.pow((y - center_y),2) <= Math.pow(radius,2)){
			float dx=0f,dy=0f;
			float distance = position.distance(new Vector2f(center_x, center_y));
			switch(gravityType){
			case 0:
				dx = (x-center_x)/750f;
				dy = (y-center_y)/750f;
				break;
			case 1:
				dx = (x-center_x)/distance*0.25f;
				dy = (y-center_y)/distance*0.25f;
				break;
			}
			
			velocity = velocity.add(new Vector2f(-dx, -dy));
		}	
	}

	/**
	 * Checks for collision with the edge of the window
	 * @param windowWidth window width
	 * @param windowHeight window height
	 */
	public void checkCollision(int windowWidth, int windowHeight, float delta, boolean wrap){
		if(position.x<0){
			if(wrap){
				position.x = windowWidth;
			}else{
				position.x = 0;
				float jitter = (new Random().nextInt(999)+9000)/10000f;
				velocity = new Vector2f(-velocity.x*jitter, velocity.y);
			}
		}else if(position.x>windowWidth){
			if(wrap){
				position.x = 0;
			}else{
				position.x=windowWidth;
				float jitter = (new Random().nextInt(999)+9000)/10000f;
				velocity = new Vector2f(-velocity.x*jitter, velocity.y);
			}
		}else if(position.y<0){
			if(wrap){
				position.y = windowHeight;
			}else{
				position.y = 0;
				float jitter = (new Random().nextInt(999)+9000)/10000f;
				velocity = new Vector2f(velocity.x, -velocity.y*jitter);
			}
		}else if(position.y>windowHeight){
			if(wrap){
				position.y = 0;
			}else{
				position.y=windowHeight;
				float jitter = (new Random().nextInt(199)+800)/1000f;
				velocity = new Vector2f(velocity.x, -velocity.y*jitter);
			}
		}
	}

	public void reset(){
		position = originalPosition;
		velocity = new Vector2f();
	}
	
}
