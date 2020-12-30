package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGdxGame extends Game {
	public SpriteBatch gr_batch;
	public OrthographicCamera gr_camera;
	public Hero gr_char;
	OrthogonalTiledMapRenderer gr_mapRender;
	private Animation gr_winAnimation;
	private Animation gr_looseAnimation;
	public ShapeRenderer gr_shapeRenderer;


	@Override
	public void create () {
		//Batches
		gr_batch = new SpriteBatch();
		gr_shapeRenderer = new ShapeRenderer();
		gr_shapeRenderer.setAutoShapeType(true);
		gr_shapeRenderer.setColor(Color.BLACK);
		//camera
		gr_camera = new OrthographicCamera(960,960);
		//Win/Loose Texts
		gr_winAnimation = new Animation(new Texture("winText.png"),50,2);
		gr_looseAnimation = new Animation(new Texture("looseText.png"),50,2);
		//Create char
		gr_char = new Hero(3*16+0.5f,0+0.5f,15);
		gr_char.addAnimationIdle(new Texture ("charIdle.png"),60,1);
		gr_char.addAnimationMove(new Texture("charMove.png"),3,11);
		gr_char.addAnimationExplode(new Texture ("charExplode.png"),3,17);
		gr_char.addAnimationAppear(new Texture ("charAppearing.png"),1,225);
		chooseLevel(1);
	}

	public void chooseLevel(int iv_level) {
		gr_char.reset(3*16+0.5f,0+0.5f);
		//TestLevel
		switch (iv_level) {
			case 1:
			Enemy[] la_enemies = new Enemy[2];
			Enemy lr_enemy1 = new Enemy(16 + 0.5f, 3 * 16 + 0.5f, 15);
			lr_enemy1.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			lr_enemy1.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			lr_enemy1.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			la_enemies[0] = lr_enemy1;
			Enemy lr_enemy2 = new Enemy(0.5f, 3 * 16 + 0.5f, 15);
			lr_enemy2.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			lr_enemy2.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			lr_enemy2.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			la_enemies[1] = lr_enemy2;
			//Level 1
			setScreen(new LevelBasic(this, 1, la_enemies, "Level1.tmx", "Default", gr_winAnimation, gr_looseAnimation));
			break;
			case 2:
			Enemy lr_enemy3 = new Enemy(16 + 0.5f, 9 * 16 + 0.5f, 15);
			lr_enemy3.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			lr_enemy3.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			lr_enemy3.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			Enemy[] la_enemies2 = new Enemy[2];
			la_enemies2[0] = lr_enemy3;
			Enemy lr_enemy4 = new Enemy(0 + 0.5f, 9 * 16 + 0.5f, 15);
			lr_enemy4.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			lr_enemy4.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			lr_enemy4.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			la_enemies2[1] = lr_enemy4;
			setScreen(new LevelBasic(this,2,la_enemies2,"Level2.tmx","Default",gr_winAnimation,gr_looseAnimation));
			break;
		}


	}


	@Override
	public void dispose () {
		gr_batch.dispose();
	}
}
