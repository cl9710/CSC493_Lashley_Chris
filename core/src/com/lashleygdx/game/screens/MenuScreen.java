package com.lashleygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;	// not listed in book, needed for show()
import com.lashleygdx.game.world.Assets;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.util.AudioManager;
import com.lashleygdx.game.util.CharacterSkin;
import com.lashleygdx.game.util.GamePreferences;

/**
 * MenuScreen controls the game start menu screen
 * @author Chris Lashley
 */
public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();

	private Stage stage;
	private Skin skinMurderKitty;
	private Skin skinLibgdx;

	// menu
	private Image imgBackground;
	private Image imgLogo;
	private Image imgInfo;
	private Image imgBirds;
	private Image imgKitty;
	private Button btnMenuPlay;
	private Button btnMenuOptions;

	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private SelectBox<CharacterSkin> selCharSkin;
	private Image imgCharSkin;
	private CheckBox chkShowFpsCounter;

	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	/**
	 * constructor
	 * @param game
	 */
	public MenuScreen (Game game)
	{
		super(game);
	}

	/**
	 * draws the main menu screen
	 */
	@Override
	public void render (float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (debugEnabled)
		{
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0)
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
//		Table.drawDebug(stage);  doesnt exist anymore
//		stage.setDebugAll(true);	// fix for line above
	}

	/**
	 * set screen size to fill window
	 */
	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	/**
	 * show the main menu
	 */
	@Override
	public void show()
	{
		AudioManager.instance.play(Assets.instance.music.normalSong);
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	/**
	 * hide the main menu
	 */
	@Override
	public void hide()
	{
		stage.dispose();
		skinMurderKitty.dispose();
		skinLibgdx.dispose();
	}

	/**
	 * pause the game (not used currently since not doing android)
	 */
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * build the main menu "stage" composed of 5 layers
	 */
	private void rebuildStage()
	{
		skinMurderKitty = new Skin(Gdx.files.internal(Constants.SKIN_MURDERKITTY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogosLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();

		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}

	/**
	 * main menu background layer
	 * @return layer
	 */
	private Table buildBackgroundLayer()
	{
		Table layer = new Table();
		// + background
		imgBackground = new Image(skinMurderKitty, "background");
		layer.add(imgBackground);
		return layer;
	}

	/**
	 * main menu objects (decoration) layer
	 * @return layer
	 */
	private Table buildObjectsLayer()
	{
		Table layer = new Table();
		// + birds
		imgBirds = new Image(skinMurderKitty, "birds");
		layer.addActor(imgBirds);
		imgBirds.setPosition(135, 80);
		// + bunny
		imgKitty = new Image(skinMurderKitty, "kitty");
		layer.addActor(imgKitty);
		imgKitty.setPosition(355, 40);
		return layer;
	}

	/**
	 * main menu logo layer
	 * @return layer
	 */
	private Table buildLogosLayer()
	{
		Table layer = new Table();
		layer.left().top();
		// + game logo
		imgLogo = new Image(skinMurderKitty, "logo");
		layer.add(imgLogo);
		layer.row().expandY();
		// + info logos
		imgInfo = new Image(skinMurderKitty, "info");
		layer.add(imgInfo).bottom();
		if (debugEnabled) layer.debug();
		return layer;
	}

	/**
	 * main menu controls (button) layer
	 * @return layer
	 */
	private Table buildControlsLayer()
	{
		Table layer = new Table();
		layer.right().bottom();
		// + play button
		btnMenuPlay = new Button(skinMurderKitty, "play");
		layer.add(btnMenuPlay);
		btnMenuPlay.addListener(new ChangeListener()
		{
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onPlayClicked();
			}
		});
		layer.row();
		// + options button
		btnMenuOptions = new Button(skinMurderKitty, "options");
		layer.add(btnMenuOptions);
		btnMenuOptions.addListener(new ChangeListener()
		{
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onOptionsClicked();
			}
		});
		if (debugEnabled) layer.debug();
		return layer;
	}

	/**
	 * main menu options menu layer
	 * @return winOptions
	 */
	private Table buildOptionsWindowLayer()
	{
		winOptions = new Window("Options", skinLibgdx);
		// + audio settings: sound/music checkbox and volume sliders
		winOptions.add(buildOptWinAudioSettings()).row();
		// + character skin: selection box (white, gray, brown)
		winOptions.add(buildOptWinSkinSelection()).row();
		// + debug: show FPS counter
		winOptions.add(buildOptWinDebug()).row();
		// + separator and buttons (save, cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// hide options window by default
		winOptions.setVisible(false);
		if (debugEnabled) winOptions.debug();
		// let tablelayout recalculate widget sizes and positions
		winOptions.pack();
		// move options window to bottom right corner
		winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}

	/**
	 * start game when play button clicked
	 */
	private void onPlayClicked()
	{
		game.setScreen(new GameScreen(game));
	}

	/**
	 * open options menu when options button clicked
	 */
	private void onOptionsClicked()
	{
		loadSettings();
		btnMenuPlay.setVisible(false);
		btnMenuOptions.setVisible(false);
		winOptions.setVisible(true);
	}

	/**
	 * populate options menu with current settings when opened
	 */
	private void loadSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		selCharSkin.setSelectedIndex(prefs.charSkin);
		onCharSkinSelected(prefs.charSkin);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}

	/**
	 * save current option menu settings
	 */
	private void saveSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.charSkin = selCharSkin.getSelectedIndex();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}

	/**
	 * display current character skin color/image in options menu
	 * @param index
	 */
	private void onCharSkinSelected (int index)
	{
		CharacterSkin skin = CharacterSkin.values()[index];
		imgCharSkin.setColor(skin.getColor());
	}

	/**
	 * save option menu settings when save is clicked
	 */
	private void onSaveClicked()
	{
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}

	/**
	 * close options menu without saving
	 */
	private void onCancelClicked()
	{
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		winOptions.setVisible(false);
		AudioManager.instance.onSettingsUpdated();
	}

	/**
	 * construct options menu audio section
	 * @return tbl
	 */
	private Table buildOptWinAudioSettings()
	{
		Table tbl = new Table();
		// + title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();
		// + checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}

	/**
	 * construct options menu char skin selection section
	 * @return tbl
	 */
	private Table buildOptWinSkinSelection()
	{
		Table tbl = new Table();
		// + title: "Character Skin"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
		// + drop down box filled with skin items
		selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);

		selCharSkin.setItems(CharacterSkin.values());

		selCharSkin.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onCharSkinSelected(((SelectBox<CharacterSkin>)actor).getSelectedIndex());
			}
		});
		tbl.add(selCharSkin).width(120).padRight(20);
		// + skin preview image
		imgCharSkin = new Image(Assets.instance.cat.cat);
		tbl.add(imgCharSkin).width(50).height(50);
		return tbl;
	}

	/**
	 * construct options menu debug section (fps display)
	 * @return tbl
	 */
	private Table buildOptWinDebug()
	{
		Table tbl = new Table();
		// + title: "Debug"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx);
		tbl.add(new Label("Show FPS Counter", skinLibgdx));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		return tbl;
	}

	/**
	 * construct options menu save/cancel buttons section
	 * @return tbl
	 */
	private Table buildOptWinButtons()
	{
		Table tbl = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + save button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onSaveClicked();
			}
		});
		// + cancel button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				onCancelClicked();
			}
		});
		return tbl;
	}
}