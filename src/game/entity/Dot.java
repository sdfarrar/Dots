package game.entity;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.awt.Color;

import math.Vector2f;
import graphics.GameRenderer;
import graphics.opengl.Shader;
import graphics.opengl.ShaderProgram;

public class Dot extends Entity {
	
	private Shader vertexShader;
	private Shader fragmentShader;
	private ShaderProgram program;
	
	private Vector2f velocity;

	public Dot(float x, float y, float width, float height) {
		super(x, y, width, height);
		color = Color.white;
		velocity = new Vector2f();
	}
	
	@Override
	public void init() {
//		vertexShader = Shader.loadShader(GL_VERTEX_SHADER, "res/test_vertex.glsl");
//        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, "res/test_fragment.glsl");
//
//        program = new ShaderProgram();
//        program.attachShader(vertexShader);
//        program.attachShader(fragmentShader);
//        program.bindFragmentDataLocation(0, "fragColor");
        //program.link();
        //program.use();
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub

	}

	public void update(float delta) {
		previousPosition = position;
		position = position.add(velocity);
		//System.out.println(delta);
	}

	@Override
	public void dispose() {
//		vertexShader.delete();
//		fragmentShader.delete();
//		program.delete();
	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
		Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
		float x = interpolatedPosition.x;
		float y = interpolatedPosition.y;
//		x = position.x;
//		y = position.y;
		renderer.drawSquare(program, x, y, width, height, color);
	}

	public void checkCollision(Mouse mouse){
		float center_x = mouse.getX();
		float center_y = mouse.getY();
		float x = position.x;
		float y = position.y;
		float radius = mouse.getWidth();
		if(Math.pow((x - center_x),2) + Math.pow((y - center_y),2) <= Math.pow(radius,2)){
			float dx = mouse.getDx();
			float dy = mouse.getDy();
			velocity = new Vector2f(-dx, -dy);
		}
	}
	
}
