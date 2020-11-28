package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	SpriteBatch gr_batch;
	OrthographicCamera gr_camera;
	Character gr_char;

	@Override
	public void create () {
		gr_batch = new SpriteBatch();
		//camera
		gr_camera = new OrthographicCamera(960,960);


		//setScreen(new LevelBasic(this));
	}


	@Override
	public void dispose () {
		gr_batch.dispose();
	}
}
