package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Assets manages game assets
 * @author Chris Lashley
 */
public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	// singleton: prevent instantiation from other classes
	private Assets() {}

	public AssetCat cat;
	public AssetThorn thorn;
	public AssetBird bird;
	public AssetFrog frog;
	public AssetRock rock;
	public AssetLevelDecoration levelDecoration;

	// instantiate an instance of itself
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();

		Gdx.app.debug(TAG,  "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG,  "asset: " + a);
		}

	TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

	// enable texture filtering for pixel smoothing
	for (Texture t : atlas.getTextures())
	{
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	// create game resource objects
	cat = new AssetCat(atlas);
	thorn = new AssetThorn(atlas);
	rock = new AssetRock(atlas);
	bird = new AssetBird(atlas);
	frog = new AssetFrog(atlas);
	levelDecoration = new AssetLevelDecoration(atlas);
	}

	// free up memory for unwanted assets
	//@Override
	public void dispose()
	{
		assetManager.dispose();
	}

	// error handling
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + filename + "'", (Exception)throwable);;
	}

	// error handling
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG,  "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}

	// cat sprite
	public class AssetCat
	{
		public final AtlasRegion cat;

		public AssetCat (TextureAtlas atlas)
		{
			cat = atlas.findRegion("cat");
		}
	}

	// thorn sprite
	public class AssetThorn
	{
		public final AtlasRegion thorn;

		public AssetThorn (TextureAtlas atlas)
		{
			thorn = atlas.findRegion("thorn");
		}
	}

	// rock sprite
	public class AssetRock
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock (TextureAtlas atlas)
		{
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	// bird sprite
	public class AssetBird
	{
		public final AtlasRegion bird;

		public AssetBird (TextureAtlas atlas)
		{
			bird = atlas.findRegion("bird");
		}
	}

	// frog sprite
	public class AssetFrog
	{
		public final AtlasRegion frog;

		public AssetFrog (TextureAtlas atlas)
		{
			frog = atlas.findRegion("frog");
		}
	}

	// level/background sprites
	public class AssetLevelDecoration
	{
		public final AtlasRegion cloud1;
		public final AtlasRegion cloud2;
		public final AtlasRegion cloud3;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion house;
		public final AtlasRegion tree1;
		public final AtlasRegion tree2;

		public AssetLevelDecoration (TextureAtlas atlas)
		{
			cloud1 = atlas.findRegion("cloud1");
			cloud2 = atlas.findRegion("cloud2");
			cloud3 = atlas.findRegion("cloud3");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
			house = atlas.findRegion("house");
			tree1 = atlas.findRegion("tree1");
			tree2 = atlas.findRegion("tree2");

		}
	}
}