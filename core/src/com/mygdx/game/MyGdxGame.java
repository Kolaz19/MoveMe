package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;


public class MyGdxGame extends Game {
	public Hero hero;
	private Animation winAnimation;
	private Animation looseAnimation;


	@Override
	public void create () {
		//Win/Loose Texts
		winAnimation = new Animation(new Texture("winText.png"),50,2);
		looseAnimation = new Animation(new Texture("looseText.png"),50,2);
		//Create char
		hero = new Hero(3*16+0.5f,0+0.5f,15);
		hero.addAnimationIdle(new Texture ("charIdle.png"),60,1);
		hero.addAnimationMove(new Texture("charMove.png"),3,11);
		hero.addAnimationExplode(new Texture ("charExplode.png"),3,17);
		hero.addAnimationAppear(new Texture ("charAppearing.png"),1,225);
		chooseLevel(1);
	}

	public void chooseLevel(int levelToPlay) {
		hero.reset(3*16+0.5f,0+0.5f);
		//TestLevel
		switch (levelToPlay) {
			case 1:
			Enemy[] enemiesLevel1 = new Enemy[2];
			Enemy enemy1Level1 = new Enemy(16 + 0.5f, 3 * 16 + 0.5f, 15);
			enemy1Level1.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			enemy1Level1.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			enemy1Level1.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			enemiesLevel1[0] = enemy1Level1;
			Enemy enemy2Level1 = new Enemy(0.5f, 3 * 16 + 0.5f, 15);
			enemy2Level1.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			enemy2Level1.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			enemy2Level1.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			enemiesLevel1[1] = enemy2Level1;
			//Level 1
			setScreen(new LevelBasic(this,1,hero, enemiesLevel1, "Level1.tmx", "Default", winAnimation, looseAnimation));
			break;
			case 2:
			Enemy enemy1Level2 = new Enemy(16 + 0.5f, 9 * 16 + 0.5f, 15);
			enemy1Level2.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			enemy1Level2.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			enemy1Level2.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			Enemy[] enemiesLevel2 = new Enemy[2];
			enemiesLevel2[0] = enemy1Level2;
			Enemy enemy2Level2 = new Enemy(0 + 0.5f, 9 * 16 + 0.5f, 15);
			enemy2Level2.addAnimationIdle(new Texture("enemyIdle.png"), 60, 1);
			enemy2Level2.addAnimationMove(new Texture("enemyMove.png"), 3, 11);
			enemy2Level2.addAnimationAppear(new Texture("enemyAppearing.png"), 1, 225);
			enemiesLevel2[1] = enemy2Level2;
			setScreen(new LevelBasic(this,2,hero,enemiesLevel2,"Level2.tmx","Default", winAnimation, looseAnimation));
			break;
		}


	}


	@Override
	public void dispose () {

	}
}
