package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends Game {
	public SpriteBatch gr_batch;
	public OrthographicCamera gr_camera;
	public Hero gr_char;
	OrthogonalTiledMapRenderer gr_mapRender;

	@Override
	public void create () {
		gr_batch = new SpriteBatch();
		//camera
		gr_camera = new OrthographicCamera(960,960);
		//Win/Loose Texts
		Animation lr_winAnimation = new Animation(new Texture("winText.png"),50,2);
		Animation lr_looseAnimation = new Animation(new Texture("looseText.png"),50,2);

		//Create char
		gr_char = new Hero(3*16+0.5f,0+0.5f,15);
		gr_char.addAnimationIdle(new Texture("charIdle.png"),60,1);
		gr_char.addAnimationMove(new Texture("charMove.png"),3,11);
		gr_char.addAnimationExplode(new Texture("charExplode.png"),3,17);
		gr_char.addAnimationAppear(new Texture("charAppearing.png"),1,225);
		//Create enemy array
		Enemy[] la_enemies = new Enemy[2];
		Enemy lr_enemy1 = new Enemy(16+0.5f, 3*16+0.5f,15);
		lr_enemy1.addAnimationIdle(new Texture("enemyIdle.png"),60,1);
		lr_enemy1.addAnimationMove(new Texture("enemyMove.png"),3,11);
		lr_enemy1.addAnimationAppear(new Texture("enemyAppearing.png"),1,225);
		la_enemies[0] = lr_enemy1;
		Enemy lr_enemy2 = new Enemy(0.5f, 3*16+0.5f,15);
		lr_enemy2.addAnimationIdle(new Texture("enemyIdle.png"),60,1);
		lr_enemy2.addAnimationMove(new Texture("enemyMove.png"),3,11);
		lr_enemy2.addAnimationAppear(new Texture("enemyAppearing.png"),1,225);
		la_enemies[1] = lr_enemy2;
		//Level 1
		setScreen(new LevelBasic(this,la_enemies,"Level1.tmx","Default",lr_winAnimation,lr_looseAnimation));
	}


	@Override
	public void dispose () {
		gr_batch.dispose();
	}
}
