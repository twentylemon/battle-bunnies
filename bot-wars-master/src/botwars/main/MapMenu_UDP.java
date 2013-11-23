package botwars.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**********************************************************************************
 * 
 * Inherit from superclass MapMenu and add dialog to enter IP address
 * This IP address is passed to MultiPlayer_UDP
 * 
 **********************************************************************************/

public class MapMenu_UDP extends MapMenu{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		 GridView gridView = (GridView) findViewById(R.id.gridview);
	        gridView.setAdapter(new ImageAdapter(this));
	        gridView.setOnItemClickListener(new OnItemClickListener()
	        {
	        public void onItemClick(AdapterView<?> parent,
	        View v, int position, long id)
	        {if(position<3)
	       {
	        BotWars.setMap(position);
	        makeIPDialog();

	       }
	        else Toast.makeText(getBaseContext(),
	               "Map Not Available",
	                Toast.LENGTH_SHORT).show();
				
	        }
	        });
	}

	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_SHORT).show();
	    }
	    return null;
	}
	private void makeIPDialog() {


		final Dialog ipDialog = new Dialog(MapMenu_UDP.this);

		ipDialog.setContentView(R.layout.ip_dialog);
		ipDialog.setTitle("Your IP address is: "+getLocalIpAddress());
		
		final EditText edtx_ip = (EditText) ipDialog.findViewById(R.id.edtx_ip);

		edtx_ip.setHint("Enter IP Address of Player 2");
		
		
		//edtx_ip.setText(getLocalIpAddress());
		//edtx_ip.setSelection(edtx_ip.getText().length());
		
		ImageView img_lan = (ImageView) ipDialog.findViewById(R.id.img_lan);
		img_lan.setImageResource(R.drawable.mp_icon);
		ipDialog.show();
		
				edtx_ip.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
				
					
					MultiPlayer_UDP.setIPAdd(edtx_ip.getText().toString());
					ipDialog.dismiss();
Intent openMP_BotWars_UDP = new Intent(MapMenu_UDP.this, MultiPlayer_UDP.class);
startActivity(openMP_BotWars_UDP);
					finish();
					return true;
				}
				return false;
			}
		
		});
	}
	

}