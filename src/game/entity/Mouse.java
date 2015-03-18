package game.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import graphics.GameRenderer;
import graphics.opengl.Shader;
import graphics.opengl.ShaderProgram;
import graphics.opengl.VertexArrayObject;
import graphics.opengl.VertexBufferObject;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import math.Matrix4f;
import math.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse extends Entity{
	private boolean pressed;
	private GLFWCursorPosCallback mousePosCallback;
	
	//Rendering shit
	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private Shader vertexShader;
	private Shader fragmentShader;
	private ShaderProgram program;

	private FloatBuffer vertices;
	private int numVertices;
	private boolean drawing;

	public Mouse(float x, float y, float radius) {
		super(x, y, radius, radius);
		
		long id = GLFW.glfwGetCurrentContext();
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(id, widthBuffer, heightBuffer);
        int height = heightBuffer.get();
        int width = widthBuffer.get();
        glfwSetCursorPosCallback(id, mousePosCallback = new GLFWCursorPosCallback(){
        	@Override
        	public void invoke(long window, double xpos, double ypos) {
        		previousPosition = position;
        		position.x=(float)xpos;
        		position.y=(float) (height-ypos); // should invert y axis for cursor on the screen
        		
        		System.out.println("xpos: " + position.x + ", ypos: " + position.y + "    pressed: " + pressed);
        	}
        });

	}
	
	public void init(){
		numVertices = 0;
        drawing = false;
        
        // generate vao
        vao = new VertexArrayObject();
        vao.bind();

        // generate vbo
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        // create the vertices buffer
        vertices = BufferUtils.createFloatBuffer(4096);

        // upload null data to the gpu to allocate space for the vbo
        long size = vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_STREAM_DRAW);        

        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, "res/test_vertex.glsl");
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, "res/test_fragment.glsl");

        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.bindFragmentDataLocation(0, "fragColor");
        program.link();
        program.use();

        // Get width and height of window for the orthographic matrix
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();

        specifyVertexAttributes();

        // Set texture uniform
//        int uniTex = program.getUniformLocation("texImage");
//        program.setUniform(uniTex, 0);

        // Set projection matrix to an orthographic projection
        Matrix4f ortho = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
        int uniProjection = program.getUniformLocation("ortho");
        program.setUniform(uniProjection, ortho);

        // Enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void input() {
		long window = GLFW.glfwGetCurrentContext();
		pressed = (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS);
		if(glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS){
			width = height += 2.5;
		}else if(glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS){
			if(width>15){
				width = height -= 2.5;
			}else{
				System.out.println("Minimum radius of " + width + " reached.");
			}
		}
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(GameRenderer renderer, float alpha) {
//		Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
//        float x = interpolatedPosition.x;
//        float y = interpolatedPosition.y;        
//        renderer.drawTextureRegion(texture, position.x, position.y, 0, 0, width, height, color);
	}
	
	public void renderSquare(float alpha){
		drawing = true;

		// clear the screen. this will remove anything drawn before this
		//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		float x = position.x;
		float y = position.y;
		
		float r = Color.WHITE.getRed();
		float g = Color.WHITE.getGreen();
		float b = Color.WHITE.getBlue();
		
		Vector2f tl = new Vector2f(x-width/2,y+height/2);
		Vector2f bl = new Vector2f(x-width/2,y-height/2);
		Vector2f tr = new Vector2f(x+width/2, y+height/2);
		Vector2f br = new Vector2f(x+width/2, y-height/2);
		
		vertices.put(bl.x).put(bl.y).put(r).put(g).put(b);//.put(0).put(1);
		vertices.put(tl.x).put(tl.y).put(r).put(g).put(b);//.put(0).put(1);
		vertices.put(tr.x).put(tr.y).put(r).put(g).put(b);//.put(0).put(1);
		
		vertices.put(bl.x).put(bl.y).put(r).put(g).put(b);//.put(0).put(1);
		vertices.put(tr.x).put(tr.y).put(r).put(g).put(b);//.put(0).put(1);
		vertices.put(br.x).put(br.y).put(r).put(g).put(b);//.put(0).put(1);
		
		vertices.flip();
		
		//texture.bind();
		vao.bind();
		program.use();
		
		// upload the new vertex data
		vbo.bind(GL_ARRAY_BUFFER);
		vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		vertices.clear();
		numVertices = 0;		
		drawing = false;
	}
	
	public void renderCircle(float delta){
		drawing = true;

		// clear the screen. this will remove anything drawn before this
		//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		float x = position.x;
		float y = position.y;
		
		float r = Color.WHITE.getRed();
		float g = Color.WHITE.getGreen();
		float b = Color.WHITE.getBlue();
		

		int count = 0;
		for(float i=0; i<2*Math.PI; i+=.015){
			float cx = x + (float) (width * Math.cos(i));
			float cy = y + (float) (width * Math.sin(i));
			vertices.put(cx).put(cy).put(r).put(g).put(b);//.put(0).put(1);
			count++;
		}		
		vertices.flip();
		
		vao.bind();
		program.use();
		
		// upload the new vertex data
		vbo.bind(GL_ARRAY_BUFFER);
		vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

		glDrawArrays(GL_LINE_LOOP, 0, count);
		
		vertices.clear();
		numVertices = 0;		
		drawing = false;
	}

	@Override
	public void dispose() {
		mousePosCallback.release();
		
		vao.delete();
		vbo.delete();
		vertexShader.delete();
		fragmentShader.delete();
		program.delete();
	}
	
	private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
//        program.pointVertexAttribute(posAttrib, 2, 7 * Float.BYTES, 0); // With color + texture
//        program.pointVertexAttribute(posAttrib, 2, 0, 0); // No color, no texture
        program.pointVertexAttribute(posAttrib, 2, 5 * Float.BYTES, 0); // With color, no texture

        /* Specify Color Pointer */
        int colAttrib = program.getAttributeLocation("color");
        program.enableVertexAttribute(colAttrib);
//        program.pointVertexAttribute(colAttrib, 3, 7 * Float.BYTES, 2 * Float.BYTES); // With color + texture
        program.pointVertexAttribute(colAttrib, 3, 5 * Float.BYTES, 2 * Float.BYTES); // With color, no texture
//
//        /* Specify Texture Pointer */
//        int texAttrib = program.getAttributeLocation("texcoord");
//        program.enableVertexAttribute(texAttrib);
//        program.pointVertexAttribute(texAttrib, 2, 7 * Float.BYTES, 5 * Float.BYTES);
    }
}
