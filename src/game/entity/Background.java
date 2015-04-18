package game.entity;

import java.awt.Color;

import graphics.GameRenderer;
import graphics.opengl.Texture;

public class Background extends Entity {
	
	private Texture texture;

	public Background(float x, float y, float width, float height) {
		super(x, y, width, height);
		//this.texture = texture;
	}


	public void init(String texturePath) {
		texture = Texture.loadTexture(texturePath);
	}

	@Override
	public void input() {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
		texture.bind();
		//renderer.drawTexture(texture, getX(), getY());
		//renderer.drawTextureRegion(texture, getX(), getY(), 0, 0, getWidth(), getHeight());
		//renderer.drawTextureRegion(getX(), getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, 1, 1);
		renderer.drawTexture(texture, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void dispose() {
		texture.delete();
	}


	@Override
	public void init() {
		this.init("res/sadface.png");
	}

}
