package game.core;

import game.entity.Dot;
import game.entity.Mouse;

public class DotsGame extends VariableTimestepGame {
	protected Mouse mouse;
	protected Dot dot;
	protected Dot dot2;
	
	public DotsGame(){
		super();
	}
	
	@Override
	public void input() {
		mouse.input();
	}

	@Override
	public void renderText() {
//      int height = renderer.getDebugTextHeight("Context");
//      renderer.drawDebugText("FPS: " + timer.getFPS() + " | UPS: " + timer.getUPS(), 5, 5 + height);
	}

	@Override
	public void initGameObjects() {
		mouse = new Mouse(0, 0, 25);
		mouse.init();
		dot = new Dot(300, 200, 50, 100);
		dot.init();
		
		dot2 = new Dot(400, 400, 125, 75);
		dot2.init();
	}

	@Override
	public void updateGameObjects(float delta) {
		mouse.update(delta);
		dot.update(delta);
		dot2.update(delta);
	}

	@Override
	public void renderGameObjects(float alpha) {
		dot.render(renderer, alpha);
		dot2.render(renderer, alpha);
		mouse.render(renderer, alpha);
	}

	@Override
	public void disposeGameObjects() {
		mouse.dispose();
		dot.dispose();
		dot2.dispose();
	}
	
}
