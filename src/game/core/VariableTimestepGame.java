package game.core;

public abstract class VariableTimestepGame extends AbstractGame {
	
	@Override
    public void gameloop() {
        float delta;

        while (running) {
            if (window.isClosing()) {
                running = false;
                break;
            }

            delta = timer.getDelta();

            input();
            
            update(delta);
            timer.updateUPS();

            render();
            timer.updateFPS();

            timer.update();

            renderText();

            // finally update the window to show the new screen
            window.update();

            //window.setVSync(false);
            if (!window.isVSyncEnabled()) {
                sync(TARGET_FPS);
            	//sync(120);
            }
        }
    }
	
}
