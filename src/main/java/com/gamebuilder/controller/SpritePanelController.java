package com.gamebuilder.controller;

import java.util.ArrayList;


import org.apache.log4j.Logger;

import com.gamebuilder.commands.SpriteCommand;
import com.gamebuilder.commands.TimerCommand;
import com.gamebuilder.helpers.ActionButtonListener;
import com.gamebuilder.helpers.BackgroundButtonListener;
import com.gamebuilder.helpers.BackgroundMusicButtonListener;
import com.gamebuilder.helpers.DragEventListener;
import com.gamebuilder.helpers.EventButtonListener;
import com.gamebuilder.helpers.GameButtonListener;
import com.gamebuilder.helpers.GameConstants;
import com.gamebuilder.helpers.ImageButtonListener;
import com.gamebuilder.helpers.ReadoutsButtonListener;
import com.gamebuilder.helpers.SpriteKeyListener;
import com.gamebuilder.model.SpritePanelModel;
import com.gamebuilder.sprite.GameSprite;
import com.gamebuilder.ui.GamePanelView;
import com.gamebuilder.ui.SpritePanelView;
import com.gamebuidler.readouts.*;

public class SpritePanelController {

	private SpritePanelModel spritePanelModel;
	private SpritePanelView spritePanelView;
	private GamePanelView gamePanelView;
	private DragEventListener dragEventListener;
	private SpriteKeyListener spriteKeyListener; 
	private boolean gamePlay = false;
	
	private static Logger gameControllerLog = Logger.getLogger("gameControllerLogger");
	
	public SpritePanelController(SpritePanelModel spritePanelModel, SpritePanelView spritePanelView, GamePanelView gamePanelView){
		this.spritePanelModel = spritePanelModel;
		this.spritePanelView = spritePanelView;
		this.gamePanelView = gamePanelView;
		this.dragEventListener = new DragEventListener(gamePanelView);
		this.spriteKeyListener = new SpriteKeyListener(gamePanelView);
		
		this.spritePanelView.addActionButtonListener(new ActionButtonListener(this.spritePanelModel));
		this.spritePanelView.addImageButtonListener(new ImageButtonListener(this.spritePanelModel, this.spritePanelView));
		this.spritePanelView.addEventButtonListener(new EventButtonListener(this.spritePanelModel, this.gamePanelView));
		this.spritePanelView.addBackGroundButtonListener(new BackgroundButtonListener(this.spritePanelModel, this.gamePanelView));
		this.spritePanelView.addBackGroundMusicButtonListener(new BackgroundMusicButtonListener());
		this.spritePanelView.addReadoutButtonListener(new ReadoutsButtonListener(this.spritePanelModel, this.gamePanelView));
		this.spritePanelView.addGameButtonListener(new GameButtonListener(this));
		this.gamePanelView.addMouseListener(this.dragEventListener);
		this.gamePanelView.addMouseMotionListener(this.dragEventListener);
		this.gamePanelView.addKeyListener(this.spriteKeyListener);
		gameLoop();
	}
	
	public void gameLoop() {
		new Thread() {
			public void run() {
				while (true) {
					if(gamePlay){
						for (GameSprite gameSprite : gamePanelView.getGameSpriteArray()) {
							SpriteCommand spriteCommand = new SpriteCommand(gameSprite);
							spriteCommand.execute();
							gamePanelView.repaint();
						}
						for (GameTime gametime : gamePanelView.getGameTimeArray()) {
							TimerCommand timerCommand = new TimerCommand(gametime);
							timerCommand.execute();
						}
					}
					try {
						java.lang.Thread.sleep(30);
					} catch (InterruptedException e) {
						gameControllerLog.error("Error in game loop thread", e);
					}
				}
			}
		}.start();
	}

	public SpritePanelModel getSpritePanelModel() {
		return spritePanelModel;
	}

	public void setSpritePanelModel(SpritePanelModel spritePanelModel) {
		this.spritePanelModel = spritePanelModel;
	}

	public SpritePanelView getSpritePanelView() {
		return spritePanelView;
	}

	public void setSpritePanelView(SpritePanelView spritePanelView) {
		this.spritePanelView = spritePanelView;
	}

	public GamePanelView getGamePanelView() {
		return gamePanelView;
	}

	public void setGamePanelView(GamePanelView gamePanelView) {
		this.gamePanelView = gamePanelView;
	}

	public DragEventListener getDragEventListener() {
		return dragEventListener;
	}

	public void setDragEventListener(DragEventListener dragEventListener) {
		this.dragEventListener = dragEventListener;
	}
	
	public boolean isGamePlay() {
		return gamePlay;
	}

	public void setGamePlay(boolean gamePlay) {
		this.gamePlay = gamePlay;
	}
}
