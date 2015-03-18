package game.core;

public class DotsGame extends VariableTimestepGame {

	@Override
	public void input() {
		mouse.input();
	}

	@Override
	public void renderText() {
//      int height = renderer.getDebugTextHeight("Context");
//      renderer.drawDebugText("FPS: " + timer.getFPS() + " | UPS: " + timer.getUPS(), 5, 5 + height);
	}
	
}
