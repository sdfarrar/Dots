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

	public Dot(float x, float y, float width, float height) {
		super(x, y, width, height);
		color = Color.white;
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

	@Override
	public void update(float delta) {
		previousPosition = position;
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

}
