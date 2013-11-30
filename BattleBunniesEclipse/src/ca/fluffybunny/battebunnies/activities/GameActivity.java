package ca.fluffybunny.battebunnies.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ca.fluffybunny.battlebunnies.R;
import ca.fluffybunny.battlebunnies.game.GameMaster;

public class GameActivity extends Activity {
	
	private GameMaster gameMaster = null;
	private Thread playerThread;
	private Thread gameCanvas;
	private boolean isServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
}
