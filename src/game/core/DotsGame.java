package game.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import game.entity.Background;
import game.entity.Dot;
import game.entity.GravityWell;
import game.entity.Mouse;
import graphics.opengl.Texture;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class DotsGame extends VariableTimestepGame {
	private static final int DOT_WIDTH = 1, DOT_HEIGHT= 1;
	private static final int SPRAY_RATE = 13;
	private enum InputType { PUSH, SPRAY };
	
	private Mouse mouse;
	private List<Dot> dots;
	private List<GravityWell> wells;
	private Background background;
	private int gameWidth, gameHeight;
	
	private Texture texture;
	private Texture simpleTexture;

	private GLFWKeyCallback keycallback;
	private GLFWMouseButtonCallback mousebuttoncallback;

	private boolean reset;
	private boolean freeze;
	private boolean heatmap;
	private boolean wrap;
	private boolean clearBoard;
	private boolean rebuildDots;
	
	private boolean showUI;
	private boolean showStats;

	private int gravityType;
	private InputType inputType;

	public DotsGame(){
		super();
		dots = new ArrayList<Dot>();
		wells = new ArrayList<GravityWell>();
		reset = false;
		freeze = false;
		heatmap = false;
		wrap = false;
		clearBoard = false;
		showUI = true;
		showStats = false;
		rebuildDots = false;
		gravityType = 0;
		inputType = InputType.PUSH;		
	}

	public void init(){
		super.init();

		long id = glfwGetCurrentContext();
		glfwSetKeyCallback(id, keycallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long id, int key, int scancode, int action, int mods) {
				if(key==GLFW_KEY_R && action==GLFW_PRESS){
					reset = true;
				}
				if(key==GLFW_KEY_F && action==GLFW_PRESS){
					freeze = !freeze;
				}
				if(key==GLFW_KEY_H && action==GLFW_PRESS){
					heatmap = !heatmap;
				}
				if(key==GLFW_KEY_W && action==GLFW_PRESS){
					wrap = !wrap;
				}
				if(key==GLFW_KEY_G && action==GLFW_PRESS){ // && mods==GLFW_MOD_SHIFT
					gravityType = (gravityType==0) ? 1 : 0;
				}
				if(key==GLFW_KEY_SPACE && action==GLFW_PRESS){
					showUI = !showUI;
				}
				if(key==GLFW_KEY_S && action==GLFW_PRESS){
					showStats = !showStats;
				}
				if(key==GLFW_KEY_C && action==GLFW_PRESS){
					clearBoard = true;
					rebuildDots = true;
				}
				if(key==GLFW_KEY_1 && action==GLFW_PRESS){
					inputType = InputType.PUSH;
				}
				if(key==GLFW_KEY_2 && action==GLFW_PRESS){
					inputType = InputType.SPRAY;
				}
				if(key==GLFW_KEY_ESCAPE){					
					window.close(id);
				}
			}
		});

		glfwSetMouseButtonCallback(id, mousebuttoncallback = new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if(button==GLFW_MOUSE_BUTTON_2 && action==GLFW_PRESS){
					wells.add(new GravityWell(mouse.getX(), mouse.getY(), mouse.getWidth()));
				}
			}

		});
	}

	@Override
	public void input() {
		mouse.input();
	}

	@Override
	public void renderText() {
		int x = 10;
		int y = 750;
		int height = textRenderer.getTextHeight("D");		
		int vSpace = 2;
		textRenderer.drawDebugText("(Space)Show UI", x, y);
		
		if(showUI){
			textRenderer.drawDebugText("(W)Wrap:" + ((wrap) ? "On" : "Off"), x, y - height - vSpace);
			textRenderer.drawDebugText("(F)Freeze:" + ((freeze) ? "On" : "Off"), x, y - height*2 - vSpace);
			textRenderer.drawDebugText("(H)Heatmap:" + ((heatmap) ? "On" : "Off"), x, y - height*3 - vSpace);
			textRenderer.drawDebugText("(G)Gravity Type:" + gravityType, 10, y - height*4 - vSpace);
			textRenderer.drawDebugText("(0-9)Input Type:" + inputType, x, y - height*5 - vSpace);
			textRenderer.drawDebugText("(C)Clear All:", 10, y - height*6 - vSpace);
			textRenderer.drawDebugText("(S)Show Stats", x, y - height*8 - vSpace);			
			if(showStats){
				textRenderer.drawDebugText("FPS:" + timer.getFPS(), x, y - height*9 - vSpace);
				textRenderer.drawDebugText("UPS:" + timer.getUPS(), x, y - height*10 - vSpace);
				textRenderer.drawDebugText("Dot count:" + dots.size(), 10, y - height*11 - vSpace);
			}
		}
	}

	@Override
	public void initGameObjects() {
		mouse = new Mouse(0, 0, 50);
		mouse.init();

		buildDots();
	}

	@Override
	public void updateGameObjects(float delta) {
		mouse.update(delta);
		
		if(clearBoard){
			dots.clear();
			wells.clear();
			clearBoard = false;
		}else if(reset){
			dots.forEach((dot) -> dot.reset());
			wells.clear();
			reset = false;
			if(rebuildDots){
				buildDots();
				rebuildDots=false;
			}
		}else{
			if(inputType==InputType.SPRAY && mouse.isPressed()){
				Random r = new Random();
				float radius = mouse.getWidth();
				for(int i=0; i<SPRAY_RATE; i++){
					float xVel = (float) (r.nextFloat()*radius/100) * (((r.nextInt(2)%2)==0) ? 1 : -1);
					float yVel = (float) (r.nextFloat()*radius/100) * (((r.nextInt(2)%2)==0) ? 1 : -1);
					Dot dot = new Dot(mouse.getX(), mouse.getY(), DOT_WIDTH, DOT_HEIGHT, new Vector2f(xVel, yVel));
					dots.add(dot);
				}
			}
			dots.forEach((dot) -> {
				if(!freeze){
					dot.update(delta);
				}
				if(inputType==InputType.PUSH){
					dot.collidesWith(mouse);
				}
				dot.checkCollision(gameWidth, gameHeight, delta, wrap);

				if(!freeze){
					wells.forEach((well) -> {
						dot.influencedBy(well, gravityType);
					});
				}
			});		
		}
	}

	@Override
	public void renderGameObjects(float alpha) {
		//background.render(renderer, alpha);
		texture.bind();
		dots.forEach((dot) -> dot.render(renderer, alpha, freeze));
		renderer.flush();
		simpleTexture.bind();
		wells.forEach((well) -> well.render(renderer, alpha));
		mouse.render(renderer, alpha);
	}

	@Override
	public void disposeGameObjects() {
		//background.dispose();
		texture.delete();
		simpleTexture.delete();
		keycallback.release();
		mousebuttoncallback.release();
		mouse.dispose();
	}
	
	private void buildDots(){
		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
		gameHeight = heightBuffer.get();
		gameWidth = widthBuffer.get();

		for(float i=1; i<gameWidth; i+=4){
			for(float j=1; j<gameHeight; j+=4){
				Dot dot = new Dot(i, j, DOT_WIDTH, DOT_HEIGHT);	
				dots.add(dot);
			}
		}
	}

}
