package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class ScoreBox extends Drawable {
	public int pscore;
	public int width;
	public int height;
	private Paint PAINTBOX1=new Paint();
	private Paint PAINTBOX2=new Paint();
	private Paint PAINTTEXT=new Paint();
	
	public ScoreBox(int width, int height){
		this.width = width;
		this.height = height;
		PAINTBOX1.setColor(Color.parseColor("#809BE5"));		
		PAINTBOX2.setColor(Color.parseColor("#EE647F"));		
		PAINTTEXT.setColor(Color.parseColor("#000000"));
	}

	
	public void draw(Canvas can, int score, int player){
		RectF rect=null;
		Paint temp=null;
		int xloc = 10;
		int yloc = (int)(height * 0.15) - 15;
		int size = (int)(height*0.15) - 15;
		
		if(player == 1){
			rect = new RectF(0,0,(int)(width*0.15),(int)(height*0.15));
			temp = PAINTBOX1;			
		}
		else{
			rect = new RectF(width - (int)(width * 0.15) , 0, width, (int)(height * 0.15));
			temp = PAINTBOX2;
			xloc = width - (int)(width * 0.15) + 10;
		}
		
		can.drawRoundRect(rect, 10, 10, temp);
		PAINTTEXT.setTextSize(size);
		can.drawText("P"+(player+1)+": "+ score, xloc, yloc, PAINTTEXT);
		
		
	}
	
	@Override
	public void draw(Canvas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter arg0) {
		// TODO Auto-generated method stub

	}

}
