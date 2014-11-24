package com.neet.blockbunny.states;

import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.echonest.api.v4.EchoNestException;
import com.neet.blockbunny.handlers.GameButton;
import com.neet.blockbunny.handlers.GameStateManager;
import com.neet.blockbunny.main.Game;
import com.neet.blockbunny.music.Track;

public class LevelSelect extends GameState {

	private TextureRegion reg;

	private GameButton[][] buttons;

	public LevelSelect(GameStateManager gsm) {

		super(gsm);

		reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, 320, 240);

		TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("hud"), 0, 0, 32, 32);
		buttons = new GameButton[5][5];
		for(int row = 0; row < 1; row++) {
			for(int col = 0; col < 2; col++) {
				buttons[row][col] = new GameButton(buttonReg, 80 + col * 40, 200 - row * 40, cam);
				buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
			}
		}

		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

	}

	public void handleInput() {
	}

	public void update(float dt) {

		handleInput();

		for(int row = 0; row < 1; row++) {
			for(int col = 0; col < 2; col++) {
				buttons[row][col].update(dt);
				if(buttons[row][col].isClicked()) {
					try {
						if(row==0 && col==1){
							Thread thread = new Thread(){
							public void run(){
							System.out.println("choosing");
							Frame yourJFrame = new Frame();;
							FileDialog fd = new FileDialog(yourJFrame , "Choose a file", FileDialog.LOAD);
							fd.setDirectory("C:\\");
							fd.setFile("*.mp3");
							fd.setVisible(true);
							String filename = fd.getFile();
							
							if (filename == null){
							  System.out.println("You cancelled the choice");
							}
							else{
							  System.out.println("You chose " + filename);
							  gsm.game().res.loadMusic(fd.getDirectory()+filename);
							  Play.song= filename.substring(0,filename.length()-4);
							  gsm.game().getTrack().setTrack(fd.getDirectory()+filename);
								
								
								try{
									Thread thread = new Thread(){
										public void run(){
											gsm.game().getTrack().initilize();
										}
									};
									thread.start();
								}
								catch(Exception e){	}
							}
							
							/*JFileChooser chooser = new JFileChooser();
							FileNameExtensionFilter filter = new FileNameExtensionFilter(
									"MP3 & WAV Images", "mp3", "wav");
							chooser.setFileFilter(filter);
							int returnVal = chooser.showDialog(null, "choose song");
							if(returnVal == JFileChooser.APPROVE_OPTION) {
								chooser.getSelectedFile().getName();
								gsm.game().res.loadMusic(chooser.getSelectedFile().getAbsolutePath());
								String temp =chooser.getSelectedFile().getName();
								Play.song= temp.substring(0,temp.length()-4);
								System.out.println(Play.song);
								gsm.game().getTrack().setTrack(chooser.getSelectedFile().getAbsolutePath());
								
							
								try{
									Thread thread = new Thread(){
										public void run(){
											gsm.game().getTrack().initilize();
										}
									};
									thread.start();
								}
								catch(Exception e){	}
							}
						*/}
						
							};
							thread.start();
						}
						else if(gsm.game().getTrack().getUploader().getUploadProgress().equals("pending")){
							System.out.println("pending");
						}
						else if(gsm.game().getTrack()==null || Play.song == null){
							System.out.println("null track");
						}

						else{
							//Play.level = row * buttons[0].length + col + 1;
							Play.level = 5;
							//Play.tileMap = makeMap();
							Game.res.getSound("levelselect").play();
							gsm.setState(GameStateManager.PLAY);
							gsm.game().getTrack().getTatumIn(-1);
							gsm.game().getTrack().getBeatIn(-1);
							gsm.game().getTrack().getBarIn(-1);
							gsm.game().getTrack().getSectionIn(-11);
							gsm.game().getTrack().getSegmentIn(-1);
							

						}

					} catch (EchoNestException e) {
					}
					catch(NullPointerException e){
						System.out.println("null");
					}

				}
			}
		}

	}

	public void render() {

		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		sb.draw(reg, 0, 0);
		sb.end();

		for(int row = 0; row < 1; row++) {
			for(int col = 0; col < 2; col++) {
				buttons[row][col].render(sb);
			}
		}

	}

	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}

}
