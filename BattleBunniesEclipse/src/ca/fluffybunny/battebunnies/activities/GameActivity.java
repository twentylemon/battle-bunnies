package ca.fluffybunny.battebunnies.activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.game.GameMaster;
import ca.fluffybunny.battlebunnies.game.GameView;
import ca.fluffybunny.battlebunnies.game.Player;

public class GameActivity extends Activity {
	
	private GameMaster gameMaster = null;
	private Thread playerThread;
	private Player player;
	private boolean isMultiplayer;
	private boolean isServer;
	private BluetoothDevice bluetoothDevice;
	private int aiDifficulty;
	private GameView gameView;
	
	/** Intent keys */
	public static final String IS_MULTIPLAYER = "isMultiplayer";
	public static final String IS_SERVER = "isServer";
	public static final String BLUETOOTH_DEVICE = "bluetoothDevice";
	public static final String AI_DIFFICULTY = "aiDifficulty";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		Intent intent = getIntent();
		isMultiplayer = intent.getBooleanExtra(IS_MULTIPLAYER, false);
		isServer = intent.getBooleanExtra(IS_SERVER, false);
		bluetoothDevice = intent.getParcelableExtra(BLUETOOTH_DEVICE);
		aiDifficulty = intent.getIntExtra(AI_DIFFICULTY, 0);
		
		gameView = (GameView) findViewById(R.id.game);
		SurfaceHolder holder = gameView.getHolder();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
}
