package com.neet.blockbunny.states;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.neet.blockbunny.handlers.GameButton;
import com.neet.blockbunny.handlers.GameStateManager;
import com.neet.blockbunny.main.Game;

public class Menu2 extends GameState {
	
	private TextureRegion reg;
	
	private ImageButton[] buttons;
	
	public Menu2(GameStateManager gsm) {
		
		super(gsm);
		
		reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, 320, 240);
		
		//TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("hud"), 0, 0, 32, 32);
		TextureRegion playButton = new TextureRegion(Game.res.getTexture("play"),0,0,128,32);
		TextureRegion trackButton = new TextureRegion(Game.res.getTexture("track"),0,0,128,32);
		TextureRegion leaderButton = new TextureRegion(Game.res.getTexture("leader"),0,0,256,32);
		buttons = new ImageButton[3];
		//for(int row = 0; row < buttons.length; row++) {
		//	for(int col = 0; col < buttons[0].length; col++) {
		//		buttons[row][col] = new GameButton(buttonReg, 80 + col * 40, 200 - row * 40, cam);
		//		buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
		//	}
		//}
		buttons[0] = new ImageButton(new TextureRegionDrawable(playButton));
		buttons[1] = new ImageButton(new TextureRegionDrawable(trackButton));
		buttons[2] = new ImageButton(new TextureRegionDrawable(leaderButton));
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		
	}
	
	public void handleInput() {
	}
	
	public void update(float dt) {
		
		handleInput();
		
	//	for(int row = 0; row < buttons.length; row++) {
	//		for(int col = 0; col < buttons[0].length; col++) {
	//			buttons[row][col].update(dt);
	//			if(buttons[row][col].isClicked()) {
	//				Play.level = row * buttons[0].length + col + 1;
	//				Game.res.getSound("levelselect").play();
	//				gsm.setState(GameStateManager.PLAY);
	//			}
	//		}
	//	}
		if(buttons[0].isPressed()){
			System.out.println("Play test");
		}
		if(buttons[1].isPressed()){
			System.out.println("track test");
		}
		if(buttons[2].isPressed()){
			System.out.println("leader test");
		}
	}
	
	public void render() {
		
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		sb.draw(reg, 0, 0);
		sb.end();
		for(int i =0; i<3;i++){
		}
		//for(int row = 0; row < buttons.length; row++) {
		//	for(int col = 0; col < buttons[0].length; col++) {
	//			buttons[row][col].render(sb);
	//		}
	//	}
		
	}
	
	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}
	
}
