package mc.pong.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PingPongNotify {
   final int SCREEN_WIDTH = 300;
   final int SCREEN_HEIGHT =420;
   int counter;
   int dir;
   int p1X, p1Y, p2X, p2Y, p3X, p3Y;
   final int LENGTH = 20;
   final int DISTANCE = 10;
   
   public PingPongNotify(int dir){
	 counter = 50;
	 this.dir = dir;  
	 switch(dir){
		 case 0:  p1X = DISTANCE; p1Y= SCREEN_HEIGHT/2; 
		          p2X= (int)(LENGTH*Math.sin(Math.PI/3) +DISTANCE); p2Y= (SCREEN_HEIGHT - LENGTH)/2; 
		          p3X= p2X; p3Y= (SCREEN_HEIGHT + LENGTH)/2;
			 break;
		 case 1:  p1X = SCREEN_WIDTH/2; p1Y= DISTANCE; 
		          p2X= (SCREEN_WIDTH - LENGTH)/2;  p2Y= (int)(Math.sin(Math.PI/3)*LENGTH +DISTANCE);
		          p3X= (SCREEN_WIDTH + LENGTH)/2; p3Y= p2Y;
			 break;
		 case 2:  p1X = SCREEN_WIDTH - DISTANCE; p1Y= SCREEN_HEIGHT/2; 
		          p2X= (int)(SCREEN_WIDTH - LENGTH*Math.sin(Math.PI/3)) -DISTANCE; p2Y= (SCREEN_HEIGHT - LENGTH)/2; 
		          p3X= p2X; p3Y= (SCREEN_HEIGHT + LENGTH)/2;
			 break;
		 case 3:  p1X = SCREEN_WIDTH/2; p1Y= SCREEN_HEIGHT - DISTANCE; 
		          p2X= (SCREEN_WIDTH - LENGTH)/2;  p2Y= (int) (SCREEN_HEIGHT -LENGTH*Math.sin(Math.PI/3)-DISTANCE);
		          p3X= (SCREEN_WIDTH + LENGTH)/2; p3Y= p2Y;
			 break;
	 }
   }

  public void draw(Canvas canvas, Paint paint) {
	  if(counter-- >= 0){	      
	        paint.setARGB(200, 0, 180, 0);
	        paint.setStyle(Paint.Style.FILL_AND_STROKE);
	        paint.setAntiAlias(true);
	        
	        
	        Path path = new Path();
	        path.setFillType(Path.FillType.EVEN_ODD);
	        path.moveTo(p1X, p1Y);
	        path.lineTo(p2X, p2Y);
	        path.lineTo(p3X, p3Y);
	        path.lineTo(p1X, p1Y);
	        path.close();
	
	        canvas.drawPath(path, paint);
	  }
  }

  public boolean hasExpire(){
	  return counter<=0;
  }
}
