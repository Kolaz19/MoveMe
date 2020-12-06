package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends Game {
	public SpriteBatch gr_batch;
	public OrthographicCamera gr_camera;
	public Character gr_char;
	OrthogonalTiledMapRenderer gr_mapRender;

	@Override
	public void create () {
		gr_batch = new SpriteBatch();
		//camera
		gr_camera = new OrthographicCamera(960,960);
		//Create char
		gr_char = new Character(3*16+0.5f,0+0.5f,16);
		gr_char.addAnimationIdle("charIdle.png",60,1);
		gr_char.addAnimationMove("charMove.png",30,17);
		//Level 1
		setScreen(new LevelBasic(this,null,"Level1.tmx","Default"));
		//TODO check if you can return from LevelBasic and dispose its elements
	}


	@Override
	public void dispose () {
		gr_batch.dispose();
	}
}
