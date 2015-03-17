/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package game.state;

import static org.lwjgl.opengl.GL11.glClearColor;
import game.entity.Mouse;
import graphics.Renderer;
import other.Texture;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;


public class GameState implements State {

    public static final int NO_COLLISION = 0;
    public static final int COLLISION_TOP = 1;
    public static final int COLLISION_BOTTOM = 2;
    public static final int COLLISION_RIGHT = 3;
    public static final int COLLISION_LEFT = 4;

    private Texture texture;
    private final Renderer renderer;

//    private Paddle player;
//    private Paddle opponent;
//    private Ball ball;
    private Mouse mouse;

    private int playerScore;
    private int opponentScore;
    private int gameWidth;
    private int gameHeight;
    
    private GLFWCursorPosCallback mousePosCallback;

    public GameState(Renderer renderer) {
        this.renderer = renderer;
        
    }

    @Override
    public void input() {
    	mouse.input();
//        player.input(null);
//        opponent.input(ball);
    }

    @Override
    public void update(float delta) {
    	mouse.update(delta);;
        /* Update position */
//        player.update(delta);
//        opponent.update(delta);
//        ball.update(delta);

        /* Check for collisions */
//        player.checkCollision(gameHeight);
//        ball.collidesWith(player);
//        opponent.checkCollision(gameHeight);
//        ball.collidesWith(opponent);

        /* Update score if necessary */
//        switch (ball.checkCollision(gameWidth, gameHeight)) {
//            case COLLISION_LEFT:
//                opponentScore++;
//                break;
//            case COLLISION_RIGHT:
//                playerScore++;
//                break;
//        }
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        //renderer.clear();

        /* Draw game objects */
        //texture.bind();
        //renderer.begin();
        mouse.render(renderer, alpha);
//        player.render(renderer, alpha);
//        opponent.render(renderer, alpha);
//        ball.render(renderer, alpha);
        //renderer.end();

    }

    @Override
    public void enter() {
        /* Get width and height of framebuffer */
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();

        /* Load texture */
        texture = Texture.loadTexture("res/texture.png");

        /* Initialize game objects */
        float speed = 250f;
//        player = new Paddle(Color.GREEN, texture, 5f, (height - 100) / 2f, speed, true);
//        opponent = new Paddle(Color.RED, texture, width - 25f, (height - 100) / 2f, speed, false);
//        ball = new Ball(Color.BLUE, texture, (width - 20) / 2f, (height - 20) / 2f, speed);
        
        /* Initialize variables */
        playerScore = 0;
        opponentScore = 0;
        gameWidth = width;
        gameHeight = height;

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
    	if(texture!=null)
    		texture.delete();
    }

	@Override
	public void dispose() {
		mouse.dispose();		
	}
}
