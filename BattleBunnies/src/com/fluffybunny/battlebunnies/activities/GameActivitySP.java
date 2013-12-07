package com.fluffybunny.battlebunnies.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

import com.fluffybunny.battlebunnies.R;
import com.fluffybunny.battlebunnies.game.FireAction;
import com.fluffybunny.battlebunnies.game.GameCanvas;
import com.fluffybunny.battlebunnies.game.GameInfo;
import com.fluffybunny.battlebunnies.game.RandomGenerator;
import com.fluffybunny.battlebunnies.game.Terrain;
import com.fluffybunny.battlebunnies.game.Weapon;

public class GameActivitySP extends Activity {
	
	public static final String PLAYER_NAMES = "playerNames";
	public static final String PLAYER_IMAGES = "playerImages";
	public static final String TERRAIN_TYPE = "terrainType";
	public static final String AI_DIFFICULTY = "aiDifficulty";
	
	public static final int TERRAIN_TYPE_RANDOM = 0;
	
	private SurfaceView gameView;
	private SurfaceHolder surfaceHolder;
	private SeekBar power;
	private SeekBar angle;
	private Button fire;
	private Spinner weaponSpinner;

	private int shotPower;
	private int shotAngle;
	private int aiDifficulty;
	private int terrainType;
	private int[] playerImages;
	private String[] playerNames;
	
	private GameInfo game;
	private GameCanvas gameCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_activity_sp);
		
		gameView = (SurfaceView) findViewById(R.id.game);
		surfaceHolder = gameView.getHolder();
		
		game = makeGameInfo();
		
		power = (SeekBar) findViewById(R.id.seekBar1);		
		power.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){ 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotPower = progress;
            } 
            public void onStartTrackingTouch(SeekBar seekBar){} 
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
		
		angle = (SeekBar) findViewById(R.id.seekBar2);		
		angle.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){ 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotAngle = progress;
            } 
            public void onStartTrackingTouch(SeekBar seekBar){} 
            public void onStopTrackingTouch(SeekBar seekBar){}
        }); 
		
		fire = (Button) findViewById(R.id.button1);	
		fire.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){ 
				firePressed();
			}
		});
		
		populateSpinner(game.getWeaponList());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game_activity_s, menu);
		return true;
	}
	
	
	/**
	 * Stop the canvas thread on back pressed.
	 */
	@Override
	public void onBackPressed(){
		gameCanvas.stop();
		super.onBackPressed();
	}
	
	
	private void firePressed(){
		if (!gameCanvas.isFiring()){
			String selected = (String) weaponSpinner.getSelectedItem();
			//index of should be searching for a weapon
			int id = -1;
			for (Weapon w : game.getWeaponList()){
				id++;
				if (w.getName().equals(selected)){
					break;
				}
			}
			FireAction action = new FireAction(game.getMyID(), shotPower, shotAngle, id);
			action.execute(game);
			
			while (gameCanvas.isFiring()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e){}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e){}
			Random rand = new Random();
			Weapon weapon = game.getWeaponList().get(rand.nextInt(game.getNumWeapons()));
			game.getBunny(1).fireWeapon(80 + rand.nextInt(80), 90 + rand.nextInt(60), weapon);
			new FireAction(1, 80 + rand.nextInt(80), 90 + rand.nextInt(60), rand.nextInt(game.getNumWeapons())).execute(game);
			gameCanvas.setFiring(true);
		}
	}
	

	/**
	 * Initializes the spinner with the list of weapons.
	 * 
	 * @param weaponList the list of all the weapons
	 */
	private void populateSpinner(List<Weapon> weaponList){
		weaponSpinner = (Spinner) findViewById(R.id.spinner1);
		List<String> weap = new ArrayList<String>();
		for (Weapon w : weaponList){
			weap.add(w.getName());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,weap);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weaponSpinner.setAdapter(dataAdapter);
	}
	
	
	/**
	 * Returns the game info for this game.
	 * 
	 * @return the GameInfo
	 */
	private GameInfo makeGameInfo(){
		Intent intent = getIntent();
		aiDifficulty = intent.getIntExtra(AI_DIFFICULTY, 0);
		playerImages = intent.getIntArrayExtra(PLAYER_IMAGES);
		playerNames = intent.getStringArrayExtra(PLAYER_NAMES);
		terrainType = intent.getIntExtra(TERRAIN_TYPE, TERRAIN_TYPE_RANDOM);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = (int)(0.85 * getWindowManager().getDefaultDisplay().getHeight());
		
		Terrain.Generator generator;
		switch (terrainType){
		case TERRAIN_TYPE_RANDOM: default:
			generator = new RandomGenerator();
			break;
		}

		GameInfo g = new GameInfo(playerImages, playerNames, width, height, generator);
		g.setID(0);
		for (int i = 0; i < playerNames.length; i++){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), g.getBunny(i).getImageResource());
			g.getBunny(i).setBitmapImage(bitmap);
		}
		gameCanvas = new GameCanvas(g, surfaceHolder);
		g.getBunny(0).setGameCanvas(gameCanvas);
		return g;
	}
}
