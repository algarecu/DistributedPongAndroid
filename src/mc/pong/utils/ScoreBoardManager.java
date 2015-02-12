package mc.pong.utils;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ScoreBoardManager {
    Map<String, ScoreBoard> scoreBoards = new HashMap<String, ScoreBoard>();
    
    
    public void draw(Canvas canvas, Paint paint){
    	for(ScoreBoard board: scoreBoards.values()){
    		board.draw(canvas, paint);
    	}
    }
    
    public void updateScoreBoard(ScoreBoardInfo info){
    	ScoreBoard myboard;
    	if((myboard = scoreBoards.get(info.ownerAddress)) != null){
    		myboard.setScoreBoard(info);
    	}
    	else{
    		ScoreBoard newBoard = new ScoreBoard(info);
    		newBoard.setOrder(scoreBoards.size());
    		scoreBoards.put(info.ownerAddress, newBoard);
    	}
    }

	public void initLocalBoard() {
		scoreBoards.put("Me", new ScoreBoard("Me"));
	}

	public void minusMyScore() {
		scoreBoards.get("Me").minusScore();
	}

	public ScoreBoardInfo getMyInfo() {
		return scoreBoards.get("Me").getInfo();
	}
}
