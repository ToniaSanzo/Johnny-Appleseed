package com.toni;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toni.managers.GameInputProcessor;
import com.toni.managers.GameKeys;
import com.toni.managers.GameStateManager;

public class Game extends ApplicationAdapter {

	public static int WIDTH;               // Stage width
	public static int HEIGHT;              // Stage height

	public static OrthographicCamera cam;  // Camera used to view Game Universe

	private GameStateManager gsm;

	SpriteBatch batch;
	Texture bgTex;
	Sprite sprite;


	@Override
	public void create () {
		WIDTH  = Gdx.graphics.getWidth();            // Set WIDTH on startup
		HEIGHT = Gdx.graphics.getHeight();           // Set HEIGHT on startup

		cam = new OrthographicCamera(WIDTH, HEIGHT); // Initialize camera dimensions

		cam.translate(WIDTH / 2, HEIGHT / 2); // Move Camera Right: WIDTH/2, Move Camera UO: HEIGHT/2
		cam.update();                                // Update cam to reflect the changes made with translate

		Gdx.input.setInputProcessor(new GameInputProcessor());
		gsm = new GameStateManager();

		batch = new SpriteBatch();


		bgTex = new Texture(Gdx.files.internal("Background.png"));
		sprite = new Sprite(bgTex, 0, 0, 1497, 1262);
		sprite.setPosition(0, 0);
		sprite.setSize(WIDTH,  HEIGHT);
		sprite.setRotation(0);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, .2f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		sprite.draw(batch);
		batch.end();

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();

		GameKeys.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bgTex.dispose();
	}
}
