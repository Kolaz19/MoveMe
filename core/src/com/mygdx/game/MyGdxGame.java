package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	public Hero hero;

	@Override
	public void create () {
		//Create char
		hero = new Hero(4,1,14);
		chooseLevel(1);
	}

	public void chooseLevel(int levelToPlay) {
		hero.reset(2,1);
		//TestLevel
		switch (levelToPlay) {
			case 1:
			Enemy[] enemiesLevel1 = new Enemy[3];
			Enemy enemy1Level1 = new Enemy(2, 4, 14);
			enemiesLevel1[0] = enemy1Level1;
			Enemy enemy2Level1 = new Enemy(1, 4, 14);
			enemiesLevel1[1] = enemy2Level1;
			Enemy enemy3Level1 = new Enemy(1, 3, 14);
			enemiesLevel1[2] = enemy3Level1;
			//Level 1
			setScreen(new LevelBasic(this,1,hero, enemiesLevel1, "Level1.tmx", "Default"));
			break;
			case 2:
			Enemy enemy1Level2 = new Enemy(2, 10, 14);
			Enemy[] enemiesLevel2 = new Enemy[2];
			enemiesLevel2[0] = enemy1Level2;
			Enemy enemy2Level2 = new Enemy(1, 10, 14);
			enemiesLevel2[1] = enemy2Level2;
			setScreen(new LevelBasic(this,2,hero,enemiesLevel2,"Level2.tmx","Default"));
			break;
		}
	}


	@Override
	public void dispose () {

	}

}
