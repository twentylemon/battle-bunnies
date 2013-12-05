package ca.fluffybunny.battebunnies.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.game.AIPlayer;
import ca.fluffybunny.battlebunnies.game.BluetoothChannel;
import ca.fluffybunny.battlebunnies.game.Channel;
import ca.fluffybunny.battlebunnies.game.GameInfo;
import ca.fluffybunny.battlebunnies.game.GameMaster;
import ca.fluffybunny.battlebunnies.game.Player;
import ca.fluffybunny.battlebunnies.game.Port;
import ca.fluffybunny.battlebunnies.game.RandomGenerator;
import ca.fluffybunny.battlebunnies.game.StartInfo;
import ca.fluffybunny.battlebunnies.game.TerrainGenerator;
import ca.fluffybunny.battlebunnies.game.Weapon;
import ca.fluffybunny.battlebunnies.util.ConnectionHandler;

@SuppressLint("NewApi")
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
	private int[] playerImages;
	private String[] playerNames;
	private int[] moreTemp = { R.drawable.player_bunny, R.drawable.player2_bunny };
	private String[] temp = {"",""};
	private List<Weapon> weaponList = new GameInfo(moreTemp, temp, 100, 100, new RandomGenerator()).getWeaponList();
	
	private SurfaceView gameView;
	private SurfaceHolder surfaceHolder;
	private SeekBar power;
	private SeekBar angle;
	private Button fire;
	private TextView powtext;
	private TextView angletext;
	private Spinner weaponsList;
	
	private int shotPower;
	private int shotAngle;
	
	public ConnectionHandler conHandle;
	
	/** Intent keys */
	public static final String IS_MULTIPLAYER = "isMultiplayer";
	public static final String IS_SERVER = "isServer";
	public static final String BLUETOOTH_DEVICE = "bluetoothDevice";
	public static final String BLUETOOTH_SOCKET = "bluetoothSocket";
	public static final String AI_DIFFICULTY = "aiDifficulty";
	public static final String PLAYER_IMAGES = "playerImages";
	public static final String PLAYER_NAMES = "playerNames";
	public static final String TERRAIN_TYPE = "terrainType";
	
	public static final int TERRAIN_TYPE_RANDOM = 0;

	public static final int NUM_PLAYERS = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		conHandle= new ConnectionHandler(this);
		
		Intent intent = getIntent();
		isMultiplayer = intent.getBooleanExtra(IS_MULTIPLAYER, false);
		isServer = intent.getBooleanExtra(IS_SERVER, false);
		bluetoothDevice = intent.getParcelableExtra(BLUETOOTH_DEVICE);
		bluetoothSocket = intent.getParcelableExtra(BLUETOOTH_SOCKET);
		aiDifficulty = intent.getIntExtra(AI_DIFFICULTY, 0);
		playerImages = intent.getIntArrayExtra(PLAYER_IMAGES);
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
		
		fire= (Button) findViewById(R.id.button1);	
		fire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				firePressed();
			}
		});
		populateSpinner();
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	private void populateSpinner(){
		
		weaponsList= (Spinner) findViewById(R.id.spinner1);
		List<String> weap= new ArrayList<String>();
		for(Weapon w: weaponList){
			weap.add(w.getName());		
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,weap);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weaponsList.setAdapter(dataAdapter);
	}
	
	private void firePressed(){
		String s=(String) weaponsList.getSelectedItem();
		Weapon weap= null;
		for(Weapon w: weaponList){
			if(w.getName().equals(s)) weap =w;
		}		
		player.fireSomething(shotPower, shotAngle, weap);

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
        TerrainGenerator generator = getTerrainGenerator(terrainType);
		
		Channel[] channels = new Channel[NUM_PLAYERS];
		Port[] clients = new Port[NUM_PLAYERS];
		Port[] masters = new Port[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++){
			channels[i] = new Channel();
			clients[i] = channels[i].asClientPort();
			masters[i] = channels[i].asMasterPort();
		}
		
		player = new Player(0, playerNames[0], clients[0], surfaceHolder, getApplicationContext());
		playerThread = new Thread(player);
		playerThread.start();

		aiPlayer = new AIPlayer(1, playerNames[1], clients[1]);
		aiThread = new Thread(aiPlayer);
		aiThread.start();

		StartInfo startInfo = new StartInfo(playerImages, playerNames, masters, generator);
		/*
		 * requires API 13
		android.graphics.Point size = new android.graphics.Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		GameMaster.setGameWidth(size.x);
		GameMaster.setGameHeight(size.y);
		*/
		GameMaster.setGameWidth(getWindowManager().getDefaultDisplay().getWidth());
		GameMaster.setGameHeight((int)(0.85 * getWindowManager().getDefaultDisplay().getHeight()));
		gameMaster = new GameMaster(startInfo);
		gameThread = new Thread(gameMaster);
		gameThread.start();
	}
	
	private void fireZeMissiles(){
		String s="Ze Missiles Zey are Fireing, Power: "+shotPower+" ,Angle: "+shotAngle;
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
		
	}
	
	
	/**
	 * Returns the terrain generator.
	 * 
	 * @param type the type of terrain generator to return
	 * @return the terrain generator
	 */
	private TerrainGenerator getTerrainGenerator(int type){
		TerrainGenerator generator = null;
		switch (type){
		case TERRAIN_TYPE_RANDOM: default:
			generator = new RandomGenerator();
			break;
		}
		return generator;
	}

	
	/**
	 * Initializes a multiplayer game.
	 */
	public void initMultiplayer(){
		if (!isServer){
			Toast.makeText(getApplicationContext(), "client", Toast.LENGTH_LONG).show();
			/*
			conHandle.start();
			bluetoothSocket = null;
			bluetoothSocket = conHandle.getBluetoothSocket();
			*/
			try {
				BluetoothServerSocket serverSocket = BluetoothAdapter.getDefaultAdapter().
						listenUsingRfcommWithServiceRecord(ConnectionHandler.NAME, ConnectionHandler.MY_UUID);
				bluetoothSocket = serverSocket.accept();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Port port = new BluetoothChannel(bluetoothSocket);
			player = new Player(1, playerNames[1], port, surfaceHolder, getApplicationContext());
			playerThread = new Thread(player);
			playerThread.start();
		}
		else {
			Toast.makeText(getApplicationContext(), "server", Toast.LENGTH_LONG).show();
			/*
			conHandle.connect(bluetoothDevice);
            bluetoothSocket = conHandle.getBluetoothSocket();
            */
			try {
				bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(ConnectionHandler.MY_UUID);
				bluetoothSocket.connect();
				Log.e("tag", "" + bluetoothSocket.isConnected());
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            TerrainGenerator generator = getTerrainGenerator(terrainType);

    		Channel channel = new Channel();
    		Port client = channel.asClientPort();
    		Port server = channel.asMasterPort();
    		Port remote = new BluetoothChannel(bluetoothSocket);
    		Port[] masters = { server, remote };
    		
    		player = new Player(0, playerNames[0], client, surfaceHolder, getApplicationContext());
    		playerThread = new Thread(player);
    		playerThread.start();

    		StartInfo startInfo = new StartInfo(playerImages, playerNames, masters, generator);
    		/*
    		 * requires API 13
    		android.graphics.Point size = new android.graphics.Point();
    		getWindowManager().getDefaultDisplay().getSize(size);
    		GameMaster.setGameWidth(size.x);
    		GameMaster.setGameHeight(size.y);
    		*/
    		GameMaster.setGameWidth(getWindowManager().getDefaultDisplay().getWidth());
    		GameMaster.setGameHeight((int)(0.85 * getWindowManager().getDefaultDisplay().getHeight()));
    		gameMaster = new GameMaster(startInfo);
    		gameThread = new Thread(gameMaster);
    		gameThread.start();
		}
	}
}
