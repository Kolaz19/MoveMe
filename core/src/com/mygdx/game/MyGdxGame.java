package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	public Hero hero;

	@Override
	public void create () {
		//Create char
		hero = new Hero(4,1,14);
		setScreen(new EndScreen(this));
		//setScreen(new MainMenu(this));
	}

	public void chooseLevel(int levelToPlay) {
		hero.reset(2,1);
		//TestLevel
		switch (levelToPlay) {
			//Level 1
			case 1:
			Enemy[] enemiesLevel1 = new Enemy[4];
			Enemy enemy1Level1 = new Enemy(2, 4, 14);
			enemiesLevel1[0] = enemy1Level1;
			Enemy enemy2Level1 = new Enemy(1, 4, 14);
			enemiesLevel1[1] = enemy2Level1;
			Enemy enemy3Level1 = new Enemy(1, 3, 14);
			enemiesLevel1[2] = enemy3Level1;
			Enemy enemy4Level1 = new Enemy(1, 5, 14);
			enemiesLevel1[3] = enemy4Level1;
			setScreen(new LevelBasic(this,1,hero, enemiesLevel1, "Level1.tmx", "Default"));
			break;
			//Level 2
			case 2:
			hero.reset(2,7);
			Enemy enemy1Level2 = new Enemy(2, 3, 14);
			Enemy[] enemiesLevel2 = new Enemy[2];
			enemiesLevel2[0] = enemy1Level2;
			Enemy enemy2Level2 = new Enemy(2, 4, 14);
			enemiesLevel2[1] = enemy2Level2;
			setScreen(new LevelBasic(this,2,hero,enemiesLevel2,"Level2.tmx","Default"));
			break;
		}
	}

	public void setEndScreen() {
		EndScreen endScreen = new EndScreen(this);
		this.setScreen(endScreen);
	}


	@Override
	public void dispose () {

	}

}
