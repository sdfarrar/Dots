package game.entity;

import graphics.Renderer;

import java.awt.Color;

import math.Vector2f;

public abstract class Entity{
	protected Vector2f previousPosition;
	protected Vector2f position;
	protected Vector2f direction;
	
	protected float height, width;
	
	protected Color color;
	
	public Entity(float x, float y, float width, float height){
		this.previousPosition = new Vector2f(x, y);
		this.position = new Vector2f(x, y);
		this.width = width;
		this.height = height;
		
		color = Color.WHITE;
	}
	
	public abstract void input();
	public abstract void update(float delta);
	public abstract void render(Renderer renderer, float alpha);
	public abstract void dispose();
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
}
