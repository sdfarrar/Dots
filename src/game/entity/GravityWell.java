package game.entity;

import graphics.GameRenderer;

import java.awt.Color;

public class GravityWell extends Entity {

	public GravityWell(float x, float y, float radius) {
		super(x, y, radius, radius);
		color = Color.red;
	}

	@Override
	public void init() {

	}

	@Override
	public void input() {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
		renderer.drawCircle(getX(), getY(), getWidth(), color);
	}

	@Override
	public void dispose() {

	}

}
