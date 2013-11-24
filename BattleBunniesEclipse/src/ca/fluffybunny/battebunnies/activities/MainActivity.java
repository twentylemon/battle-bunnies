package ca.fluffybunny.battebunnies.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ca.fluffybunny.battlebunnies.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Called when Profile is clicked. Starts up the settings activity.
     *
     * @param view the button clicked
     */
    public void onProfileClicked(View view){
        //start the settings activity
    }


    /**
     * Called when the Campaign button is clicked. Starts the campaign loader activity.
     *
     * @param view the button clicked
     */
    public void onCampaignClicked(View view){
        //start the campaign loader activity
    }

    /**
     * Called when Quick Play is clicked. Starts up the weapon select activity.
     *
     * @param view the button clicked
     */
    public void onQuickPlayClicked(View view){
        //start the weapon select activity
    }


    /**
     * Called when mutliplayer is clicked. Starts up the bluetooth handler activity.
     *
     * @param view the button clicked
     */
    public void onMultiplayerClicked(View view){
        //start the bluetooth activity
    }
}
