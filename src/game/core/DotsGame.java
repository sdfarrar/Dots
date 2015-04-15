package game.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import game.entity.Dot;
import game.entity.GravityWell;
import game.entity.Mouse;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class DotsGame extends VariableTimestepGame {
	private Mouse mouse;
	private List<Dot> dots;
	private List<GravityWell> wells;
	private int gameWidth, gameHeight;

	private GLFWKeyCallback keycallback;
	private GLFWMouseButtonCallback mousebuttoncallback;

	private boolean reset;
	private boolean freeze;
	private boolean heatmap;
	private boolean wrap;
	
	private boolean showUI;
	private boolean showStats;

	private int gravityType;

	public DotsGame(){
		super();
		dots = new ArrayList<Dot>();
		wells = new ArrayList<GravityWell>();
		reset = false;
		freeze = false;
		heatmap = false;
		wrap = false;
		showUI = true;
		showStats = false;
		gravityType = 0;
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
			textRenderer.drawDebugText("(S)Show Stats", x, y - height*6 - vSpace);
			if(showStats){
				textRenderer.drawDebugText("FPS:" + timer.getFPS(), x, y - height*7 - vSpace);
				textRenderer.drawDebugText("UPS:" + timer.getUPS(), x, y - height*8 - vSpace);
				textRenderer.drawDebugText("Dot count:" + dots.size(), 10, y - height*9 - vSpace);
			}
		}
	}

	@Override
	public void initGameObjects() {
		mouse = new Mouse(0, 0, 50);
		mouse.init();

		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
		gameHeight = heightBuffer.get();
		gameWidth = widthBuffer.get();

		for(float i=1; i<gameWidth; i+=4){
			for(float j=1; j<gameHeight; j+=4){
				dots.add(new Dot(i, j, 1, 1));
			}
		}
	}

	@Override
	public void updateGameObjects(float delta) {
		mouse.update(delta);

		if(reset){
			dots.forEach((dot) -> dot.reset());
			wells.clear();
			reset = false;
		}else{
			dots.forEach((dot) -> {
				if(!freeze){
					dot.update(delta);
				}
				dot.collidesWith(mouse);
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
		dots.forEach((dot) -> dot.render(renderer, alpha, freeze));
		wells.forEach((well) -> well.render(renderer, alpha));
		mouse.render(renderer, alpha);


	}

	@Override
	public void disposeGameObjects() {
		keycallback.release();
		mousebuttoncallback.release();
		mouse.dispose();
	}

}
