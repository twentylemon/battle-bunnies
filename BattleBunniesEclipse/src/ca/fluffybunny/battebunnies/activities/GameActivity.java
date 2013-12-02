package ca.fluffybunny.battebunnies.activities;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.SurfaceView;
=======
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
>>>>>>> 2b7df67eb9912652953fb75f5a69db28326646dc
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.game.AIPlayer;
import ca.fluffybunny.battlebunnies.game.Channel;
import ca.fluffybunny.battlebunnies.game.GameInfo;
import ca.fluffybunny.battlebunnies.game.GameMaster;
import ca.fluffybunny.battlebunnies.game.Player;
import ca.fluffybunny.battlebunnies.game.Port;
import ca.fluffybunny.battlebunnies.game.RandomGenerator;
import ca.fluffybunny.battlebunnies.game.StartInfo;
import ca.fluffybunny.battlebunnies.game.TerrainGenerator;
import ca.fluffybunny.battlebunnies.game.Weapon;

public class GameActivity extends Activity {
	
	private Thread gameThread;
	private GameMaster gameMaster = null;
	private Thread playerThread = null;
	private Player player = null;
	private Thread aiThread = null;
	private AIPlayer aiPlayer = null;

	private boolean isMultiplayer;
	private boolean isServer;
	private BluetoothDevice bluetoothDevice;
	private BluetoothSocket bluetoothSocket;
	private int aiDifficulty;
	private int terrainType;
	private String[] playerNames;
	private String[] temp = {"",""};
	private List<Weapon> weaponList = new GameInfo(temp, 100, 100, new RandomGenerator()).getWeaponList();
	
	private SurfaceView gameView;
	private SurfaceHolder surfaceHolder;
	private SeekBar power;
	private SeekBar angle;
	private Button fire;
	private TextView powtext;
	private TextView angletext;
	
	private int shotPower;
	private int shotAngle;
	
	/** Intent keys */
	public static final String IS_MULTIPLAYER = "isMultiplayer";
	public static final String IS_SERVER = "isServer";
	public static final String BLUETOOTH_DEVICE = "bluetoothDevice";
	public static final String BLUETOOTH_SOCKET = "bluetoothSocket";
	public static final String AI_DIFFICULTY = "aiDifficulty";
	public static final String PLAYER_NAMES = "playerNames";
	public static final String TERRAIN_TYPE = "terrainType";
	
	public static final int TERRAIN_TYPE_RANDOM = 0;

	public static final int NUM_PLAYERS = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		
		Intent intent = getIntent();
		isMultiplayer = intent.getBooleanExtra(IS_MULTIPLAYER, false);
		isServer = intent.getBooleanExtra(IS_SERVER, false);
		bluetoothDevice = intent.getParcelableExtra(BLUETOOTH_DEVICE);
		bluetoothSocket = intent.getParcelableExtra(BLUETOOTH_SOCKET);
		aiDifficulty = intent.getIntExtra(AI_DIFFICULTY, 0);
		playerNames = intent.getStringArrayExtra(PLAYER_NAMES);
		terrainType = intent.getIntExtra(TERRAIN_TYPE, TERRAIN_TYPE_RANDOM);
		
		gameView = (SurfaceView) findViewById(R.id.game);
		surfaceHolder = gameView.getHolder();
		
		initControls();
		if (isMultiplayer){
			initMultiplayer();
		}
		else {
			initSinglePlayer();
		}
	}
	public void initControls(){
		
		
		
		power = (SeekBar) findViewById(R.id.seekBar1);		
		power.setOnSeekBarChangeListener(new OnSeekBarChangeListener() { 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotPower = progress;
            } 
            public void onStartTrackingTouch(SeekBar seekBar) {} 
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });   
		angle = (SeekBar) findViewById(R.id.seekBar2);		
		angle.setOnSeekBarChangeListener(new OnSeekBarChangeListener() { 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                shotAngle = progress;
            } 
            public void onStartTrackingTouch(SeekBar seekBar) {} 
            public void onStopTrackingTouch(SeekBar seekBar) {}
        }); 
		
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	
	/**
	 * Closes the game activity. Stop the threads. Just interrupt them as they are discarded
	 * immediately after this anyways.
	 */
	@Override
	public void onBackPressed(){
		if (playerThread != null){
			player.stop();
			playerThread.interrupt();
		}
		if (aiThread != null){
			aiPlayer.stop();
			aiThread.interrupt();
		}
		if (gameThread != null){
			gameThread.interrupt();
		}
		super.onBackPressed();
	}
	
	
	/**
	 * Initializes a single player game. Basically! We run three (four [well, five]) threads.
	 * Most of them are blocking so it's ok!
	 * GameMaster - needs master ports from a Channel
	 * Player - needs corresponding client ports from a Channel
	 * AIPlayer - needs corresponding client ports from a Channel
	 * Player has a sub-thread that updates the canvas
	 * In addition, the UI thread is always running, so five. Five threads.
	 */
	public void initSinglePlayer(){
		TerrainGenerator generator = null;
		switch (terrainType){
		case TERRAIN_TYPE_RANDOM: default:
			generator = new RandomGenerator();
			break;
		}
		
		Channel[] channels = new Channel[NUM_PLAYERS];
		Port[] clients = new Port[NUM_PLAYERS];
		Port[] masters = new Port[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++){
			channels[i] = new Channel();
			clients[i] = channels[i].asClientPort();
			masters[i] = channels[i].asMasterPort();
		}
		
		player = new Player(0, playerNames[0], clients[0], surfaceHolder);
		playerThread = new Thread(player);
		playerThread.start();

		aiPlayer = new AIPlayer(1, playerNames[1], clients[1]);
		aiThread = new Thread(aiPlayer);
		aiThread.start();

		StartInfo startInfo = new StartInfo(playerNames, masters, generator);
		/*
		 * requires API 13
		android.graphics.Point size = new android.graphics.Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		GameMaster.setGameWidth(size.x);
		GameMaster.setGameHeight(size.y);
		*/
		GameMaster.setGameWidth(getWindowManager().getDefaultDisplay().getWidth());
		GameMaster.setGameHeight(getWindowManager().getDefaultDisplay().getHeight());
		gameMaster = new GameMaster(startInfo);
		gameThread = new Thread(gameMaster);
		gameThread.start();
	}

	
	/**
	 * Initializes a multiplayer game.
	 */
	public void initMultiplayer(){
	}
}
