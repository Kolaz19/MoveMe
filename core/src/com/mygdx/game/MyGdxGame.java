package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	public Hero hero;

	@Override
	public void create () {
		//Create char
		hero = new Hero(4,1,14);
		setScreen(new MainMenu(this));
	}

	public void chooseLevel(int levelToPlay) {
		Enemy[] enemies;
		switch (levelToPlay) {
			//Level 1
			case 1:
			hero.reset(3,1);
			enemies = new Enemy[1];
			enemies[0] = new Enemy(1,3);
			setScreen(new LevelBasic(this,1,hero, enemies, "Level1.tmx", "Default"));
			break;
			//Level 2
			case 2:
			hero.reset(2,1);
			enemies = new Enemy[2];
			enemies[0] = new Enemy(2, 6);
			enemies[1] = new Enemy(2, 5);
			setScreen(new LevelBasic(this,2,hero,enemies,"Level2.tmx","Default"));
			break;
			//Level 3
			case 3:
			hero.reset(3,2);
			enemies = new Enemy[3];
			enemies[0] = new Enemy(1,3);
			enemies[1] = new Enemy(5,3);
			enemies[2] = new Enemy(2,4);
			setScreen(new LevelBasic(this,3,hero,enemies,"Level3.tmx","Default"));
			break;
			//Level 4
			case 4:
			hero.reset(2,1);
			enemies = new Enemy[4];
			enemies[0] = new Enemy(3,4);
			enemies[1] = new Enemy(3,5);
			enemies[2] = new Enemy(3,7);
			enemies[3] = new Enemy(3,8);
			setScreen(new LevelBasic(this,4,hero,enemies,"Level4.tmx","Default"));
			break;
			//Level 5
			case 5:
			hero.reset(5,4);
			enemies = new Enemy[3];
			enemies[0] = new Enemy(3,3);
			enemies[1] = new Enemy(3,1);
			enemies[2] = new Enemy(2,4);
			setScreen(new LevelBasic(this,5,hero,enemies,"Level5.tmx","Default"));
			break;
			//Level 6


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
