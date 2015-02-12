package mc.pong.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class ScoreBoard {
     int order = 0;
     final int width = 100,
    		  height = 40,
    		  boardDist = 50,
    		  leftBoarderX = 350,
    	      leftBoarderY = 10;
     int leftUpX = leftBoarderX , leftUpY = leftBoarderY;
     String ownerAddress;
     Integer score;
     
	  public ScoreBoard(String ownerAddress){
		  this.ownerAddress = ownerAddress;
		  score = 5;
	  }
	  
	  public ScoreBoard(ScoreBoardInfo info){
		  this.ownerAddress = info.ownerAddress;
		  this.score = info.score;
	  }
	  
	  public ScoreBoardInfo getInfo(){
		  return new ScoreBoardInfo(ownerAddress, score);
	  }
	  
	  public void setScoreBoard(ScoreBoardInfo info){
		  this.score = info.score;
	  }
     
	  public void draw(Canvas canvas, Paint paint) {  
		  
	        paint.setARGB(20, 120, 120, 140);
	        paint.setStyle(Paint.Style.FILL_AND_STROKE);
	        paint.setAntiAlias(true);
	        canvas.drawRect(leftUpX, leftUpY, leftUpX+width, leftUpY+height, paint);
	        paint.setTextSize(10);
	        paint.setARGB(200, 100, 100, 200);
	        canvas.drawText(ownerAddress, leftUpX, leftUpY + height/2, paint);
	        paint.setTextSize(15);
	        canvas.drawText(score.toString(), leftUpX + width*3/4, leftUpY + height/2, paint);
	  }
	  
	  public void setOrder(int order){
		  this.order= order;
		  leftUpY = leftBoarderY + order * boardDist;
	  }
	  
	  public void minusScore(){
		  --score;
	  }
	  
	  public void setScore(int score){
		  this.score = score;
	  }
}
