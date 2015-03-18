package game.core;

public abstract class FixedTimestepGame extends AbstractGame {
	
	@Override
    public void gameloop() {
        float delta;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;
        float alpha;

        while (running) {
            if (window.isClosing()) {
                running = false;
                break;
            }

            delta = timer.getDelta();
            accumulator += delta;

            input();

            // Update game and timer UPS if enough time has passed
            while (accumulator >= interval) {
                update();
                timer.updateUPS();
                accumulator -= interval;
            }

            // Calculate alpha value for interpolation
            alpha = accumulator / interval;


            render(alpha);
            timer.updateFPS();

            timer.update();

            renderText();

            // Update window to show the new screen
            window.update();

            //window.setVSync(false);
            if (!window.isVSyncEnabled()) {
                sync(TARGET_FPS);
                //sync(120);
            }
        }
    }
	
}
