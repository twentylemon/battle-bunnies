import java.awt.Color;
import java.io.Serializable;
import java.util.Random;
public class RandomGenerator implements TerrainGenerator ,Serializable{

	
	public int[][] generateTerrain(int width, int height) {
		int[][] c= new int[width][height];
		c=topline(c,width,height);
		Random rng= new Random();
		c= smooth(c,rng.nextInt(width/10));
		c=fillLower(c,Terrain.GRASS,Terrain.GRASS,Terrain.GRASS);			
		return c;
	}
	
	/**
	 * Takes an empty array and first chooses the type(peak or valley) and
	 *  balance point for that type. the creates a new top line with a flatter 
	 *  area where the bunnies start.
	 * 
	 * @param c an empty int[][]
	 * @param width the width of the terrain
	 * @param height the height of the terrain
	 * @return the array c with a top line of terrain
	 */
	public int[][] topline(int[][] c,int width, int height){
		
		int prev;
		Random rng= new Random();		
		
		boolean b=rng.nextBoolean();
		int scale= width/4;
		int modifier= rng.nextInt(scale);
		if(!b)scale*=-1;		
		int central= (width/2)+modifier;
		
		boolean peak= rng.nextBoolean();		
		int type=1;
		if(!peak)type*=-1;
		if(peak)prev=height/4;
		else{prev=height-height/4;}
		
		int p1w= rng.nextInt(width/20)+20;
		int p2w= rng.nextInt(width/20)+20;
		
		int p1cent=(width/10)*2;
		int p2cent= width-((width/10)*2);		
		
		for(int i=0;i<c.length;i++){			
			
			if(i==central){ 
				type*=-1;
				c[i][prev]=1;
				System.out.println("I Switched");
				System.out.println(type);
			}
			else if((i>p1cent-p1w && i<p1cent+p1w)||(i>p2cent-p2w && i<p2cent+p2w)){
				int next=rng.nextInt(3);
				int bias=rng.nextInt(10);
				if(bias<=4) next*=-1; 
				next*=type;
				if(prev+next<height-(height/10)&&prev+next>height/10){
					prev=prev+next;
				}
				c[i][prev]=1;	
				
			}
			else{
				int next=rng.nextInt(3);
				int bias=rng.nextInt(25);
				if(bias<=3) next*=-1; 
				next*=type;
				if(prev+next<height-(height/10)&&prev+next>height/10){
					prev=prev+next;
				}
				c[i][prev]=1;	
			}
			
		}
		
		return c;
	}
	
	/**
	 * Takes in a generated  terrain array and averages the top line over
	 * a fact values to the left and right of it, creating a smoother line
	 * 
	 * @param c the terrain array
	 * @param fact the amount of coordinates on either side to be averaged
	 * @return the smoothed array c
	 */
	public int[][] smooth(int[][] c,int fact){
		
		int count=0;
		int total=0;
		for(int i=0;i<c.length;i++){
			int temp= i-fact;
			count=0;
			total=0;
			//System.out.println(i);
			for(int j= 0;j<2*fact+1; j++){
				if(temp>=0 && temp<c.length){
					total+= getVal(c,temp);					
					count++;
				}
				temp++;
			}
			
			total/=count;
			int t=getVal(c,i);
			c[i][t]=0;
			
			if(total< c[i].length)c[i][total]=1;
			else {c[i][t]=1;}
		}		
		
		return c;
	}

	public int getVal(int[][] c, int loc){
		
		for(int i=0;i<c[loc].length;i++){
			if(c[loc][i]==1)return i;
		}
		return 0;
	}
	
	/**
	 * Takes a terrain array and finds the top line fills the soil and grass below it
	 * 
	 * @param c the unfilled terrain array
	 * @param top represents the color of the top grass layer
	 * @param soil represents the color of the under soil
	 * @param sky represents the color of the sky
	 * @return c the filled terrain array
	 */
	public int[][] fillLower(int[][] c, int top, int soil,int sky){
		
		boolean lower=false;
		for(int i=0;i<c.length;i++){
			lower= false;
			int grassDepth=30;
			for(int j=c[i].length-1;j>=0;j--){
								
				if(c[i][j]!= 0){
					lower=true;
					c[i][j]=1;
				}
				if(lower && grassDepth>0){
					grassDepth--;
					c[i][j]=top;
					
				}				
				if(lower && grassDepth<=0){
					c[i][j]=soil;					
				}
				if(!lower){
					c[i][j]=sky;
				}
			}			
		}		
		return c;		
	}

}
