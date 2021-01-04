package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class MyGdxGame extends Game {
	public Hero hero;
	private Animation winAnimation;
	private Animation looseAnimation;
	private Texture heroIdleTexture;
	private Texture heroMoveTexture;
	private Texture heroExplodeTexture;
	private Texture heroAppearTexture;
	private Texture enemyIdleTexture;
	private Texture enemyMoveTexture;
	private Texture enemyAppearTexture;


	@Override
	public void create () {
		//Load Textures
		heroIdleTexture = new Texture("charIdle.png");
		heroMoveTexture = new Texture ("charMove.png");
		heroExplodeTexture = new Texture("charExplode.png");
		heroAppearTexture = new Texture("charAppearing.png");
		enemyIdleTexture = new Texture("enemyIdle.png");
		enemyMoveTexture = new Texture ("enemyMove.png");
		enemyAppearTexture = new Texture("enemyAppearing.png");
		//Win/Loose Texts
		winAnimation = new Animation(new Texture("winText.png"),50,80,32);
		looseAnimation = new Animation(new Texture("looseText.png"),50,80,32);
		//Create char
		hero = new Hero(4,1,14);
		hero.addAnimationIdle(heroIdleTexture,60,14,14);
		hero.addAnimationMove(heroMoveTexture,3,14,14);
		hero.addAnimationExplode(heroExplodeTexture,3,45,45);
		hero.addAnimationAppear(heroAppearTexture,10,14,14);
		chooseLevel(1);
	}

	public void chooseLevel(int levelToPlay) {
		hero.reset(2,1);
		//TestLevel
		switch (levelToPlay) {
			case 1:
			Enemy[] enemiesLevel1 = new Enemy[2];
			Enemy enemy1Level1 = new Enemy(2, 4, 14);
			enemy1Level1.addAnimationIdle(enemyIdleTexture, 60, 14,14);
			enemy1Level1.addAnimationMove(enemyMoveTexture, 3, 14,14);
			enemy1Level1.addAnimationAppear(enemyAppearTexture, 10, 14,14);
			enemiesLevel1[0] = enemy1Level1;
			Enemy enemy2Level1 = new Enemy(1, 4, 14);
			enemy2Level1.addAnimationIdle(enemyIdleTexture, 60, 14,14);
			enemy2Level1.addAnimationMove(enemyMoveTexture, 3, 14,14);
			enemy2Level1.addAnimationAppear(enemyAppearTexture, 10, 14,14);
			enemiesLevel1[1] = enemy2Level1;
			//Level 1
			setScreen(new LevelBasic(this,1,hero, enemiesLevel1, "Level1.tmx", "Default", winAnimation, looseAnimation));
			break;
			case 2:
			Enemy enemy1Level2 = new Enemy(2, 10, 14);
			enemy1Level2.addAnimationIdle(enemyIdleTexture, 60, 14,14);
			enemy1Level2.addAnimationMove(enemyMoveTexture, 3, 14,14);
			enemy1Level2.addAnimationAppear(enemyAppearTexture, 10, 14,14);
			Enemy[] enemiesLevel2 = new Enemy[2];
			enemiesLevel2[0] = enemy1Level2;
			Enemy enemy2Level2 = new Enemy(1, 10, 14);
			enemy2Level2.addAnimationIdle(enemyIdleTexture, 60, 14,14);
			enemy2Level2.addAnimationMove(enemyMoveTexture, 3, 14,14);
			enemy2Level2.addAnimationAppear(enemyAppearTexture, 10, 14,14);
			enemiesLevel2[1] = enemy2Level2;
			setScreen(new LevelBasic(this,2,hero,enemiesLevel2,"Level2.tmx","Default", winAnimation, looseAnimation));
			break;
		}
	}


	@Override
	public void dispose () {
		winAnimation.dispose();
		looseAnimation.dispose();
		heroIdleTexture.dispose();
		heroMoveTexture.dispose();
		heroExplodeTexture.dispose();
		heroAppearTexture.dispose();
		enemyIdleTexture.dispose();
		enemyMoveTexture.dispose();
		enemyAppearTexture.dispose();
	}

}
